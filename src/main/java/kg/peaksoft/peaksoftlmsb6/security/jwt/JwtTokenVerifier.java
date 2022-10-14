package kg.peaksoft.peaksoftlmsb6.security.jwt;


import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Optional<String> optional = getJwtFromRequest(request);

        optional.ifPresent(token -> {
                    String email = jwtTokenUtil.validateJWTToken(token);

                    User user = userRepository.findByEmail(email).orElseThrow(
                            () -> new UsernameNotFoundException(String.format("email(%s) not found", email)));
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }
        );

        filterChain.doFilter(request, response);
    }

    private Optional<String> getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return Optional.of(bearerToken.substring(BEARER.length()));
        }

        return Optional.empty();
    }
}