package microservice.order.services;

import microservice.order.dtos.OrderDTO.OrderRequestDTO;
import microservice.order.dtos.OrderDTO.OrderResponseDTO;
import microservice.order.dtos.OrderDTO.OrderUpdateDTO;

import java.util.Set;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO);
    OrderUpdateDTO updateOrderStatus(OrderUpdateDTO orderUpdateDTO);
    Set<OrderResponseDTO> getAllOrders();
}
