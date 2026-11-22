
package fatec.backend.AGIL.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import fatec.backend.AGIL.dto.ProdutosDTO;
import fatec.backend.AGIL.model.Produtos;
import fatec.backend.AGIL.repository.ProdutosRepository;

@Service
public class ProdutosService {

    private final ProdutosRepository produtosRepository;

    public ProdutosService(ProdutosRepository produtosRepository) {
        this.produtosRepository = produtosRepository;
    }

    // Entidade para DTO
    private ProdutosDTO converterDTO(Produtos produto) {
        return new ProdutosDTO(
            produto.getId(),
            produto.getNome(),
            produto.getPreco()
        );
    }

    // DTO para entidade
    private Produtos converterEntidade(ProdutosDTO dto) {
        Produtos produto = new Produtos();

        produto.setId(dto.id());
        produto.setNome(dto.nome());
        produto.setPreco(dto.preco());

        return produto;
    }

    // GET - listar todos
    public List<ProdutosDTO> listarTodos() {
        return produtosRepository.findAll()
                .stream()
                .map(this::converterDTO)
                .toList();
    }

    // POST - salvar
    public ProdutosDTO salvar(ProdutosDTO dto) {
        Produtos produto = converterEntidade(dto);
        produto.setId(null);

        Produtos salvo = produtosRepository.save(produto);

        return converterDTO(salvo);
    }

    // GET - buscar por ID
    public ProdutosDTO buscarPorId(Long id) {
        Produtos produto = produtosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado: id " + id));

        return converterDTO(produto);
    }

    // PATCH - atualizar campos 
    public ProdutosDTO atualizarCampo(Long id, ProdutosDTO dados) {
        Produtos produto = produtosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado: id " + id));

        // No PATCH os campos são opcionais, mas se vierem precisam ser válidos
        if (dados.nome() != null) {
            if (dados.nome().isBlank()) {
                throw new IllegalArgumentException("O nome do produto não pode ser vazio.");
            }
            produto.setNome(dados.nome());
        }

        if (dados.preco() != null) {
            if (dados.preco().signum() <= 0) {
                throw new IllegalArgumentException("O preço deve ser maior que zero.");
            }
            produto.setPreco(dados.preco());
        }

        Produtos atualizado = produtosRepository.save(produto);

        return converterDTO(atualizado);
    }

    // PUT - atualizar geral
    public ProdutosDTO atualizar(Long id, ProdutosDTO novo) {
        Produtos existente = produtosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado: id " + id));

        existente.setNome(novo.nome());
        existente.setPreco(novo.preco());

        Produtos atualizado = produtosRepository.save(existente);

        return converterDTO(atualizado);
    }

    // DELETE 
    public void excluir(Long id) {
        if (!produtosRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Produto não encontrado: id " + id);
        }

        produtosRepository.deleteById(id);
    }
}


