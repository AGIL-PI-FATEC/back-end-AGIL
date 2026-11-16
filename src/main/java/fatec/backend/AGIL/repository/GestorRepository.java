package fatec.backend.AGIL.repository;

import fatec.backend.AGIL.model.GestorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface GestorRepository extends JpaRepository<GestorModel, Long> {

    boolean existsByEmail(String email);

    boolean existsByCnpj(String cnpj);

    Optional<GestorModel> findByEmail(String email);
}
