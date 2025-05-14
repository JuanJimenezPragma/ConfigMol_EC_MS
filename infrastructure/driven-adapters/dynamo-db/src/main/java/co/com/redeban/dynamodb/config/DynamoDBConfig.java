package co.com.redeban.dynamodb.config;

import co.com.redeban.dynamodb.DynamoDBConfigMolAdapter;
import co.com.redeban.dynamodb.DynamoDBParameterBankAdapter;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.metrics.MetricPublisher;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.net.URI;

@Configuration
public class DynamoDBConfig {

    @Bean
    @Profile({"local"})
    public DynamoDbAsyncClient amazonDynamoDB(@Value("${aws.dynamodb.endpoint}") String endpoint,
                                              @Value("${REGION}") String region,
                                              MetricPublisher publisher) {
        return DynamoDbAsyncClient.builder()
                .credentialsProvider(getProviderChain())
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .overrideConfiguration(o -> o.addMetricPublisher(publisher))
                .build();
    }

    @Bean
    @Profile({"!local"})
    public DynamoDbAsyncClient amazonDynamoDBAsync(MetricPublisher publisher, @Value("${REGION}") String region) {
        return DynamoDbAsyncClient.builder()
                .credentialsProvider(getProviderChain())
                .region(Region.of(region))
                .overrideConfiguration(o -> o.addMetricPublisher(publisher))
                .build();
    }

    @Bean
    public DynamoDbEnhancedAsyncClient getDynamoDbEnhancedAsyncClient(DynamoDbAsyncClient client) {
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(client)
                .build();
    }

    @Bean
    public DynamoDBParameterBankAdapter dynamoDBParameterBankAdapter(DynamoDbEnhancedAsyncClient enhancedClient,
                                                                         DynamoDbAsyncClient dynamoDbAsyncClient,
                                                                         ObjectMapper objectMapper,
                                                                         @Value("${aws.dynamodb.tableNameParameter}") String tableName) {
        return new DynamoDBParameterBankAdapter(enhancedClient, objectMapper, tableName);
    }

    @Bean
    public DynamoDBConfigMolAdapter dynamoDBConfigMolAdapter(DynamoDbEnhancedAsyncClient enhancedClient,
                                                             DynamoDbAsyncClient dynamoDbAsyncClient,
                                                             ObjectMapper objectMapper,
                                                             @Value("${aws.dynamodb.tableNameConfigMol}") String tableName){
        return new DynamoDBConfigMolAdapter(enhancedClient,objectMapper,tableName);
    }

    private AwsCredentialsProviderChain getProviderChain() {
        return AwsCredentialsProviderChain.builder()
                .addCredentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .addCredentialsProvider(SystemPropertyCredentialsProvider.create())
                .addCredentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                .addCredentialsProvider(ProfileCredentialsProvider.create())
                .addCredentialsProvider(ContainerCredentialsProvider.builder().build())
                .addCredentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();
    }

}
