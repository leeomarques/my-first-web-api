package dio.my_first_web_api.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import dio.my_first_web_api.handler.CampoObrigatorioException;
import dio.my_first_web_api.model.Usuario;

@Repository
public class UsuarioRepository {

    public void save(Usuario usuario) {

        if (usuario.getLogin() == null) {
            throw new CampoObrigatorioException("login");
        }

        if (usuario.getPassword() == null) {
            throw new CampoObrigatorioException("senha");
        }

        if (usuario.getId() == null) {
            System.out.println("Saving new user");
        } else {
            System.out.println("Updating user");
        }
        System.out.println(usuario);
    }

    public void deleteById(Integer id) {
        System.out.println("Deleting user");
    }

    public List<Usuario> findAll() {
        System.out.println("Finding all users");

        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario("user1", "password1"));
        usuarios.add(new Usuario("user2", "password2"));
        usuarios.add(new Usuario("user3", "password3"));

        return usuarios;
    }

    public Usuario findById(Integer id) {
        System.out.println("Finding user by ID: " + id);
        return new Usuario("user" + id, "password" + id);
    }

    public Usuario findByUsername(String username) {
        System.out.println("Finding user by username: " + username);
        return new Usuario(username, "password");
    }
}
