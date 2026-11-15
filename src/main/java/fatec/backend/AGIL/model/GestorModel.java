package fatec.backend.AGIL.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "gestor_empresa")

public class GestorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gestor")
    private Long id;

    @Column(name = "nome_gestor", nullable = false, length = 80)
    private String nome;

    @Column(name = "email_gestor", nullable = false, unique = true, length = 80)
    private String email;

    @Column(name = "senha_gestor", nullable = false, length = 60)
    private String senha;

    @Column(name = "cnpj_empresa", nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(name = "razao_social", nullable = false, length = 80)
    private String razaoSocial;

    @Column(name = "nome_empresa", length = 80)
    private String nomeEmpresa;
}