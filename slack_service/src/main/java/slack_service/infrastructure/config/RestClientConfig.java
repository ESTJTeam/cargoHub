package slack_service.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(
        RestClient.Builder builder,
        @Value("${spring.slack.bot-token}") String slackBotToken
    ) {
        return builder
            .defaultHeader("Authorization", "Bearer " + slackBotToken)
            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
}
