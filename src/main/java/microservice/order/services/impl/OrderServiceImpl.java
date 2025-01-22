package microservice.order.services.impl;

import jakarta.persistence.EntityNotFoundException;
import microservice.order.dtos.OrderDTO.*;
import microservice.order.enums.Status;
import microservice.order.mappers.OrderMapper;
import microservice.order.models.Order;
import microservice.order.models.OrderItem;
import microservice.order.repositories.OrderRepository;
import microservice.order.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {

        Order order = new Order();
        order.setUserId(orderRequestDTO.userId());
        order.setStatus(Status.PENDING);

        List<OrderItem> orderItems = orderRequestDTO.orders().stream()
                .map(orderItemRequestDTO -> {
                    OrderItem orderItem = new OrderItem(
                            orderItemRequestDTO.productId(),
                            orderItemRequestDTO.quantity()
                    );
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItemList(orderItems);
        orderRepository.save(order);
        return orderMapper.toResponseDTO(order);
    }

    @Override
    public OrderUpdateDTO updateOrderStatus(OrderUpdateDTO orderUpdateDTO) {
        Order order = orderRepository.findById(orderUpdateDTO.id())
                .orElseThrow(() -> new EntityNotFoundException("El ID del orden no fue encontrado"));
        if(orderUpdateDTO.status() != null) {
            order.setStatus(orderUpdateDTO.status());
        }
        orderRepository.save(order);
        return orderMapper.toUpdateDTO(order);
    }

    @Override
    public Set<OrderResponseDTO> getAllOrders() {
        List<Order> orderList = orderRepository.findAll();
        return orderMapper.toResponseSetDTO(orderList);
    }
}