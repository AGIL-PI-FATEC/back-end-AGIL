
package fatec.backend.AGIL.dto;

import java.time.LocalDateTime;

public record NotificacaoDTO(
    Long id,
    Long produtoId,
    String titulo,
    String mensagem,
    String tipo,
    LocalDateTime data,
    Boolean lida
) {
}
