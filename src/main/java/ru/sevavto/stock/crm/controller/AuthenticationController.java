package ru.sevavto.stock.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sevavto.stock.crm.model.dto.security.AuthenticationRequest;
import ru.sevavto.stock.crm.model.dto.security.AuthenticationResponse;
import ru.sevavto.stock.crm.model.dto.security.TokenRefreshRequest;
import ru.sevavto.stock.crm.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }
}
