package fatec.backend.AGIL.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FuncionarioSenhaDTO {

    @NotBlank(message = "Obrigatório informar a nova senha")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String novaSenha;

    @NotBlank(message = "A confirmação de senha é obrigatória")
    private String confirmacaoSenha;
}
