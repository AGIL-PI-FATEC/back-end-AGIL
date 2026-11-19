package fatec.backend.AGIL.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import fatec.backend.AGIL.model.Notificacao;

public interface NotificacaoRepository
        extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByGestor_IdAndAtivaTrueOrderByDataDesc(
        Long gestorId
    );

    Optional<Notificacao> findFirstByProdutoIdAndGestor_IdAndAtivaTrue(
        Long produtoId,
        Long gestorId
    );
}
