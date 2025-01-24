package microservice.order.dtos.OrderDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import microservice.order.dtos.OrderItemDTO.OrderItemRequestDTO;

import java.util.Set;

public record OrderRequestDTO(
        //Long userId,
        @NotBlank(message = "El email no debe estar en blanco")
        String email,
        @Valid @NotNull
        Set<OrderItemRequestDTO> orders
) {
}