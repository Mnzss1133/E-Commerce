package bigdata.ecommerce.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

public class Usuario {
    public Usuario() {
        this.creditCards = new LinkedList<>();
        this.addresses = new LinkedList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "data_nascimento")
    private LocalDateTime dataNascimento;

    @Column(name = "cpf", unique = true)
    private String cpf;

    @Column(name = "numero_telefone")
    private String numero;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Cartao> creditCards;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Endereco> addresses;
}