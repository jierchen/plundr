package jier.plundr.security;

import jier.plundr.model.PlundrUser;
import jier.plundr.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PlundrUser user = userRepository.findByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException(String.format("Username %s not found", username));
        }

        return new UserDetailsImpl(user);
    }
}
