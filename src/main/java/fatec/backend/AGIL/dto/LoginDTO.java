package fatec.backend.AGIL.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class LoginDTO {

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "A senha é obriagtória")
    private String senha;
}
