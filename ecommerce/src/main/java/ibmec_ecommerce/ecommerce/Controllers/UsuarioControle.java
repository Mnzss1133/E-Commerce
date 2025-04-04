package ibmec_ecommerce.ecommerce.Controllers;

import ibmec_ecommerce.ecommerce.Model.Usuario;
import ibmec_ecommerce.ecommerce.Repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    // Método para criar um novo usuário
    @PostMapping
    public ResponseEntity<String> criarUsuario(@RequestBody Usuario usuario) {
        // Verificar se o saldo do cartão de crédito do usuário é suficiente
        if (usuario.getCartao().getSaldo() < 0) {
            return ResponseEntity.badRequest().body("Saldo insuficiente no cartão de crédito.");
        }

        // Salvar o usuário no banco de dados
        usuarioRepository.save(usuario);

        // Retornar resposta com sucesso
        return ResponseEntity.ok("Usuário criado com sucesso.");
    }

    // Método para atualizar as informações de um usuário
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        // Verificar se o usuário existe
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Atualizar o usuário no banco de dados
        usuario.setId(id);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuário atualizado com sucesso.");
    }
}
