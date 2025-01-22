package microservice.order.dtos.OrderItemDTO;

public record OrderItemRequestDTO(
        Long productId,
        Integer quantity
) {
}
