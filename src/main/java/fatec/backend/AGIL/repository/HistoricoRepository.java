package fatec.backend.AGIL.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import fatec.backend.AGIL.model.Historico;

public interface HistoricoRepository
        extends JpaRepository<Historico, Long> {

    List<Historico> findByGestor_IdOrderByDataDesc(Long gestorId);
}
