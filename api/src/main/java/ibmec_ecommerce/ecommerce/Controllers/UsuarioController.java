package ibmec_ecommerce.ecommerce.Controllers;

import ibmec_ecommerce.ecommerce.Model.Cartao;
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
public class UsuarioController {

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
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable int id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Método para criar um novo usuário
    @PostMapping(consumes = {"application/json", "application/json;charset=UTF-8"})
    public ResponseEntity<String> criarUsuario(@RequestBody Usuario usuario) {

    // Verifica se cartoes foi enviado no JSON
    if (usuario.getCartoes() != null) {
        for (Cartao cartao : usuario.getCartoes()) {
            cartao.setUsuario(usuario);
        }
    }

    usuarioRepository.save(usuario);

    return new ResponseEntity<>("Usuário criado com sucesso.", HttpStatus.CREATED);
}


    // Método para atualizar as informações de um usuário
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarUsuario(@PathVariable int id, @RequestBody Usuario usuario) {
        // Verificar se o usuário existe
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Atualizar o cartão de crédito do usuário, se necessário
        Usuario usuarioAtualizado = usuarioExistente.get();
        if (usuario.getCartoes() != null && !usuario.getCartoes().isEmpty()) {
            //  atualizar o cartão de crédito
            Cartao novoCartao = usuario.getCartoes().get(0);
            usuarioAtualizado.getCartoes().clear();  // Limpa os cartões antigos
            usuarioAtualizado.getCartoes().add(novoCartao);  // Adiciona o novo cartão
        }

        // Atualizar o restante das informações do usuário
        usuarioAtualizado.setNome(usuario.getNome());
        usuarioAtualizado.setEmail(usuario.getEmail());
        usuarioAtualizado.setCpf(usuario.getCpf());
        usuarioAtualizado.setTelefone(usuario.getTelefone());

        // Salvar o usuário atualizado no banco de dados
        usuarioRepository.save(usuarioAtualizado);

        return new ResponseEntity<>("Usuário atualizado.", HttpStatus.OK);
    }

    // Método para excluir um usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluirUsuario(@PathVariable int id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isEmpty()) {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }

        // Excluir o usuário da base de dados
        usuarioRepository.delete(usuario.get());

        return new ResponseEntity<>("Usuário excluído com sucesso.", HttpStatus.NO_CONTENT);
    }
}
