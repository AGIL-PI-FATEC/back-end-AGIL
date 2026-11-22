package fatec.backend.AGIL.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GestorCadastroDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 80, message = "O campo nome deve ter no máximo 80 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Size(max = 80, message = "O campo email deve ter no máximo 80 caracteres")
    private String email;

    @NotBlank(message = "O CNPJ é obrigatório")
    @Size(max = 18, message = "O campo cnpj deve ter no máximo 18 caracteres")
    private String cnpj;

    @NotBlank(message = "A razão social é obrigatória")
    @Size(max = 80, message = "O campo razaoSocial deve ter no máximo 80 caracteres")
    private String razaoSocial;

    @Size(max = 80, message = "O campo nomeEmpresa deve ter no máximo 80 caracteres")
    private String nomeEmpresa;

    @NotBlank(message = "Obrigatório informar a senha")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String senha;

    @NotBlank(message = "A confirmação de senha é obrigatória")
    private String confirmacaoSenha;
}