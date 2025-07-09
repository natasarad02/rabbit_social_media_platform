package rs.ac.uns.ftn.informatika.jpa.controller;

import javax.servlet.http.HttpServletResponse;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rs.ac.uns.ftn.informatika.jpa.dto.ProfileDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.util.JwtAuthenticationRequest;
import rs.ac.uns.ftn.informatika.jpa.dto.util.UserRequest;
import rs.ac.uns.ftn.informatika.jpa.dto.util.UserTokenState;
import rs.ac.uns.ftn.informatika.jpa.exception.AccountNotActivatedException;
import rs.ac.uns.ftn.informatika.jpa.exception.ResourceConflictException;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.service.AuthService;
import rs.ac.uns.ftn.informatika.jpa.service.ProfileService;
import rs.ac.uns.ftn.informatika.jpa.utils.TokenUtils;
import rs.ac.uns.ftn.informatika.jpa.service.inferface.IAuthService;

import java.security.Principal;

//Kontroler zaduzen za autentifikaciju korisnika
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ProfileService userService;

    @Autowired
    private IAuthService authService;

    // Prvi endpoint koji pogadja korisnik kada se loguje.
    // Tada zna samo svoje korisnicko ime i lozinku i to prosledjuje na backend.
    @PostMapping("/login")
    public ResponseEntity<Object> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) {
        try {
            // Pozivamo servisnu metodu koja ima Rate Limiter na sebi
            UserTokenState token = authService.login(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );
            return ResponseEntity.ok(token);

        } catch (RequestNotPermitted ex) {
            // Hvatamo izuzetak bačen iz fallback metode
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS) // HTTP 429
                    .body("Previše neuspelih pokušaja logovanja. Molimo pokušajte ponovo kasnije.");
        } catch (BadCredentialsException ex) {
            // Hvatamo grešku za pogrešne kredencijale
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Pogrešno korisničko ime ili lozinka.");
        } catch (AccountNotActivatedException ex) {
            // Hvatamo grešku za neaktiviran nalog
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ex.getMessage());
        } catch (Exception ex) {
            // Opšti handler za sve ostale greške
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Došlo je do greške prilikom logovanja: " + ex.getMessage());
        }
    }


    // Endpoint za registraciju novog korisnika
    @PostMapping("/signup")
    public ResponseEntity<Profile> addUser(@RequestBody UserRequest userRequest, UriComponentsBuilder ucBuilder) {
        Profile user = this.userService.createProfile(userRequest);
        this.userService.sendActivationEmail(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/userFromToken")
    public ResponseEntity<ProfileDTO> GetUserProfile(@RequestHeader(value = "Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {

                String token = authorizationHeader.substring(7);


                String username = tokenUtils.getUsernameFromToken(token);


                if (username != null) {
                    Profile profile = userService.findByUsername(username);


                    if (profile != null) {
                        return new ResponseEntity<>(new ProfileDTO(profile), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }
                }
            } catch (ExpiredJwtException ex) {

                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } catch (Exception e) {

                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }



}