package fatec.backend.AGIL.controller;

import fatec.backend.AGIL.dto.GestorAtualizacaoDTO;
import fatec.backend.AGIL.dto.GestorCadastroDTO;
import fatec.backend.AGIL.dto.GestorRespostaDTO;
import fatec.backend.AGIL.service.GestorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gestores")
@CrossOrigin(origins = "*")
public class GestorController {

    @Autowired
    private GestorService gestorService;

    // 1. Cadastrar Gestor (POST /gestores)
    @PostMapping
    public ResponseEntity<GestorRespostaDTO> cadastrar(@RequestBody @Valid GestorCadastroDTO dto) {
        GestorRespostaDTO novoGestor = gestorService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoGestor);
    }

    // 2. Buscar por ID (GET /gestores/{id})
    @GetMapping("/{id}")
    public ResponseEntity<GestorRespostaDTO> buscarPorId(@PathVariable Long id) {
        GestorRespostaDTO gestor = gestorService.buscarPorId(id);
        return ResponseEntity.ok(gestor);
    }

    // 3. Listar todos os gestores (GET /gestores)
    @GetMapping
    public ResponseEntity<List<GestorRespostaDTO>> listarTodos() {
        List<GestorRespostaDTO> gestores = gestorService.listarTodos();
        return ResponseEntity.ok(gestores);
    }

    // 4. Atualizar Nome e E-mail (PUT /gestores/{id})
    @PutMapping("/{id}")
    public ResponseEntity<GestorRespostaDTO> atualizar(@PathVariable Long id, @RequestBody @Valid GestorAtualizacaoDTO dto) {
        GestorRespostaDTO gestorAtualizado = gestorService.atualizar(id, dto);
        return ResponseEntity.ok(gestorAtualizado);
    }

    // 5. Deletar gestor por ID (DELETE /gestores/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<List<GestorRespostaDTO>> deletarPorId(@PathVariable Long id) {
        gestorService.deletarPorId(id);
        return listarTodos();
    }

    // 6. Deletar todos os gestores (DELETE /gestores)
    @DeleteMapping
    public ResponseEntity<List<GestorRespostaDTO>> deletarTodos() {
        gestorService.deletarTodos();
        return listarTodos();
    }
}