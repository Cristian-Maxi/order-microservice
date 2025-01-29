package microservice.order.services.impl;

import jakarta.persistence.EntityNotFoundException;
import microservice.order.config.RabbitMQConfig;
import microservice.order.dtos.OrderDTO.*;
import microservice.order.dtos.OrderItemDTO.OrderItemRequestDTO;
import microservice.order.dtos.ProductDTO.ProductDTO;
import microservice.order.enums.Status;
import microservice.order.mappers.OrderMapper;
import microservice.order.models.Order;
import microservice.order.models.OrderItem;
import microservice.order.repositories.OrderRepository;
import microservice.order.services.OrderService;
import microservice.order.utils.OrderCreatedEvent;
import microservice.order.utils.ProductClient;
import microservice.order.utils.UserClient;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {

        Long userId = userClient.validateUserEmail(orderRequestDTO.email());

        productClient.reserveStock(orderRequestDTO.orders());

        Order order = buildOrder(userId);

        Set<OrderItem> orderItems = createOrderItems(order, orderRequestDTO.orders());

        order.setOrderItemList(orderItems);

        Order savedOrder = orderRepository.save(order);

        publishOrderCreatedEvent(savedOrder, orderItems, orderRequestDTO.email());

        return orderMapper.toResponseDTO(savedOrder);
    }

    private Order buildOrder(Long userId) {
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(Status.PENDING);
        return order;
    }

    private Set<OrderItem> createOrderItems(Order order, Set<OrderItemRequestDTO> orderItemRequestDTOs) {
        return orderItemRequestDTOs.stream()
                .map(orderItemRequestDTO -> {
                    OrderItem orderItem = new OrderItem(
                            orderItemRequestDTO.productId(),
                            orderItemRequestDTO.quantity()
                    );
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toSet());
    }

//    private void publishOrderCreatedEvent(Order savedOrder, Set<OrderItem> orderItems, String email) {
//        // Obtener los nombres de los productos del microservicio de productos
//        Map<Long, String> productNames = productClient.getProductNames(
//                orderItems.stream().map(OrderItem::getProductId).collect(Collectors.toSet())
//        );
//
//        OrderCreatedEvent event = new OrderCreatedEvent(
//                savedOrder.getId(),
//                savedOrder.getUserId(),
//                orderItems.stream()
//                        .map(item -> new OrderCreatedEvent.OrderItemDetails(
//                                item.getProductId(),
//                                productNames.getOrDefault(item.getProductId(), "Desconocido"),
//                                item.getQuantity()))
//                        .collect(Collectors.toList())
//        );
//
//        event.setEmail(email);
//        amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, event);
//    }

    private void publishOrderCreatedEvent(Order savedOrder, Set<OrderItem> orderItems, String email) {
        Set<Long> productIds = orderItems.stream().map(OrderItem::getProductId).collect(Collectors.toSet());

        Map<Long, ProductDTO> productDetails = productClient.getProductDetails(productIds);

        List<OrderCreatedEvent.OrderItemDetails> orderItemDetails = orderItems.stream()
                .map(item -> {
                    ProductDTO product = productDetails.get(item.getProductId());
                    return new OrderCreatedEvent.OrderItemDetails(
                            item.getProductId(),
                            product != null ? product.name() : "Unknown",
                            product != null ? product.description() : "No description",
                            product != null ? product.price() : 0.0,
                            product != null ? product.stock() : 0,
                            item.getQuantity()
                    );
                })
                .collect(Collectors.toList());

        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getUserId(),
                orderItemDetails
        );
        event.setEmail(email);

        amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, event);
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