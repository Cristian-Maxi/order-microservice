package microservice.order.utils;

import microservice.order.dtos.ApiResponseDTO;
import microservice.order.dtos.OrderItemDTO.OrderItemRequestDTO;
import microservice.order.exceptions.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Component
public class ProductClient {

    @Autowired
    private RestTemplate restTemplate;

    public void reserveStock(Set<OrderItemRequestDTO> items) {
        String url = "http://localhost:8081/api/product/reserve-stock";
        try {
            restTemplate.postForObject(url, items, Void.class);
        } catch (HttpClientErrorException e) {
            throw new ApplicationException(e.getResponseBodyAsString());
        }
    }
}
