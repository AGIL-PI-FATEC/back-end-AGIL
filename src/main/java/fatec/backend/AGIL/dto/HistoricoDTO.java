package fatec.backend.AGIL.dto;
import java.time.LocalDateTime;

public record HistoricoDTO(
    Long id,
    String produto,
    String tipo,
    Integer quantidade,
    String unidade,
    LocalDateTime data,
    Long gestorId
) {
}
