package fatec.backend.AGIL.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import fatec.backend.AGIL.model.Produtos;
import fatec.backend.AGIL.repository.ProdutosRepository;

@Service
public class ProdutosService {

    private final ProdutosRepository produtosRepository;

    public ProdutosService(ProdutosRepository produtosRepository) {
        this.produtosRepository = produtosRepository;
    }

    public List<Produtos> listarTodos() {
        return produtosRepository.findAll();
    }

    public Produtos salvar(Produtos produtos) {
        produtos.setId(null);
        return produtosRepository.save(produtos); 
    }

    public Produtos buscarPorId(Long id) {
        return produtosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produto não encontrado: id " + id));
    }

    public Produtos atualizarCampo(Long id, Produtos dados) {
    Produtos produto = produtosRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

    if (dados.getNome() != null) {
        produto.setNome(dados.getNome());
    }

    if (dados.getPreco() != null) {
        produto.setPreco(dados.getPreco());
    }

    return produtosRepository.save(produto); }


    public Produtos atualizar(Long id, Produtos novo) {
        Produtos existente = buscarPorId(id);

        existente.setNome(novo.getNome());
        existente.setPreco(novo.getPreco());

        return produtosRepository.save(existente);
    }

    public void excluir(Long id) {
        if (!produtosRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Produto não encontrado: id " + id);
        }
        produtosRepository.deleteById(id); 
    }
}

