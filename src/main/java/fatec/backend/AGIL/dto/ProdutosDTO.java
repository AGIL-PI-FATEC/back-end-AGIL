package fatec.backend.AGIL.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProdutosDTO(
    Long id,

    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(max = 255, message = "O nome do produto deve ter no máximo 255 caracteres")
    String nome,

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser maior que zero")
    BigDecimal preco
) {
}
