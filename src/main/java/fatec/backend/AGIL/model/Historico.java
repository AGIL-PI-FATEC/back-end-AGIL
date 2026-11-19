package fatec.backend.AGIL.model;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "historico")
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long produtoId;
    private String produto;
    private String tipo;
    private Integer quantidade;
    private String unidade;
    private LocalDateTime data;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_gestor", nullable = false)
    private GestorModel gestor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public GestorModel getGestor() {
        return gestor;
    }

    public void setGestor(GestorModel gestor) {
        this.gestor = gestor;
    }
}
