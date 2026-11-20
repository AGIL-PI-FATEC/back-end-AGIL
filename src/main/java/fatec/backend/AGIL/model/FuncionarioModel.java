package fatec.backend.AGIL.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "funcionario")

public class FuncionarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_funcionario")
    private Long id;

    @Column(name = "nome_funcionario", nullable = false, length = 80)
    private String nome;

    @Column(name = "email_funcionario", nullable = false, unique = true, length = 80)
    private String email;

    @Column(name = "senha_funcionario", nullable = false, length = 60)
    private String senha;

    @Column(name = "cargo_funcionario", nullable = false, length = 60)
    private String cargo;

    @Column(name = "foto", columnDefinition = "LONGTEXT")
    private String foto;

    // Cada funcionário pertence à empresa de um gestor
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_gestor", nullable = false)
    private GestorModel gestor;
}
