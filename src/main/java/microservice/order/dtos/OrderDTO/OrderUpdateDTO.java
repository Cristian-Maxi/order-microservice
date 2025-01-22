package microservice.order.dtos.OrderDTO;

import jakarta.validation.constraints.NotNull;
import microservice.order.enums.Status;

public record OrderUpdateDTO(
        @NotNull(message = "El id no debe ser nulo")
        Long id,
        @NotNull(message = "el status no debe ser nulo")
        Status status
) {
}
