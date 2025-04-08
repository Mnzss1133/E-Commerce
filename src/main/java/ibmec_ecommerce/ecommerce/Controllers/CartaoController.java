package ibmec_ecommerce.ecommerce.Controllers;

import ibmec_ecommerce.ecommerce.Model.Cartao;
import ibmec_ecommerce.ecommerce.Model.Usuario;
import ibmec_ecommerce.ecommerce.Repository.CartaoRepositorio;
import ibmec_ecommerce.ecommerce.Repository.UsuarioRepositorio;
import ibmec_ecommerce.ecommerce.Request.TransacaoRequest;
import ibmec_ecommerce.ecommerce.Request.TransacaoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users/{idusuario}/credit-card")
public class CartaoController {

    @Autowired
    private CartaoRepositorio cartaoRepository;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @PostMapping
    public ResponseEntity<Usuario> adicionarCartao(@PathVariable("idusuario") int idUser, @RequestBody Cartao cartao) {
        Optional<Usuario> usuarioOptional =usuarioRepositorio.findById(idUser);

        if (usuarioOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioOptional.get();

        // Salva o cartão e associa ao usuário
        cartaoRepository.save(cartao);
        usuario.getCartoes().add(cartao);
        usuarioRepositorio.save(usuario);

        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @PostMapping("/authorize")
    public ResponseEntity<TransacaoResponse> autorizarCompra(@PathVariable("idusuario") int idUser,
                                                             @RequestBody TransacaoRequest request) {
        Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(idUser);

        if (usuarioOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Usuario usuario = usuarioOptional.get();
        Cartao cartaoEncontrado = null;

        for (Cartao cartao : usuario.getCartoes()) {
            if (cartao.getNumero().equals(request.getNumero()) &&
                    cartao.getCvv().equals(request.getCvv())) {
                cartaoEncontrado = cartao;
                break;
            }
        }

        TransacaoResponse response = new TransacaoResponse();
        response.setDataHora(LocalDateTime.now());

        if (cartaoEncontrado == null) {
            response.setStatus("NOT_AUTHORIZED");
            response.setMensagem("Cartão não encontrado para o usuário");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (cartaoEncontrado.getDtExpiracao().isBefore(LocalDateTime.now())) {
            response.setStatus("NOT_AUTHORIZED");
            response.setMensagem("Cartão expirado");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (cartaoEncontrado.getSaldo() < request.getValor()) {
            response.setStatus("NOT_AUTHORIZED");
            response.setMensagem("Saldo insuficiente");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Atualiza saldo e salva
        Double novoSaldo = cartaoEncontrado.getSaldo() - request.getValor();
        cartaoEncontrado.setSaldo(novoSaldo);
        cartaoRepository.save(cartaoEncontrado);

        // Compra autorizada
        response.setStatus("AUTHORIZED");
        response.setMensagem("Compra autorizada");
        response.setCodigoAutorizacao(UUID.randomUUID());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
