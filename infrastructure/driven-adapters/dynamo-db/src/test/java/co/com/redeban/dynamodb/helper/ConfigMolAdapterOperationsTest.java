package co.com.redeban.dynamodb.helper;

import co.com.redeban.dynamodb.DynamoDBConfigMolAdapter;
import co.com.redeban.dynamodb.ConfigMolEntity;
import co.com.redeban.model.configmol.ConfigMol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class ConfigMolAdapterOperationsTest {

    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private DynamoDbAsyncClient dynamoDbAsyncClient;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private DynamoDbAsyncTable<ConfigMolEntity> customerTable;

    private ConfigMolEntity configMolEntity;

    private ConfigMol configMol;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(dynamoDbEnhancedAsyncClient.table("table_name", TableSchema.fromBean(ConfigMolEntity.class)))
                .thenReturn(customerTable);

        configMolEntity = new ConfigMolEntity();
        configMolEntity.setModuleName("moduleName");
    }

    @Test
    void modelEntityPropertiesMustNotBeNull() {
        ConfigMolEntity configMolEntityUnderTest = new ConfigMolEntity("moduleName", true, true);

        assertNotNull(configMolEntityUnderTest.getModuleName());
    }

    @Test
    void testSave() {
        when(customerTable.putItem(configMolEntity)).thenReturn(CompletableFuture.runAsync(()->{}));
        when(mapper.map(configMolEntity, ConfigMolEntity.class)).thenReturn(configMolEntity);

        DynamoDBConfigMolAdapter dynamoDBConfigMolAdapter =
                new DynamoDBConfigMolAdapter(dynamoDbEnhancedAsyncClient,dynamoDbAsyncClient, mapper, "table_name");

        StepVerifier.create(dynamoDBConfigMolAdapter.save(configMol))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testGetById() {
        String id = "id";

        when(customerTable.getItem(
                Key.builder().partitionValue(AttributeValue.builder().s(id).build()).build()))
                .thenReturn(CompletableFuture.completedFuture(configMolEntity));
        when(mapper.map(configMolEntity, Object.class)).thenReturn("value");

        DynamoDBConfigMolAdapter dynamoDBConfigMolAdapter =
                new DynamoDBConfigMolAdapter(dynamoDbEnhancedAsyncClient,dynamoDbAsyncClient, mapper, "table_name");

        StepVerifier.create(dynamoDBConfigMolAdapter.getById("id"))
                .expectNext(configMol)
                .verifyComplete();
    }

    @Test
    void testDelete() {
        when(mapper.map(configMolEntity, ConfigMolEntity.class)).thenReturn(configMolEntity);
        when(mapper.map(configMolEntity, Object.class)).thenReturn("value");

        when(customerTable.deleteItem(configMolEntity))
                .thenReturn(CompletableFuture.completedFuture(configMolEntity));

        DynamoDBConfigMolAdapter dynamoDBConfigMolAdapter =
                new DynamoDBConfigMolAdapter(dynamoDbEnhancedAsyncClient,dynamoDbAsyncClient, mapper, "table_name");

        StepVerifier.create(dynamoDBConfigMolAdapter.delete(configMol))
                .expectNext(configMol)
                .verifyComplete();
    }
}