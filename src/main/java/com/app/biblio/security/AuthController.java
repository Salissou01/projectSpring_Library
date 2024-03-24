package com.app.biblio.security;
import com.app.biblio.bean.Role;
import com.app.biblio.bean.User;

import com.app.biblio.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;
@PostMapping("/api/login")
public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    User user = userService.findByUsername(authentication.getName());

    Set<Role> rolesSet = user.getRoles();

    List<String> rolesList = rolesSet.stream()
                                     .map(Role::getName)
                                     .collect(Collectors.toList());

   
    String jwt = jwtProvider.generateToken(authentication.getName(), user.getId(), rolesList);
    return ResponseEntity.ok(new JwtResponse(jwt));
}

   
}
