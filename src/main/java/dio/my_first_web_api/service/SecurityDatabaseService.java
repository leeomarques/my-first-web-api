package dio.my_first_web_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dio.my_first_web_api.model.User;
import dio.my_first_web_api.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class SecurityDatabaseService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public SecurityDatabaseService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            User userEntity = userRepository.findByUsername(username);
            if (userEntity == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            Set<GrantedAuthority> authorities = new HashSet<>();
            userEntity.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            });

            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    userEntity.getUsername(),
                    userEntity.getPassword(),
                    authorities);

            return userDetails;

        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("Error loading user: " + username, e);
        }
    }
}