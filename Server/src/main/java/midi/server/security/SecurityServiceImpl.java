package midi.server.security;

import midi.common.security.SecurityService;
import midi.common.security.User;
import midi.server.security.dao.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Inject private AuthenticationManager authenticationManager;
    @Inject private UserRepository userRepository;
    @Inject private PasswordEncoder encoder;

    @Override
    public void login(String userName, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userName, password);
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return userRepository.findByUsername(username);
    }

    @Override
    public void registerNewUserAccount(User user) {
        String originalPass = user.getPassword();
        user.setPassword(encoder.encode(originalPass));
        userRepository.save(user);
        login(user.getUsername(), originalPass);
    }
}
