package microservice.order.dtos.OrderDTO;

import microservice.order.dtos.OrderItemDTO.OrderItemResponseDTO;
import microservice.order.enums.Status;

import java.util.List;

public record OrderResponseDTO(
        Long id,
        Long userId,
        Status status,
        List<OrderItemResponseDTO> orderItems
) {
}
