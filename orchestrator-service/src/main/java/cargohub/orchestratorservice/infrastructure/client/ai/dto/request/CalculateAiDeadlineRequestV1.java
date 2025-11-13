package cargohub.orchestratorservice.infrastructure.client.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class CalculateAiDeadlineRequestV1 {

    @NotNull
    private UUID orderNum;

//    @NotBlank
//    private String requesterName;
//
//    @NotBlank
//    @Email
//    private String requesterEmail;

    @NotNull
    private LocalDateTime orderAt;

    @NotBlank
    private String productName;

    @NotNull
    private int quantity;

    @Size(max = 100)
    private String requestNote;

    @NotBlank
    private String shipFromHub;

    @NotBlank
    private String destination;


    @Builder
    private CalculateAiDeadlineRequestV1(
        UUID orderNum,
        LocalDateTime orderAt,
        String productName,
        int quantity,
        String requestNote,
        String shipFromHub,
        String destination
    ) {
        this.orderNum = orderNum;
        this.orderAt = orderAt;
        this.productName = productName;
        this.quantity = quantity;
        this.requestNote = requestNote;
        this.shipFromHub = shipFromHub;
        this.destination = destination;
    }
}
