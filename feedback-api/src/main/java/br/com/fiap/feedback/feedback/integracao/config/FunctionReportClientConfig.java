
package br.com.fiap.feedback.feedback.integracao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.signer.Aws4Signer;
import software.amazon.awssdk.auth.signer.params.Aws4SignerParams;
import software.amazon.awssdk.http.SdkHttpFullRequest;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.regions.Region;

@Configuration
public class FunctionReportClientConfig {

    @Bean(destroyMethod = "close")
    public DefaultCredentialsProvider functionReportCredentialsProvider() {
        return DefaultCredentialsProvider.create();
    }

    @Bean
    public RestClient functionReportRestClient(
            @Value("${app.clients.function-report.url}") String url,
            @Value("${app.clients.function-report.internal-token}") String internalToken,
            DefaultCredentialsProvider credentialsProvider) {

        Aws4Signer signer = Aws4Signer.create();

        ClientHttpRequestInterceptor sigV4Interceptor =
                (request, body, execution) -> {

                    var sdkRequestBuilder = SdkHttpFullRequest.builder()
                            .uri(request.getURI())
                            .method(SdkHttpMethod.fromValue(
                                    request.getMethod().name()
                            ));

                    request.getHeaders().forEach((name, values) ->
                            sdkRequestBuilder.putHeader(name, values)
                    );

                    var signedRequest = signer.sign(
                            sdkRequestBuilder.build(),
                            Aws4SignerParams.builder()
                                    .awsCredentials(
                                            credentialsProvider.resolveCredentials()
                                    )
                                    .signingName("execute-api")
                                    .signingRegion(Region.US_EAST_2)
                                    .build()
                    );

                    signedRequest.headers().forEach((name, values) -> {
                        if (!name.equalsIgnoreCase("host")) {
                            request.getHeaders().put(name, values);
                        }
                    });

                    return execution.execute(request, body);
                };

        return RestClient.builder()
                .baseUrl(url)
                .defaultHeader("X-Internal-Service-Token", internalToken)
                .requestInterceptor(sigV4Interceptor)
                .build();
    }
}
