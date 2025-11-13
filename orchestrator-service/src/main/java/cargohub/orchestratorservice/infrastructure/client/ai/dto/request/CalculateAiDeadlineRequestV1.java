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

//    private List<String> viaHubs;

    @NotBlank
    private String destination;

//    @NotBlank
//    private String handlerName;
//
//    @NotBlank
//    @Email
//    private String handlerEmail;

    @Builder
    private CalculateAiDeadlineRequestV1(
        UUID orderNum,
//        String requesterName,
//        String requesterEmail,
        LocalDateTime orderAt,
        String productName,
        int quantity,
        String requestNote,
        String shipFromHub,
//        List<String> viaHubs,
        String destination
//        String handlerName,
//        String handlerEmail
    ) {
        this.orderNum = orderNum;
//        this.requesterName = requesterName;
//        this.requesterEmail = requesterEmail;
        this.orderAt = orderAt;
        this.productName = productName;
        this.quantity = quantity;
        this.requestNote = requestNote;
        this.shipFromHub = shipFromHub;
//        this.viaHubs = (viaHubs == null ? List.of() : List.copyOf(viaHubs)); // 경유지 없을 땐 []
        this.destination = destination;
//        this.handlerName = handlerName;
//        this.handlerEmail = handlerEmail;
    }
}

/* TODO
 * 주석 처리된 미사용 코드 제거
 */