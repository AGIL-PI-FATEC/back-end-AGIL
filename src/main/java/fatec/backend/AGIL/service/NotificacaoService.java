package fatec.backend.AGIL.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import fatec.backend.AGIL.dto.NotificacaoDTO;
import fatec.backend.AGIL.model.Historico;
import fatec.backend.AGIL.model.Notificacao;
import fatec.backend.AGIL.model.Produtos;
import fatec.backend.AGIL.repository.HistoricoRepository;
import fatec.backend.AGIL.repository.NotificacaoRepository;

@Service
public class NotificacaoService {

    private static final double PERCENTUAL_ESTOQUE_BAIXO = 0.15;

    private final NotificacaoRepository notificacaoRepository;
    private final HistoricoRepository historicoRepository;

    public NotificacaoService(
            NotificacaoRepository notificacaoRepository,
            HistoricoRepository historicoRepository) {

        this.notificacaoRepository = notificacaoRepository;
        this.historicoRepository = historicoRepository;
    }

    // Verifica o estoque e gera a notificação necessária
    @Transactional
    public void verificarEstoque(Produtos produto) {

        int caixas = produto.getEstoqueCaixas() != null
                ? produto.getEstoqueCaixas()
                : 0;

        int unidadesPorCaixa = produto.getQtdPorCaixa() != null
                ? produto.getQtdPorCaixa()
                : 1;

        // Converte o estoque atual para unidades
        int estoqueUnidades = caixas * unidadesPorCaixa;

        String tipo = null;
        String titulo = null;
        String mensagem = null;

        // Estoque zerado
        if (estoqueUnidades == 0) {

            tipo = "ESTOQUE_ZERADO";
            titulo = "Estoque zerado";

            mensagem = "O produto " + produto.getNome()
                    + " está sem estoque.";

        } else {

            // Busca a última entrada do produto
            Optional<Historico> ultimaEntrada =
                    historicoRepository
                    .findFirstByProdutoIdAndGestor_IdAndTipoOrderByDataDesc(
                        produto.getId(),
                        produto.getGestor().getId(),
                        "Entrada"
                    );

            if (ultimaEntrada.isPresent()) {

                // A quantidade do histórico já está em unidades
                int quantidadeEntrada =
                        ultimaEntrada.get().getQuantidade();

                // Calcula 15% da última entrada
                int limite = Math.max(
                    1,
                    (int) Math.ceil(
                        quantidadeEntrada * PERCENTUAL_ESTOQUE_BAIXO
                    )
                );

                if (estoqueUnidades <= limite) {

                    tipo = "ESTOQUE_BAIXO";
                    titulo = "Estoque baixo";

                    mensagem = "O produto " + produto.getNome()
                            + " possui apenas " + estoqueUnidades
                            + " unidades disponíveis. "
                            + "O limite de alerta é "
                            + limite + " unidades.";
                }
            }
        }

        // Busca uma notificação ativa do produto
        Optional<Notificacao> existente =
                notificacaoRepository
                .findFirstByProdutoIdAndGestor_IdAndAtivaTrue(
                    produto.getId(),
                    produto.getGestor().getId()
                );

        // Se o estoque voltou ao normal, encerra o alerta
        if (tipo == null) {

            existente.ifPresent(notificacao -> {
                notificacao.setAtiva(false);
                notificacaoRepository.save(notificacao);
            });

            return;
        }

        // Se já existe uma notificação
        if (existente.isPresent()) {

            Notificacao notificacao = existente.get();

            // Não cria outra notificação do mesmo tipo
            if (notificacao.getTipo().equals(tipo)) {

                notificacao.setMensagem(mensagem);

                notificacaoRepository.save(notificacao);

                return;
            }

            // Encerra o alerta anterior caso o tipo tenha mudado
            notificacao.setAtiva(false);

            notificacaoRepository.save(notificacao);
        }

        // Cria uma nova notificação
        Notificacao notificacao = new Notificacao();

        notificacao.setProdutoId(produto.getId());
        notificacao.setTitulo(titulo);
        notificacao.setMensagem(mensagem);
        notificacao.setTipo(tipo);
        notificacao.setData(LocalDateTime.now());
        notificacao.setLida(false);
        notificacao.setAtiva(true);
        notificacao.setGestor(produto.getGestor());

        notificacaoRepository.save(notificacao);
    }

    // Lista as notificações ativas do gestor
    public List<NotificacaoDTO> listarPorGestor(Long gestorId) {

        return notificacaoRepository
                .findByGestor_IdAndAtivaTrueOrderByDataDesc(gestorId)
                .stream()
                .map(notificacao -> new NotificacaoDTO(
                    notificacao.getId(),
                    notificacao.getProdutoId(),
                    notificacao.getTitulo(),
                    notificacao.getMensagem(),
                    notificacao.getTipo(),
                    notificacao.getData(),
                    notificacao.getLida()
                ))
                .toList();
    }

    // Marca uma notificação como lida
    @Transactional
    public void marcarComoLida(Long id) {

        Notificacao notificacao =
                notificacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Notificação não encontrada."
                ));

        notificacao.setLida(true);

        notificacaoRepository.save(notificacao);
    }
}
