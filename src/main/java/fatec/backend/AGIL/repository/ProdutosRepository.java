package fatec.backend.AGIL.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import fatec.backend.AGIL.model.Produtos;

public interface ProdutosRepository extends JpaRepository<Produtos, Long> {

    List<Produtos> findByGestor_Id(Long gestorId);

}
