package fatec.backend.AGIL.dto;

import fatec.backend.AGIL.model.FuncionarioModel;
import lombok.Data;

@Data
public class FuncionarioRespostaDTO {

    private Long id;
    private String nome;
    private String email;
    private String cargo;
    private String foto;
    private Long gestorId;
    private String nomeEmpresa;
    // a senha não é enviada para o front

    public FuncionarioRespostaDTO(FuncionarioModel funcionario) {
        this.id = funcionario.getId();
        this.nome = funcionario.getNome();
        this.email = funcionario.getEmail();
        this.cargo = funcionario.getCargo();
        this.foto = funcionario.getFoto();
        this.gestorId = funcionario.getGestor().getId();
        this.nomeEmpresa = funcionario.getGestor().getNomeEmpresa();
    }
}
