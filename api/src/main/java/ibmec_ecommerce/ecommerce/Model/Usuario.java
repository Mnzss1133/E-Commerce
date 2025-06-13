package ibmec_ecommerce.ecommerce.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;




@Data
@Entity(name = "usuario")
public class Usuario {

    public Usuario() {
        this.cartoes = new ArrayList<>();
        this.enderecos = new ArrayList<>();
        this.status = "Ativo";  // Adicionando um status como atributo
    }

    @Transient
    private List<Pedido> pedidos = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String nome;

    @Column
    private String email;

    @Column   
    private LocalDateTime dtNascimento;

    @Column
    private String cpf;

    @Column
    private String telefone;

    @Column
    private String status;  // Novo atributo para armazenar o status do usuário (ativo, inativo, etc.)

    // Relacionamento OneToMany com Cartao
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    @JsonManagedReference
    private List<Cartao> cartoes = new ArrayList<>();

    // Relacionamento OneToMany com Endereco
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    @JsonManagedReference
    private List<Endereco> enderecos;

    // Método para verificar se o saldo total de todos os cartões é suficiente
    public boolean temSaldoSuficiente(Double valor) {
        return cartoes.stream().anyMatch(cartao -> cartao.getSaldo() >= valor);
    }
}
