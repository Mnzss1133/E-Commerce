package ibmec_ecommerce.ecommerce.Controllers;



import ibmec_ecommerce.ecommerce.Model.Endereco;
import ibmec_ecommerce.ecommerce.Model.Usuario;
import ibmec_ecommerce.ecommerce.Repository.EnderecoRepositorio;
import ibmec_ecommerce.ecommerce.Repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/usuarios/{id_usuario}/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoRepositorio enderecoRepository;

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    @PostMapping
    public ResponseEntity<Endereco> createEndereco(@PathVariable("id_usuario") int idUsuario, @RequestBody Endereco endereco) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(idUsuario);

        if (optionalUsuario.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Associa o endereço ao usuário
        Usuario usuario = optionalUsuario.get();
        endereco.setUsuario(usuario);

        // Salva o endereço no banco de dados
        Endereco enderecoSalvo = enderecoRepository.save(endereco);

        return new ResponseEntity<>(enderecoSalvo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> listarEnderecos(@PathVariable("id_usuario") int idUsuario) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(idUsuario);

        if (optionalUsuario.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalUsuario.get().getEnderecos(), HttpStatus.OK);
    }

    @DeleteMapping("/{id_endereco}")
    public ResponseEntity<Void> deletarEndereco(@PathVariable("id_usuario") int idUsuario,
                                                @PathVariable("id_endereco") int idEndereco) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(idUsuario);
        Optional<Endereco> optionalEndereco = enderecoRepository.findById(idEndereco);

        if (optionalUsuario.isEmpty() || optionalEndereco.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Endereco endereco = optionalEndereco.get();
        if (endereco.getUsuario().getId() != idUsuario) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        enderecoRepository.delete(endereco);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}