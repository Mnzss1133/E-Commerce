package ibmec_ecommerce.ecommerce.Model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;
@Data
@Entity(name = "endereco")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String logradouro;

    @Column
    private String complemento;

    @Column
    private String bairro;

    @Column
    private String cidade;

    @Column
    private String estado;

    @Column
    private String cep;

    // Relacionamento com o usuário
    @ManyToOne
    @JoinColumn(name = "id_usuario")  // Chave estrangeira para o usuário
    @JsonBackReference
    private Usuario usuario;  // Cada endereço pertence a um usuário
}

