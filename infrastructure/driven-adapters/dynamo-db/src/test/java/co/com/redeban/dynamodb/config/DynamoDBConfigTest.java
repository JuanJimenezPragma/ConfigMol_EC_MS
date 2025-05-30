package co.com.redeban.dynamodb.config;

import co.com.redeban.dynamodb.DynamoDBParameterBankAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.metrics.MetricPublisher;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class DynamoDBConfigTest {

    @Mock
    private MetricPublisher publisher;

    @Mock
    private DynamoDbAsyncClient dynamoDbAsyncClient;

    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private ObjectMapper objectMapper;

    private final DynamoDBConfig dynamoDBConfig = new DynamoDBConfig();

    @Test
    void testAmazonDynamoDB() {

        DynamoDbAsyncClient result = dynamoDBConfig.amazonDynamoDB(
                "http://aws.dynamo.test",
                "region",
                publisher);

        assertNotNull(result);
    }

    @Test
    void testAmazonDynamoDBAsync() {

        DynamoDbAsyncClient result = dynamoDBConfig.amazonDynamoDBAsync(
                publisher,
                "region");

        assertNotNull(result);
    }


    @Test
    void testGetDynamoDbEnhancedAsyncClient() {
        DynamoDbEnhancedAsyncClient result = dynamoDBConfig.getDynamoDbEnhancedAsyncClient(dynamoDbAsyncClient);

        assertNotNull(result);
    }

    @Test
    void testGetDynamoParameterBankAdapter(){
        DynamoDBParameterBankAdapter result = dynamoDBConfig.dynamoDBParameterBankAdapter(dynamoDbEnhancedAsyncClient, dynamoDbAsyncClient, objectMapper, "table_name");
        assertNotNull(result);
    }
}
