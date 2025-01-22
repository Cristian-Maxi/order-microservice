package microservice.order.dtos.OrderDTO;

import microservice.order.dtos.OrderItemDTO.OrderItemRequestDTO;

import java.util.List;

public record OrderRequestDTO(
        Long userId,
        List<OrderItemRequestDTO> orders
) {
}