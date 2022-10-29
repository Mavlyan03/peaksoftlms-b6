package kg.peaksoft.peaksoftlmsb6.security;

import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(
                () -> new NotFoundException(String.format("Not found with email =%s",username)));
    }
}
