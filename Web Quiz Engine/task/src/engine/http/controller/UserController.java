package engine.http.controller;

import engine.entity.User;
import engine.http.dto.request.UserCreateDTO;
import engine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository repository;

    @PostMapping(value = "/api/register")
    @ResponseBody
    public ResponseEntity<Object> register(
            @RequestBody UserCreateDTO userCreateDTO
    ) {
        Optional<User> user = this.repository.findByEmail(userCreateDTO.getEmail());

        if(user.isPresent()) {
            return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
        }

        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

        Pattern pattern = Pattern.compile(regex);
        String email = userCreateDTO.getEmail();
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()) {
            return new ResponseEntity<>("Email is wrong", HttpStatus.BAD_REQUEST);
        }

        String password = userCreateDTO.getPassword();
        if(password.length() < 5) {
            return new ResponseEntity<>("Password must be 5 length", HttpStatus.BAD_REQUEST);
        }

        User u = new User(email, passwordEncoder.encode(password));

        this.repository.save(u);

        return new ResponseEntity<>("Valid", HttpStatus.OK);
    }
}
