package br.com.fiap.feedback.feedback.integracao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class FunctionReportClientConfig {

    @Bean
    public RestClient functionReportRestClient(
            @Value("${app.clients.function-report.url}") String url,
            @Value("${app.clients.function-report.internal-token}") String internalToken) {

        return RestClient.builder()
                .baseUrl(url)
                .defaultHeader("X-Internal-Service-Token", internalToken)
                .build();
    }
}