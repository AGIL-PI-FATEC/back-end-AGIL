package fatec.backend.AGIL.service;

import fatec.backend.AGIL.dto.FuncionarioAtualizacaoDTO;
import fatec.backend.AGIL.dto.FuncionarioCadastroDTO;
import fatec.backend.AGIL.dto.FuncionarioRespostaDTO;
import fatec.backend.AGIL.dto.FuncionarioSenhaDTO;
import fatec.backend.AGIL.model.FuncionarioModel;
import fatec.backend.AGIL.model.GestorModel;
import fatec.backend.AGIL.repository.FuncionarioRepository;
import fatec.backend.AGIL.repository.GestorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private GestorRepository gestorRepository;

    // Instancia o encriptador BCrypt para a senha
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 1. Cadastrar Funcionário
    public FuncionarioRespostaDTO cadastrar(FuncionarioCadastroDTO dto) {
        // Valida se a senha e a confirmação são iguais
        if (!dto.getSenha().equals(dto.getConfirmacaoSenha())) {
            throw new IllegalArgumentException("A senha e a confirmação de senha não são iguais.");
        }

        // Valida se o email já existe
        if (funcionarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Este e-mail já está cadastrado no sistema.");
        }

        // Busca o gestor (empresa) ao qual o funcionário pertence
        GestorModel gestor = gestorRepository.findById(dto.getGestorId())
                .orElseThrow(() -> new RuntimeException("Gestor não encontrado com o ID: " + dto.getGestorId()));

        // Mapeia DTO para Entity
        FuncionarioModel funcionario = new FuncionarioModel();
        funcionario.setNome(dto.getNome());
        funcionario.setEmail(dto.getEmail());
        funcionario.setCargo(dto.getCargo());
        funcionario.setFotoUrl(dto.getFotoUrl());
        funcionario.setGestor(gestor);

        // Aplica o hash BCrypt na senha
        funcionario.setSenha(passwordEncoder.encode(dto.getSenha()));

        FuncionarioModel salvo = funcionarioRepository.save(funcionario);
        return new FuncionarioRespostaDTO(salvo);
    }

    // 2. Buscar por ID
    public FuncionarioRespostaDTO buscarPorId(Long id) {
        FuncionarioModel funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com o ID: " + id));
        return new FuncionarioRespostaDTO(funcionario);
    }

    // 3. Listar todos (ou apenas os de um gestor, se o gestorId for informado)
    public List<FuncionarioRespostaDTO> listarTodos(Long gestorId) {
        List<FuncionarioModel> funcionarios;

        if (gestorId != null) {
            if (!gestorRepository.existsById(gestorId)) {
                throw new RuntimeException("Gestor não encontrado com o ID: " + gestorId);
            }
            funcionarios = funcionarioRepository.findByGestorId(gestorId);
        } else {
            funcionarios = funcionarioRepository.findAll();
        }

        return funcionarios.stream()
                .map(FuncionarioRespostaDTO::new)
                .collect(Collectors.toList());
    }

    // 4. Atualizar Nome, E-mail, Cargo e Foto
    public FuncionarioRespostaDTO atualizar(Long id, FuncionarioAtualizacaoDTO dto) {
        FuncionarioModel funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com o ID: " + id));

        // Se o e-mail foi alterado, verifica se já pertence a outro usuário
        if (!funcionario.getEmail().equals(dto.getEmail()) && funcionarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Este novo e-mail já está sendo utilizado por outra pessoa.");
        }

        funcionario.setNome(dto.getNome());
        funcionario.setEmail(dto.getEmail());
        funcionario.setCargo(dto.getCargo());
        funcionario.setFotoUrl(dto.getFotoUrl());

        FuncionarioModel atualizado = funcionarioRepository.save(funcionario);
        return new FuncionarioRespostaDTO(atualizado);
    }

    // 5. Redefinir a senha (usado pelo gestor quando o funcionário pede recuperação de senha)
    public void redefinirSenha(Long id, FuncionarioSenhaDTO dto) {
        // Valida se a nova senha e a confirmação são iguais
        if (!dto.getNovaSenha().equals(dto.getConfirmacaoSenha())) {
            throw new IllegalArgumentException("A senha e a confirmação de senha não são iguais.");
        }

        FuncionarioModel funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com o ID: " + id));

        // Aplica o hash BCrypt na nova senha
        funcionario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
        funcionarioRepository.save(funcionario);
    }

    // 6. Deletar um funcionário específico por ID
    public void deletarPorId(Long id) {
        if (!funcionarioRepository.existsById(id)) {
            throw new RuntimeException("Funcionário não encontrado com o ID: " + id);
        }
        funcionarioRepository.deleteById(id);
    }
}
