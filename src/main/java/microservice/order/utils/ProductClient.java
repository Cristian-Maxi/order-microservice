package microservice.order.utils;

import microservice.order.dtos.OrderItemDTO.OrderItemRequestDTO;
import microservice.order.dtos.ProductDTO.ProductDTO;
import microservice.order.exceptions.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;

@Component
public class ProductClient {

    @Autowired
    private RestTemplate restTemplate;

    public void reserveStock(Set<OrderItemRequestDTO> items) {
        String url = "http://getaway/api/product/reserve-stock";
        try {
            restTemplate.postForObject(url, items, Void.class);
        } catch (HttpClientErrorException e) {
            throw new ApplicationException(e.getResponseBodyAsString());
        }
    }

    public Map<Long, String> getProductNames(Set<Long> productIds) {
        String url = "http://getaway/api/product/names";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Set<Long>> requestEntity = new HttpEntity<>(productIds, headers);

        ResponseEntity<Map<Long, String>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<Long, String>>() {}
        );

        return response.getBody();
    }

    public Map<Long, ProductDTO> getProductDetails(Set<Long> productIds) {
        String url = "http://getaway/api/product/details";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Set<Long>> requestEntity = new HttpEntity<>(productIds, headers);

        ResponseEntity<Map<Long, ProductDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<Long, ProductDTO>>() {}
        );

        return response.getBody();
    }
    
}