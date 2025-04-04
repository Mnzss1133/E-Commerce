package com.ibmec_ecommerce.demo.model;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;
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
    }

