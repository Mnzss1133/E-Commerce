package ibmec_ecommerce.ecommerce.Controllers;

import ibmec_ecommerce.ecommerce.Model.Usuario;
import ibmec_ecommerce.ecommerce.Repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    // Método para listar todos os usuários
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    // Método para buscar um usuário pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable int idUser) {
        Optional<Usuario> usuario = usuarioRepository.findById(idUser);
        return usuario.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Método para criar um novo usuário
    @PostMapping
    public ResponseEntity<String> criarUsuario(@RequestBody Usuario usuario) {
        // Verificar se o saldo do cartão de crédito do usuário é suficiente
        if (!usuario.temSaldoSuficiente(100.0)) {  // Exemplo de verificação com valor 100.0
            return ResponseEntity.badRequest().body("Saldo insuficiente no cartão de crédito.");
        }

        // Salvar o usuário no banco de dados
        usuarioRepository.save(usuario);

        // Retornar resposta com sucesso
        return new ResponseEntity<>("Usuário criado com sucesso.", HttpStatus.CREATED);
    }

    // Método para atualizar as informações de um usuário
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarUsuario(@PathVariable int idUser, @RequestBody Usuario usuario) {
        // Verificar se o usuário existe
        if (!usuarioRepository.existsById(idUser)) {
            return ResponseEntity.notFound().build();
        }

        // Atualizar o usuário no banco de dados
        usuarioRepository.save(usuario);

        return new ResponseEntity<>("Usuário atualizado com sucesso.", HttpStatus.OK);
    }

    // Método para excluir um usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirUsuario(@PathVariable int idUser) {
        Optional<Usuario> usuario = usuarioRepository.findById(idUser);
        if (usuario.isEmpty()) {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }

        // Excluir o usuário da base de dados
        usuarioRepository.delete(usuario.get());

        return new ResponseEntity<>("Usuário excluído com sucesso.", HttpStatus.NO_CONTENT);
    }
}
