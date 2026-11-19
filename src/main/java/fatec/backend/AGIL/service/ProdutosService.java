package fatec.backend.AGIL.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import fatec.backend.AGIL.dto.ProdutosDTO;
import fatec.backend.AGIL.model.GestorModel;
import fatec.backend.AGIL.model.Produtos;
import fatec.backend.AGIL.repository.GestorRepository;
import fatec.backend.AGIL.repository.ProdutosRepository;

@Service
public class ProdutosService {

    private final ProdutosRepository produtosRepository;
    private final GestorRepository gestorRepository;
    private final HistoricoService historicoService;
    private final NotificacaoService notificacaoService;

    public ProdutosService(
            ProdutosRepository produtosRepository,
            GestorRepository gestorRepository,
            HistoricoService historicoService,
            NotificacaoService notificacaoService) {

        this.produtosRepository = produtosRepository;
        this.gestorRepository = gestorRepository;
        this.historicoService = historicoService;
        this.notificacaoService = notificacaoService;
    }

    // Entidade para DTO
    private ProdutosDTO converterDTO(Produtos produto) {

        return new ProdutosDTO(
            produto.getId(),
            produto.getNome(),
            produto.getPreco(),
            produto.getEstoqueCaixas(),
            produto.getQtdPorCaixa(),
            produto.getGestor().getId()
        );
    }

    // GET - listar todos ou por gestor
    public List<ProdutosDTO> listarTodos(Long gestorId) {

        List<Produtos> produtos;

        if (gestorId != null) {
            produtos = produtosRepository.findByGestor_Id(gestorId);
        } else {
            produtos = produtosRepository.findAll();
        }

        return produtos.stream()
                .map(this::converterDTO)
                .toList();
    }

    // POST - salvar
    @Transactional
    public ProdutosDTO salvar(ProdutosDTO dto) {

        if (dto.gestorId() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O gestor do produto é obrigatório."
            );
        }

        GestorModel gestor = gestorRepository.findById(dto.gestorId())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Gestor não encontrado."
                ));

        validarEstoque(dto.estoqueCaixas(), dto.qtdPorCaixa());

        Produtos produto = new Produtos();

        produto.setNome(dto.nome());
        produto.setPreco(dto.preco());
        produto.setEstoqueCaixas(dto.estoqueCaixas());
        produto.setQtdPorCaixa(dto.qtdPorCaixa());
        produto.setGestor(gestor);

        Produtos salvo = produtosRepository.save(produto);

        // Registra a entrada inicial
        if (salvo.getEstoqueCaixas() != null
                && salvo.getEstoqueCaixas() > 0) {

            historicoService.registrar(
                salvo,
                "Entrada",
                salvo.getEstoqueCaixas(),
                salvo.getQtdPorCaixa(),
                gestor
            );
        }

        // Verifica se precisa gerar uma notificação
        notificacaoService.verificarEstoque(salvo);

        return converterDTO(salvo);
    }

    // GET - buscar por ID
    public ProdutosDTO buscarPorId(Long id) {

        Produtos produto = produtosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Produto não encontrado: id " + id
                ));

        return converterDTO(produto);
    }

    // PATCH - atualizar campos
    @Transactional
    public ProdutosDTO atualizarCampo(Long id, ProdutosDTO dados) {

        Produtos produto = produtosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Produto não encontrado: id " + id
                ));

        Integer estoqueAnterior = produto.getEstoqueCaixas();
        Integer qtdPorCaixaAnterior = produto.getQtdPorCaixa();

        if (dados.nome() != null) {
            produto.setNome(dados.nome());
        }

        if (dados.preco() != null) {
            produto.setPreco(dados.preco());
        }

        if (dados.estoqueCaixas() != null) {
            produto.setEstoqueCaixas(dados.estoqueCaixas());
        }

        if (dados.qtdPorCaixa() != null) {
            produto.setQtdPorCaixa(dados.qtdPorCaixa());
        }

        validarEstoque(
            produto.getEstoqueCaixas(),
            produto.getQtdPorCaixa()
        );

        Produtos atualizado = produtosRepository.save(produto);

        // Registra a entrada ou saída no histórico
        registrarDiferenca(
            atualizado,
            estoqueAnterior,
            qtdPorCaixaAnterior
        );

        // Verifica o estoque e atualiza as notificações
        notificacaoService.verificarEstoque(atualizado);

        return converterDTO(atualizado);
    }

    // PUT - atualizar geral
    @Transactional
    public ProdutosDTO atualizar(Long id, ProdutosDTO novo) {

        Produtos existente = produtosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Produto não encontrado: id " + id
                ));

        Integer estoqueAnterior = existente.getEstoqueCaixas();
        Integer qtdPorCaixaAnterior = existente.getQtdPorCaixa();

        existente.setNome(novo.nome());
        existente.setPreco(novo.preco());
        existente.setEstoqueCaixas(novo.estoqueCaixas());
        existente.setQtdPorCaixa(novo.qtdPorCaixa());

        validarEstoque(
            existente.getEstoqueCaixas(),
            existente.getQtdPorCaixa()
        );

        Produtos atualizado = produtosRepository.save(existente);

        // Registra a entrada ou saída no histórico
        registrarDiferenca(
            atualizado,
            estoqueAnterior,
            qtdPorCaixaAnterior
        );

        // Verifica o estoque e atualiza as notificações
        notificacaoService.verificarEstoque(atualizado);

        return converterDTO(atualizado);
    }

    // Registra entrada ou saída conforme a diferença do estoque
    private void registrarDiferenca(
            Produtos produto,
            Integer estoqueAnterior,
            Integer qtdPorCaixaAnterior) {

        int anterior = estoqueAnterior != null ? estoqueAnterior : 0;

        int atual = produto.getEstoqueCaixas() != null
                ? produto.getEstoqueCaixas()
                : 0;

        int diferenca = atual - anterior;

        if (diferenca == 0) {
            return;
        }

        String tipo = diferenca > 0 ? "Entrada" : "Saída";

        int quantidadeCaixas = Math.abs(diferenca);

        historicoService.registrar(
            produto,
            tipo,
            quantidadeCaixas,
            qtdPorCaixaAnterior,
            produto.getGestor()
        );
    }

    // Valida os dados do estoque
    private void validarEstoque(
            Integer estoqueCaixas,
            Integer qtdPorCaixa) {

        if (estoqueCaixas != null && estoqueCaixas < 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O estoque não pode ser negativo."
            );
        }

        if (qtdPorCaixa != null && qtdPorCaixa <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "A quantidade por caixa deve ser maior que zero."
            );
        }
    }

    // DELETE
    public void excluir(Long id) {

        if (!produtosRepository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Produto não encontrado: id " + id
            );
        }

        // O histórico permanece salvo após a exclusão.
        produtosRepository.deleteById(id);
    }
}