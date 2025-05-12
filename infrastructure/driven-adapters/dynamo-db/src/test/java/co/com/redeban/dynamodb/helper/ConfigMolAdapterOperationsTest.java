package co.com.redeban.dynamodb.helper;

import co.com.redeban.dynamodb.ConfigMolEntity;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import static org.mockito.Mockito.when;

class ConfigMolAdapterOperationsTest {

    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private DynamoDbAsyncTable<ConfigMolEntity> customerTable;

    private ConfigMolEntity configMolEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(dynamoDbEnhancedAsyncClient.table("table_name", TableSchema.fromBean(ConfigMolEntity.class)))
                .thenReturn(customerTable);

        configMolEntity = new ConfigMolEntity();
        configMolEntity.setModuleName("moduleName");
    }

}