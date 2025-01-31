package microservice.order.controllers;

import jakarta.validation.Valid;
import microservice.order.dtos.ApiResponseDTO;
import microservice.order.dtos.OrderDTO.*;
import microservice.order.exceptions.ApplicationException;
import microservice.order.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        try {
            OrderResponseDTO orderResponseDTO = orderService.createOrder(orderRequestDTO);
            return new ResponseEntity<>(orderResponseDTO, HttpStatus.CREATED);
        } catch (ApplicationException e) {
            throw new ApplicationException("Ha ocurrido un error " + e.getMessage());
        }
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<ApiResponseDTO<OrderUpdateDTO>> updateStatus(@Valid @RequestBody OrderUpdateDTO orderUpdateDTO) {
        OrderUpdateDTO newStatus = orderService.updateOrderStatus(orderUpdateDTO);
        String message = "Status Actualizado";
        return new ResponseEntity<>(new ApiResponseDTO<>(true, message, newStatus), HttpStatus.OK);
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<ApiResponseDTO<OrderResponseDTO>> getAllOrders() {
        try {
            Set<OrderResponseDTO> userEntityResponseDTO = orderService.getAllOrders();
            if (userEntityResponseDTO.isEmpty()) {
                return new ResponseEntity<>(new ApiResponseDTO<>(false, "No hay ordenes guardados", userEntityResponseDTO), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponseDTO<>(true, "Ordenes guardados", userEntityResponseDTO), HttpStatus.OK);
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(" Ha ocurrido un error " + e.getMessage());
        }
    }
}
