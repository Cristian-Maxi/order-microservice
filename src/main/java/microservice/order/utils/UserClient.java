package microservice.order.utils;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient {

    @Autowired
    private RestTemplate restTemplate;

    public void validateUser(Long userId) {
        String url = "http://getaway/api/user/validateUser/{id}";
        try {
            restTemplate.getForObject(url, Void.class, userId);
        } catch (HttpClientErrorException.NotFound e) {
            throw new EntityNotFoundException("Usuario con ID " + userId + " no encontrado.");
        }
    }

    public Long validateUserEmail(String email) {
        String url = "http://getaway/api/user/validateUserByEmail/{email}";
        try {
            return restTemplate.getForObject(url, Long.class, email);
        } catch (HttpClientErrorException.NotFound e) {
            throw new EntityNotFoundException("Usuario con email " + email + " no encontrado.");
        }
    }
}
