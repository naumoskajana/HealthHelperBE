package com.example.healthhelper.web;

import com.example.healthhelper.config.security.JwtUtils;
import com.example.healthhelper.dto.UserDTO;
import com.example.healthhelper.dto.UserLoginDTO;
import com.example.healthhelper.dto.UserLoginResponseDTO;
import com.example.healthhelper.dto.UserRegistrationDTO;
import com.example.healthhelper.entity.User;
import com.example.healthhelper.exceptions.UserAlreadyExistsException;
import com.example.healthhelper.rdf.RdfModelReport;
import com.example.healthhelper.service.UserService;
import org.apache.jena.rdf.model.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringWriter;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        try {
            userService.register(userRegistrationDTO);
            return ResponseEntity.ok("User registered successfully.");
        } catch (UserAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        User user = userService.login(userLoginDTO);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserLoginResponseDTO userResponse = new UserLoginResponseDTO();
        UserDTO userDTO = user.getAsUserDTO();
        userResponse.setUser(userDTO);
        userResponse.setToken(jwtUtils.generateJwtToken(authentication));

        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestParam("token") String token) {
        return ResponseEntity.ok(jwtUtils.validateJwtToken(token));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getLoggedInUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(user.getAsUserDTO());
    }

    @GetMapping("/generateReport")
    public ResponseEntity<String> generateReport() {
        StringWriter stringWriter = new StringWriter();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Model model = RdfModelReport.getReportModel(user);
        model.write(stringWriter, "RDF/XML-ABBREV");
        return ResponseEntity.ok(stringWriter.toString());
    }
}
