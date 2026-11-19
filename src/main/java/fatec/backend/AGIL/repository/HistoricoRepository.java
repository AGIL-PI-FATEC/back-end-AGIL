package fatec.backend.AGIL.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import fatec.backend.AGIL.model.Historico;

public interface HistoricoRepository
        extends JpaRepository<Historico, Long> {

    List<Historico> findByGestor_IdOrderByDataDesc(
        Long gestorId
    );

    Optional<Historico>
    findFirstByProdutoIdAndGestor_IdAndTipoOrderByDataDesc(
        Long produtoId,
        Long gestorId,
        String tipo
    );
}
