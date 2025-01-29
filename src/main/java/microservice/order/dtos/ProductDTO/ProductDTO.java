package microservice.order.dtos.ProductDTO;

public record ProductDTO(
        String name,
        String description,
        Double price,
        Integer stock
) {
}
