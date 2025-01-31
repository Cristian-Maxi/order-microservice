package microservice.order.utils;

import microservice.order.dtos.OrderItemDTO.OrderItemRequestDTO;
import microservice.order.dtos.ProductDTO.ProductDTO;
import microservice.order.exceptions.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;

@Component
public class ProductClient {

    @Autowired
    private RestTemplate restTemplate;

    public void reserveStock(Set<OrderItemRequestDTO> items) {
        String url = "http://product/api/product/reserve-stock";
        try {
            restTemplate.postForObject(url, items, Void.class);
        } catch (HttpClientErrorException e) {
            throw new ApplicationException(e.getResponseBodyAsString());
        }
    }

    public Map<Long, String> getProductNames(Set<Long> productIds) {
        String url = "http://product/api/product/names";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Set<Long>> requestEntity = new HttpEntity<>(productIds, headers);

        try {
            ResponseEntity<Map<Long, String>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<Map<Long, String>>() {}
            );

            if (response.getBody() == null || response.getBody().isEmpty()) {
                throw new ApplicationException("No se encontraron nombres para los productos solicitados.");
            }

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new ApplicationException("Error al obtener nombres de productos: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            throw new ApplicationException("Error de conexión con el servicio de productos.");
        }
    }

    public Map<Long, ProductDTO> getProductDetails(Set<Long> productIds) {
        String url = "http://product/api/product/details";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Set<Long>> requestEntity = new HttpEntity<>(productIds, headers);

        try {
            ResponseEntity<Map<Long, ProductDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<Map<Long, ProductDTO>>() {}
            );

            if (response.getBody() == null || response.getBody().isEmpty()) {
                throw new ApplicationException("No se encontraron nombres para los productos solicitados.");
            }

            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new ApplicationException("Error al obtener nombres de productos: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            throw new ApplicationException("Error de conexión con el servicio de productos.");
        }
    }
    
}