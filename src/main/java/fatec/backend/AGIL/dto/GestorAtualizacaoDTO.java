package fatec.backend.AGIL.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GestorAtualizacaoDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 80, message = "O campo nome deve ter no máximo 80 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Size(max = 80, message = "O campo email deve ter no máximo 80 caracteres")
    private String email;
    private String foto;
    
}