package co.com.redeban.dynamodb.helper;

import co.com.redeban.dynamodb.ConfigMolEntity;
import co.com.redeban.dynamodb.DynamoDBConfigMolAdapter;
import co.com.redeban.model.configmol.ConfigMol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

import static org.mockito.Mockito.*;

class ConfigMolAdapterOperationsTest {

    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private DynamoDbAsyncTable<ConfigMolEntity> customerTable;

    @Mock
    private ObjectMapper mapper;

    private DynamoDBConfigMolAdapter adapter;

    private ConfigMol configMol;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ConfigMolEntity configMolEntity = new ConfigMolEntity();
        configMolEntity.setModuleName("test-module");
        configMolEntity.setMol(true);
        configMolEntity.setDenial(false);

        configMol = ConfigMol.builder()
                .moduleName("test-module")
                .isMol(true)
                .isDenial(false)
                .build();

        when(mapper.map(configMolEntity, ConfigMol.class)).thenReturn(configMol);

        adapter = spy(new DynamoDBConfigMolAdapter(
                dynamoDbEnhancedAsyncClient,
                mapper,
                "table_name"
        ));
    }

    @Test
    void shouldReturnConfigMolWhenExists() {
        String moduleName = "test-module";

        doReturn(Mono.just(configMol))
                .when(adapter).getWithConsistency(moduleName);

        StepVerifier.create(adapter.findByModuleName(moduleName))
                .expectNextMatches(param -> param.getModuleName().equals(moduleName))
                .verifyComplete();

        verify(adapter).getWithConsistency(moduleName);
    }



}