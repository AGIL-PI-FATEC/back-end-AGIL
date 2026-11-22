package fatec.backend.AGIL.repository;

import fatec.backend.AGIL.model.FuncionarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface FuncionarioRepository extends JpaRepository<FuncionarioModel, Long> {

    boolean existsByEmail(String email);

    Optional<FuncionarioModel> findByEmail(String email);

    List<FuncionarioModel> findByGestorId(Long gestorId);

    boolean existsByGestorId(Long gestorId);
}
