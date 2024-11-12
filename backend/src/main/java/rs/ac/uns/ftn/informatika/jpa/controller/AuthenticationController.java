package rs.ac.uns.ftn.informatika.jpa.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rs.ac.uns.ftn.informatika.jpa.dto.util.JwtAuthenticationRequest;
import rs.ac.uns.ftn.informatika.jpa.dto.util.UserRequest;
import rs.ac.uns.ftn.informatika.jpa.dto.util.UserTokenState;
import rs.ac.uns.ftn.informatika.jpa.exception.ResourceConflictException;
import rs.ac.uns.ftn.informatika.jpa.model.Profile;
import rs.ac.uns.ftn.informatika.jpa.service.ProfileService;
import rs.ac.uns.ftn.informatika.jpa.utils.TokenUtils;

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

    // Prvi endpoint koji pogadja korisnik kada se loguje.
    // Tada zna samo svoje korisnicko ime i lozinku i to prosledjuje na backend.
    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        Profile user = (Profile) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername(), user.getRole());
        int expiresIn = tokenUtils.getExpiredIn();

        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }

    // Endpoint za registraciju novog korisnika
    @PostMapping("/signup")
    public ResponseEntity<Profile> addUser(@RequestBody UserRequest userRequest, UriComponentsBuilder ucBuilder) {
        Profile existUser = this.userService.findByUsername(userRequest.getUsername());

        if (existUser != null) {
            throw new ResourceConflictException(userRequest.getId(), "Username already exists");
        }

        Profile user = this.userService.saveProfile(userRequest);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/whoami")
    // @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // obrisati komentar posle
    public Profile user(Principal user) {
        return this.userService.findByUsername(user.getName());
    }
}