package rs.ac.uns.ftn.informatika.jpa.service;

import com.google.common.hash.BloomFilter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.dto.util.UserTokenState;
import rs.ac.uns.ftn.informatika.jpa.exception.AccountNotActivatedException;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.service.inferface.IAuthService;
import rs.ac.uns.ftn.informatika.jpa.utils.TokenUtils;

@Service
public class AuthService implements IAuthService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);
    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;


    public AuthService(AuthenticationManager authenticationManager, TokenUtils tokenUtils ) {
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
    }

    @RateLimiter(name = "login", fallbackMethod = "loginFallback")
    public UserTokenState login(String username, String password) {
        LOG.info("Pokušaj logovanja za korisnika: {}", username);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        Profile user = (Profile) authentication.getPrincipal();
        if (!user.isActivated()) {
            throw new AccountNotActivatedException("Account not activated. Please activate your account via email.");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenUtils.generateToken(user.getUsername(), user.getRole());
        int expiresIn = tokenUtils.getExpiredIn();

        return new UserTokenState(jwt, expiresIn);
    }

    public UserTokenState loginFallback(String username, String password, RequestNotPermitted exception) {
        LOG.warn("Rate limit prekoračen za pokušaj logovanja korisnika: {}. Poruka: {}", username, exception.getMessage());
        // Ne vraćamo token, već bacamo izuzetak koji će biti uhvaćen u kontroleru.
        throw exception;
    }

    public UserTokenState loginFallback2(String username, String password, RequestNotPermitted exception) {
        LOG.warn("Rate limit prekoračen za korisnika: {}", username);
        return new UserTokenState("RATE_LIMITED", 0);
    }

}


