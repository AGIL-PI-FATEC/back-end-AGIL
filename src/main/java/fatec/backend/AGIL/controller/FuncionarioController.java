package fatec.backend.AGIL.controller;

import fatec.backend.AGIL.dto.FuncionarioAtualizacaoDTO;
import fatec.backend.AGIL.dto.FuncionarioCadastroDTO;
import fatec.backend.AGIL.dto.FuncionarioRespostaDTO;
import fatec.backend.AGIL.dto.FuncionarioSenhaDTO;
import fatec.backend.AGIL.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    // 1. Cadastrar Funcionário (POST /funcionarios)
    @PostMapping
    public ResponseEntity<FuncionarioRespostaDTO> cadastrar(@RequestBody @Valid FuncionarioCadastroDTO dto) {
        FuncionarioRespostaDTO novoFuncionario = funcionarioService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFuncionario);
    }

    // 2. Buscar por ID (GET /funcionarios/{id})
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioRespostaDTO> buscarPorId(@PathVariable Long id) {
        FuncionarioRespostaDTO funcionario = funcionarioService.buscarPorId(id);
        return ResponseEntity.ok(funcionario);
    }

    // 3. Listar funcionários (GET /funcionarios ou GET /funcionarios?gestorId=1)
    @GetMapping
    public ResponseEntity<List<FuncionarioRespostaDTO>> listarTodos(@RequestParam(required = false) Long gestorId) {
        List<FuncionarioRespostaDTO> funcionarios = funcionarioService.listarTodos(gestorId);
        return ResponseEntity.ok(funcionarios);
    }

    // 4. Atualizar Nome, E-mail, Cargo e Foto (PUT /funcionarios/{id})
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioRespostaDTO> atualizar(@PathVariable Long id, @RequestBody @Valid FuncionarioAtualizacaoDTO dto) {
        FuncionarioRespostaDTO funcionarioAtualizado = funcionarioService.atualizar(id, dto);
        return ResponseEntity.ok(funcionarioAtualizado);
    }

    // 5. Redefinir senha (PUT /funcionarios/{id}/senha)
    @PutMapping("/{id}/senha")
    public ResponseEntity<Void> redefinirSenha(@PathVariable Long id, @RequestBody @Valid FuncionarioSenhaDTO dto) {
        funcionarioService.redefinirSenha(id, dto);
        return ResponseEntity.noContent().build();
    }

    // 6. Deletar funcionário por ID (DELETE /funcionarios/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {
        funcionarioService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
