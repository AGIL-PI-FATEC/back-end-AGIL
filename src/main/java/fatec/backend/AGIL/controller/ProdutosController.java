
package fatec.backend.AGIL.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import fatec.backend.AGIL.dto.ProdutosDTO;
import fatec.backend.AGIL.service.ProdutosService;

@RestController
@RequestMapping("/produtos")
public class ProdutosController {

    private final ProdutosService produtosService;

    public ProdutosController(ProdutosService produtosService) {
        this.produtosService = produtosService;
    }

    // GET - listar produtos
    @GetMapping
    public List<ProdutosDTO> listarTodos(
            @RequestParam(required = false) Long gestorId) {

        return produtosService.listarTodos(gestorId);
    }

    // GET - buscar por ID
    @GetMapping("/{id}")
    public ProdutosDTO buscar(@PathVariable Long id) {
        return produtosService.buscarPorId(id);
    }

    // POST - cadastrar
    @PostMapping
    public ResponseEntity<ProdutosDTO> criar(
            @RequestBody ProdutosDTO produtos) {

        ProdutosDTO produtoSalvo = produtosService.salvar(produtos);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(produtoSalvo);
    }

    // PATCH - atualizar campos
    @PatchMapping("/{id}")
    public ResponseEntity<ProdutosDTO> atualizarCampo(
            @PathVariable Long id,
            @RequestBody ProdutosDTO dados) {

        return ResponseEntity.ok(
            produtosService.atualizarCampo(id, dados)
        );
    }

    // PUT - atualizar geral
    @PutMapping("/{id}")
    public ProdutosDTO atualizar(
            @PathVariable Long id,
            @RequestBody ProdutosDTO produtos) {

        return produtosService.atualizar(id, produtos);
    }

    // DELETE - excluir
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        produtosService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}