package bigdata.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity(name="cartao")
public class Cartao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String card_number;

    @Column
    private LocalDateTime ExpirationDate;

    @Column
    private String cvv;

    @Column
    private Double balance;
}