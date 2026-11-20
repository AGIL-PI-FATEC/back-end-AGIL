
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

    // GET - listar produtos de um gestor
    @GetMapping
    public List<ProdutosDTO> listarTodos(
            @RequestParam Long gestorId) {

        return produtosService.listarTodos(gestorId);
    }

    // GET - buscar produto por ID, validando o gestor
    @GetMapping("/{id}")
    public ProdutosDTO buscar(
            @PathVariable Long id,
            @RequestParam Long gestorId) {

        return produtosService.buscarPorId(id, gestorId);
    }

    // POST - cadastrar produto
    @PostMapping
    public ResponseEntity<ProdutosDTO> criar(
            @RequestBody ProdutosDTO produtos) {

        ProdutosDTO produtoSalvo = produtosService.salvar(produtos);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(produtoSalvo);
    }

    // PATCH - atualizar campos, validando o gestor
    @PatchMapping("/{id}")
    public ResponseEntity<ProdutosDTO> atualizarCampo(
            @PathVariable Long id,
            @RequestParam Long gestorId,
            @RequestBody ProdutosDTO dados) {

        return ResponseEntity.ok(
                produtosService.atualizarCampo(id, gestorId, dados)
        );
    }

    // PUT - atualizar produto completo, validando o gestor
    @PutMapping("/{id}")
    public ProdutosDTO atualizar(
            @PathVariable Long id,
            @RequestParam Long gestorId,
            @RequestBody ProdutosDTO produtos) {

        return produtosService.atualizar(id, gestorId, produtos);
    }

    // DELETE - excluir produto, validando o gestor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @RequestParam Long gestorId) {

        produtosService.excluir(id, gestorId);

        return ResponseEntity.noContent().build();
    }
}