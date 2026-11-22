
package fatec.backend.AGIL.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fatec.backend.AGIL.dto.ProdutosDTO;
import fatec.backend.AGIL.service.ProdutosService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutosController {

    private final ProdutosService produtosService;

    public ProdutosController(ProdutosService produtosService) {
        this.produtosService = produtosService;
    }

    // GET em todos
    @GetMapping
    public List<ProdutosDTO> listarTodos() {
        return produtosService.listarTodos();
    }

    // GET por ID
    @GetMapping("/{id}")
    public ProdutosDTO buscar(@PathVariable Long id) {
        return produtosService.buscarPorId(id);
    }

    // POST
    @PostMapping
    public ResponseEntity<ProdutosDTO> criar(@RequestBody @Valid ProdutosDTO produtos) {
        ProdutosDTO produtoSalvo = produtosService.salvar(produtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
    }

    // PATCH
    @PatchMapping("/{id}")
    public ResponseEntity<ProdutosDTO> atualizarCampo(
        @PathVariable Long id,
        @RequestBody ProdutosDTO dados) {

        return ResponseEntity.ok(
            produtosService.atualizarCampo(id, dados)
        );
    }

    // PUT 
    @PutMapping("/{id}")
    public ProdutosDTO atualizar(@PathVariable Long id, @RequestBody @Valid ProdutosDTO produtos) {
        return produtosService.atualizar(id, produtos);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtosService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
