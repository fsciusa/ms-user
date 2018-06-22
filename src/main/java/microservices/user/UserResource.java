package microservices.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")

public class UserResource {

    private static Logger logger = LogManager.getLogger(UserResource.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getOne(@PathVariable int id) {
        Optional<User> one = userRepository.findById(id);
        return one.get();
    }

    @GetMapping("/logon/{id}/password/{password}/cid/{cid}/caller/{caller}")
    public int logon(@PathVariable int id, @PathVariable String password, @PathVariable int cid, @PathVariable String caller) {
        logger.info("RES\t{}\t{}\tuser\t/logon/{}", cid, caller, id);
        return id;
    }
    @PostMapping("/create")
    public void create(@RequestBody User user) {
        User savedUser = userRepository.save(user);
    }

    @PutMapping("/update/{id}")
    public void update(@RequestBody User user, @PathVariable int id) {
        user.setId(id);
        userRepository.save(user);
    }
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        userRepository.deleteById(id);
    }

}
