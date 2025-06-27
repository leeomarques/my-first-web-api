package dio.my_first_web_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import dio.my_first_web_api.model.ApiResponse;
import dio.my_first_web_api.model.CreateUserRequest;
import dio.my_first_web_api.model.User;
import dio.my_first_web_api.model.Usuario;
import dio.my_first_web_api.repository.UserRepository;
import dio.my_first_web_api.repository.UsuarioRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/{username}")
    public Usuario getUsuarioByUsername(@PathVariable String username) {
        return usuarioRepository.findByUsername(username);
    }

    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable Integer id) {
        usuarioRepository.deleteById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUsuario(@RequestBody CreateUserRequest request) {
        try {
            // Use UserRepository ao invés de UsuarioRepository
            User existingUser = userRepository.findByUsername(request.getUsername());
            if (existingUser != null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Username already exists"));
            }

            // Validar roles permitidas
            if (request.getRoles() != null) {
                for (String role : request.getRoles()) {
                    if (!role.equals("USER") && !role.equals("MANAGERS")) {
                        return ResponseEntity.badRequest()
                                .body(new ApiResponse(false,
                                        "Invalid role: " + role + ". Allowed roles: USER, MANAGERS"));
                    }
                }
            }

            // Cria novo usuário usando o mesmo model
            User user = new User();
            user.setName(request.getName());
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            // Adiciona roles especificadas no request ou role padrão
            if (request.getRoles() != null && !request.getRoles().isEmpty()) {
                user.setRoles(request.getRoles());
            } else {
                user.getRoles().add("USER"); // Role padrão se não especificada
            }

            // Use UserRepository
            userRepository.save(user);

            return ResponseEntity.ok(new ApiResponse(true,
                    "User created successfully with roles: " + user.getRoles()));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Error creating user: " + e.getMessage()));
        }
    }

}
