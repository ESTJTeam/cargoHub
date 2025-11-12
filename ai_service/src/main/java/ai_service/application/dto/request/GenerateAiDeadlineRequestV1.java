package ai_service.application.dto.request;

import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenerateAiDeadlineRequestV1 {

    public List<UUID> orderIdList;


}
