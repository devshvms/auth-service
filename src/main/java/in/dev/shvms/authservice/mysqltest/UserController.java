package in.dev.shvms.authservice.mysqltest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    UserRepository userRepository;
    public UserController (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/getAll")
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<User>> addUser(@RequestBody User user) {
        System.out.print("ThreadName while saving : " + Thread.currentThread() + "\n");
        return ResponseEntity.of(Optional.of(
                userRepository.save(user)
                        .doOnSuccess((data)-> System.out.println("Data added for username: " + data.getUsername() + "\n"))
                        .doOnError((error) ->
                            System.out.println("Error: " + error.getMessage() + " while adding user.")
                        )));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Mono<User>> getByUser(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(userRepository.findById(id));
    }
}
