package ibmec_ecommerce.ecommerce.Model;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
@Data
@Entity(name="cartao")
public class Cartao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String numero;

    @Column
    private LocalDateTime dtExpiracao;

    @Column
    private String cvv;

    @Column
    private Double saldo;

    // Adicionando o relacionamento com o usuário
    @ManyToOne
    @JoinColumn(name = "id_usuario")  // Chave estrangeira para o usuário
    @JsonBackReference
    private Usuario usuario;
}
