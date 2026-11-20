package fatec.backend.AGIL.dto;

import fatec.backend.AGIL.model.GestorModel;
import lombok.Data;

@Data
public class GestorRespostaDTO {

    private Long id;
    private String nome;
    private String email;
    private String cnpj;
    private String razaoSocial;
    private String nomeEmpresa;
    private String foto;
    //private String senha;            //aqui eu comentei pq não vamos mandar isso para o front, mas para testes, descomente

    public GestorRespostaDTO(GestorModel gestor) {
        this.id = gestor.getId();
        this.nome = gestor.getNome();
        this.email = gestor.getEmail();
        this.cnpj = gestor.getCnpj();
        this.razaoSocial = gestor.getRazaoSocial();
        this.nomeEmpresa = gestor.getNomeEmpresa();
        this.foto = gestor.getFoto();
        //this.senha = gestor.getSenha();            //aqui eu comentei pq não vamos mandar isso para o front, mas para testes, descomente
    }
}