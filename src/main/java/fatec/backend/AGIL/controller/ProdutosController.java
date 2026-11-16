package fatec.backend.AGIL.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fatec.backend.AGIL.model.Produtos;
import fatec.backend.AGIL.service.ProdutosService;

@RestController
@RequestMapping("/produtos")
public class ProdutosController {

    private final ProdutosService produtosService;

    public ProdutosController(ProdutosService produtosService) {
        this.produtosService = produtosService;
    }

    // GET em todos
    @GetMapping
    public List<Produtos> listarTodos() {
        return produtosService.listarTodos();
    }

    // GET por ID
    @GetMapping("/{id}")
    public Produtos buscar(@PathVariable Long id) {
        return produtosService.buscarPorId(id);
    }

    // POST
    @PostMapping
    public Produtos criar(@RequestBody Produtos produtos) {
        return produtosService.salvar(produtos);
    }

    // PATCH
    @PatchMapping("/{id}")
    public ResponseEntity<Produtos> atualizarCampo(
        @PathVariable Long id,
        @RequestBody Produtos dados) {
    return ResponseEntity.ok(
        produtosService.atualizarCampo(id, dados)
    );
    }

    // PUT 
    @PutMapping("/{id}")
    public Produtos atualizar(@PathVariable Long id, @RequestBody Produtos produtos) {
        return produtosService.atualizar(id, produtos);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        produtosService.excluir(id);
    }
}
