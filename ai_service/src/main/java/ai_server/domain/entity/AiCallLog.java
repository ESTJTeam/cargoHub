package ai_server.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "p_ai_call_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiCallLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ai_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "ai_provider")
    private String aiProvider;

    @Column(name = "model")
    private String model;

    @Column(name = "prompt", columnDefinition = "text")
    private String prompt;

    @Column(name = "output_text", columnDefinition = "text")
    private String outputText;

    protected AiCallLog(String aiProvider, String model, String prompt,
        String outputText) {

        this.aiProvider = aiProvider;
        this.model = model;
        this.prompt = prompt;
        this.outputText = outputText;
    }

    public static AiCallLog of(String aiProvider, String model, String prompt,
        String outputText) {

        return new AiCallLog(aiProvider, model, prompt, outputText);
    }
}
