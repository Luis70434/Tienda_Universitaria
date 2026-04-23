package edu.unimagdalena.ecommerce.security.web;

import edu.unimagdalena.ecommerce.security.domine.AppUser;
import edu.unimagdalena.ecommerce.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    // Define un record simple para el request
    public record LoginRequest(String username, String password) {}
    public record AuthResponse(String token) {}

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // 1. Spring verifica usuario y contraseña
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        // 2. Si es exitoso, buscamos al usuario
        AppUser user = (AppUser) userDetailsService.loadUserByUsername(request.username());

        // 3. Generamos el Token
        String jwtToken = jwtService.generateToken(user);

        // 4. Se lo enviamos al cliente
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }
}
