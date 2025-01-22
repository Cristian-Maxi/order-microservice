package microservice.order.mappers;

import microservice.order.dtos.OrderDTO.OrderResponseDTO;
import microservice.order.dtos.OrderDTO.OrderUpdateDTO;
import microservice.order.dtos.OrderItemDTO.OrderItemResponseDTO;
import microservice.order.models.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    public OrderResponseDTO toResponseDTO(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getUserId(),
                order.getStatus(),
                order.getOrderItemList().stream()
                        .map(orderItem -> new OrderItemResponseDTO(
                                orderItem.getId(),
                                orderItem.getProductId(),
                                orderItem.getQuantity()
                        )).collect(Collectors.toList())
        );
    }

    public Set<OrderResponseDTO> toResponseSetDTO(List<Order> orderList) {
        return orderList.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toSet());
    }

    public OrderUpdateDTO toUpdateDTO(Order order) {
        return new OrderUpdateDTO(
                order.getId(),
                order.getStatus()
        );
    }
}
