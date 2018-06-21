package microservices.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserResource {

    private static Logger logger = LogManager.getLogger(UserResource.class);

    @Autowired
    private UserReposity userReposity;

    @GetMapping("/users")
    public List<User> getAll() {
        return userReposity.findAll();
    }

    @GetMapping("/users/{id}")
    public User getOne(@PathVariable int id) {
        Optional<User> one = userReposity.findById(id);
        return one.get();
    }

    @GetMapping("/users/{id}/orders/cid/{cid}")
    public List<Purchase> getUserOrder(@PathVariable int id, @PathVariable int cid) {

        logger.info("CALL\t{}\tINIT\tUsers\t/users/{}/orders", cid, id);

        final String productUri = "http://localhost:8092/products";
        final String orderUri = "http://localhost:8093/orders";

        List<Purchase> result = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        try {
            logger.info("CALL\t{}\tUsers\tOrders\t{}", cid, orderUri);
            ResponseEntity<List<Purchase>> rateResponse =
                    restTemplate.exchange(orderUri + "/cid/" + cid + "/caller/Users",
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<Purchase>>() {
                            });
        List<Purchase> allOrders = rateResponse.getBody();
        System.out.println("id: "+id);

        for (Purchase order : allOrders) {
            System.out.println("Order user id: " + order.getUserId());
            if (order.getUserId() == id) {
                result.add(order);

                final String prodIdUri = productUri + "/" + order.getProductId();
                logger.info("CALL\t{}\tUsers\tProducts\t{}", cid, prodIdUri);
                Product prod = restTemplate.getForObject(prodIdUri + "/cid/" + cid + "/caller/Users", Product.class);
                System.out.println("Prod name : " + prod.getName());
                order.setProductName(prod.getName());
            }
        }
        }catch(Exception e){
            logger.info("FAIL\t{}\t-----\t-----\t{}", cid, e.getMessage());
        }

        return result;
    }

    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable int id) {
        userReposity.deleteById(id);
    }

    @PostMapping("/users")
    public void create(@RequestBody User user) {
        User savedUser = userReposity.save(user);
    }

    @PutMapping("/users/{id}")
    public void update(@RequestBody User user, @PathVariable int id) {
        Optional<User> a = userReposity.findById(id);

        user.setId(id);
        userReposity.save(user);
    }
}
