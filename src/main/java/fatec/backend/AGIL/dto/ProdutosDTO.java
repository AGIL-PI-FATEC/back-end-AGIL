package fatec.backend.AGIL.dto;

import java.math.BigDecimal;

public record ProdutosDTO(
    Long id,
    String nome,
    BigDecimal preco
) {
}
