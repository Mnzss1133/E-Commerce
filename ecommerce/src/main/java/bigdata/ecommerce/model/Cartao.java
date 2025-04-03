package bigdata.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
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
}
