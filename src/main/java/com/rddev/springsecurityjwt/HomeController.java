package com.rddev.springsecurityjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rddev.springsecurityjwt.models.AuthenticationRequest;
import com.rddev.springsecurityjwt.models.AuthenticationResponse;
import com.rddev.springsecurityjwt.utils.JwtUtil;

@RestController
public class HomeController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private MyUserDetailsService userDetailsService;

  @Autowired
  private JwtUtil jwtUtil;
  
  @GetMapping("/hello")
  public String getHello() {
    return "Hello World";
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception  {
    try {
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
      );
    } catch (BadCredentialsException e) {
      throw new Exception("Incorrect username or password", e);
    }

    final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

    final String jwt = jwtUtil.generateToken(userDetails);

    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }
}
