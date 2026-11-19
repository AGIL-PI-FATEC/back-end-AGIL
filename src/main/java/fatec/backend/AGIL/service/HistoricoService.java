package fatec.backend.AGIL.service;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import fatec.backend.AGIL.dto.HistoricoDTO;
import fatec.backend.AGIL.model.Historico;
import fatec.backend.AGIL.model.Produtos;
import fatec.backend.AGIL.model.GestorModel;
import fatec.backend.AGIL.repository.HistoricoRepository;

@Service
public class HistoricoService {

    private final HistoricoRepository historicoRepository;

    public HistoricoService(HistoricoRepository historicoRepository) {
        this.historicoRepository = historicoRepository;
    }

    // Registra uma movimentação
    public void registrar(
            Produtos produto,
            String tipo,
            Integer quantidadeCaixas,
            Integer qtdPorCaixa,
            GestorModel gestor) {

        if (quantidadeCaixas == null || quantidadeCaixas <= 0) {
            return;
        }

        int unidadesPorCaixa =
                qtdPorCaixa != null ? qtdPorCaixa : 1;

        int quantidadeUnidades =
                Math.multiplyExact(quantidadeCaixas, unidadesPorCaixa);

        Historico historico = new Historico();

        historico.setProdutoId(produto.getId());
        historico.setProduto(produto.getNome());
        historico.setTipo(tipo);
        historico.setQuantidade(quantidadeUnidades);
        historico.setUnidade("un.");
        historico.setData(LocalDateTime.now());
        historico.setGestor(gestor);

        historicoRepository.save(historico);
    }

    // Lista o histórico de um gestor
    public List<HistoricoDTO> listarPorGestor(Long gestorId) {

        return historicoRepository
                .findByGestor_IdOrderByDataDesc(gestorId)
                .stream()
                .map(historico -> new HistoricoDTO(
                        historico.getId(),
                        historico.getProduto(),
                        historico.getTipo(),
                        historico.getQuantidade(),
                        historico.getUnidade(),
                        historico.getData(),
                        historico.getGestor().getId()
                ))
                .toList();
    }
}
