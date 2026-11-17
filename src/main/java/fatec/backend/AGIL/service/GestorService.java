package fatec.backend.AGIL.service;

import fatec.backend.AGIL.dto.GestorAtualizacaoDTO;
import fatec.backend.AGIL.dto.GestorCadastroDTO;
import fatec.backend.AGIL.dto.GestorRespostaDTO;
import fatec.backend.AGIL.dto.LoginDTO;
import fatec.backend.AGIL.model.GestorModel;
import fatec.backend.AGIL.repository.GestorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GestorService {

    @Autowired
    private GestorRepository gestorRepository;

    // Instancia o encriptador BCrypt para a senha
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 1. Cadastrar Gestor
    public GestorRespostaDTO cadastrar(GestorCadastroDTO dto) {
        // Valida se a senha e a confirmação são iguais
        if (!dto.getSenha().equals(dto.getConfirmacaoSenha())) {
            throw new IllegalArgumentException("A senha e a confirmação de senha não são iguais.");
        }

        // Valida se o email já existe
        if (gestorRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Este email já existe no sistema.");
        }

        // Valida se o CNPJ já existe
        if (gestorRepository.existsByCnpj(dto.getCnpj())) {
            throw new RuntimeException("Este CNPJ já existe no sistema.");
        }

        // Mapeia DTO para Entity
        GestorModel gestor = new GestorModel();
        gestor.setNome(dto.getNome());
        gestor.setEmail(dto.getEmail());
        gestor.setCnpj(dto.getCnpj());
        gestor.setRazaoSocial(dto.getRazaoSocial());
        gestor.setNomeEmpresa(dto.getNomeEmpresa());

        // Aplica o hash BCrypt na senha
        gestor.setSenha(passwordEncoder.encode(dto.getSenha()));

        GestorModel salvo = gestorRepository.save(gestor);
        return new GestorRespostaDTO(salvo);
    }

    // 2. Buscar por ID
    public GestorRespostaDTO buscarPorId(Long id) {
        GestorModel gestor = gestorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gestor não encontrado com o ID: " + id));
        return new GestorRespostaDTO(gestor);
    }

    // 3. Buscando todos de uma vez
    public List<GestorRespostaDTO> listarTodos() {
        return gestorRepository.findAll()
                .stream()
                .map(GestorRespostaDTO::new)
                .collect(Collectors.toList());
    }

    // 4. Atualizar Nome e E-mail
    public GestorRespostaDTO atualizar(Long id, GestorAtualizacaoDTO dto) {
        GestorModel gestor = gestorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gestor não encontrado com o ID: " + id));

        // Se o e-mail foi alterado, verifica se já pertence a outro usuário
        if (!gestor.getEmail().equals(dto.getEmail()) && gestorRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Este novo e-mail já está sendo utilizado por outra pessoa.");
        }

        gestor.setNome(dto.getNome());
        gestor.setEmail(dto.getEmail());

        GestorModel atualizado = gestorRepository.save(gestor);
        return new GestorRespostaDTO(atualizado);
    }

    // 5. Deletar um gestor específico por ID
    public void deletarPorId(Long id) {
        if (!gestorRepository.existsById(id)) {
            throw new RuntimeException("Gestor não encontrado com o ID: " + id);
        }
        gestorRepository.deleteById(id);
    }

    // 6. Deletar todos os gestores cadastrados
    public void deletarTodos() {
        gestorRepository.deleteAll();
    }

    // 7. Login
    public GestorRespostaDTO login(LoginDTO dto) {
        GestorModel gestor = gestorRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        if (!passwordEncoder.matches(dto.getSenha(), gestor.getSenha())) {
            throw new RuntimeException("Credenciais inválidas");
        }
        return new GestorRespostaDTO(gestor);
    }
}
