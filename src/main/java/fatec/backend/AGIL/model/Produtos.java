package fatec.backend.AGIL.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity
@Table(name = "produtos")
public class Produtos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private BigDecimal preco;
    private Integer estoqueCaixas;
    private Integer qtdPorCaixa;
    @Column(columnDefinition = "LONGTEXT")
    private String imagem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_gestor", nullable = false)
    private GestorModel gestor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getEstoqueCaixas() {
        return estoqueCaixas;
    }

    public void setEstoqueCaixas(Integer estoqueCaixas) {
        this.estoqueCaixas = estoqueCaixas;
    }

    public Integer getQtdPorCaixa() {
        return qtdPorCaixa;
    }

    public void setQtdPorCaixa(Integer qtdPorCaixa) {
        this.qtdPorCaixa = qtdPorCaixa;
    }

    public GestorModel getGestor() {
        return gestor;
    }

    public void setGestor(GestorModel gestor) {
        this.gestor = gestor;
    }
    public String getImagem() {
    return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}