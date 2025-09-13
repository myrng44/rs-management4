package ck4.nvb.rsmanagement.base.search.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchConfig {

    @Value("${spring.elasticsearch.uris:http://localhost:9201}")
    private String elasticsearchUrl;

    @Value("${spring.elasticsearch.username:}")
    private String username;

    @Value("${spring.elasticsearch.password:}")
    private String password;

    @Value("${rs.elasticsearch.ssl.enabled:false}")
    private boolean sslEnabled;

    @Value("${rs.elasticsearch.ssl.verification:false}")
    private boolean sslVerification;

    @Bean
    public RestClient restClient() {
        var builder = RestClient.builder(HttpHost.create(elasticsearchUrl));

        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            // Configure credentials
            if (!username.isEmpty() && !password.isEmpty()) {
                var credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(
                        AuthScope.ANY,
                        new UsernamePasswordCredentials(username, password)
                );
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }

            // Configure SSL for HTTPS connections
            if (elasticsearchUrl.startsWith("https://")) {
                try {
                    SSLContextBuilder sslBuilder = SSLContextBuilder.create();

                    if (!sslVerification) {
                        // Trust all certificates (Development only)
                        sslBuilder.loadTrustMaterial(null, (chain, authType) -> true);
                        httpClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
                    } else {
                        // For production: trust self-signed or configure proper truststore
                        sslBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
                    }

                    httpClientBuilder.setSSLContext(sslBuilder.build());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to configure SSL context", e);
                }
            }

            return httpClientBuilder;
        });

        return builder.build();
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(RestClient restClient) {
        ElasticsearchTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );
        return new ElasticsearchClient(transport);
    }
}