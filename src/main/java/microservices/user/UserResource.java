package microservices.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserResource {

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

    @GetMapping("/users/{id}/orders/")
    public List<Purchase> getUserOrder(@PathVariable int id) {


        final String productUri = "http://localhost:8092/products/";
        final String orderUri = "http://localhost:8093/orders/";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<Purchase>> rateResponse =
                restTemplate.exchange(orderUri,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Purchase>>() {
                        });
        List<Purchase> allOrders = rateResponse.getBody();

        List<Purchase> result = new ArrayList<>();

        System.out.println("id: "+id);

        for (Purchase order : allOrders) {
            System.out.println("Order user id: " + order.getUserId());
            if (order.getUserId() == id) {
                result.add(order);

                final String prodIdUri = productUri + order.getProductId();
                Product prod = restTemplate.getForObject(prodIdUri, Product.class);
                System.out.println("Prod name : " + prod.getName());
                order.setProductName(prod.getName());
            }
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
