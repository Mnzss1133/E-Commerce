package bigdata.ecommerce.controller;

import bigdata.ecommerce.model.Cartao;
import bigdata.ecommerce.model.Usuario;
import bigdata.ecommerce.repository.CartaoRepository;
import bigdata.ecommerce.repository.UsuarioRepository;
import bigdata.ecommerce.request.TransacaoRequest;
import bigdata.ecommerce.request.TransacaoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("users/{id_user}/credit-card")
public class CartaoController {

    private final CartaoRepository cartaoRepository;
    private final UsuarioRepository usuarioRepository;

    public CartaoController(CartaoRepository cartaoRepository, UsuarioRepository usuarioRepository) {
        this.cartaoRepository = cartaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<Usuario> create(@PathVariable("id_user") int idUser, @RequestBody Cartao cartao) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(idUser);

        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Usuario usuario = optionalUsuario.get();

        cartaoRepository.save(cartao);
        usuario.getCartoes().add(cartao);
        usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PostMapping("/authorize")
    public ResponseEntity<TransacaoResponse> authorize(@PathVariable("id_user") int idUser,
                                                       @RequestBody TransacaoRequest request) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(idUser);
        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Usuario usuario = optionalUsuario.get();
        Cartao cartao = usuario.getCartoes().stream()
                .filter(c -> c.getNumero().equals(request.getNumero()) && c.getCvv().equals(request.getCvv()))
                .findFirst()
                .orElse(null);

        if (cartao == null) {
            return criarResposta("NOT_AUTHORIZED", "Cartão não encontrado para o usuário", HttpStatus.NOT_FOUND);
        }

        if (cartao.getDtExpiracao().isBefore(LocalDateTime.now())) {
            return criarResposta("NOT_AUTHORIZED", "Cartão Expirado", HttpStatus.BAD_REQUEST);
        }

        if (cartao.getSaldo() < request.getValor()) {
            return criarResposta("NOT_AUTHORIZED", "Sem saldo para realizar a compra", HttpStatus.BAD_REQUEST);
        }

        cartao.setSaldo(cartao.getSaldo() - request.getValor());
        cartaoRepository.save(cartao);

        TransacaoResponse response = new TransacaoResponse();
        response.setStatus("AUTHORIZED");
        response.setDtTransacao(LocalDateTime.now());
        response.setMessage("Compra autorizada");
        response.setCodigoAutorizacao(UUID.randomUUID());

        return ResponseEntity.ok(response);
    }

    private ResponseEntity<TransacaoResponse> criarResposta(String status, String mensagem, HttpStatus httpStatus) {
        TransacaoResponse response = new TransacaoResponse();
        response.setStatus(status);
        response.setDtTransacao(LocalDateTime.now());
        response.setMessage(mensagem);
        return ResponseEntity.status(httpStatus).body(response);
    }
}
