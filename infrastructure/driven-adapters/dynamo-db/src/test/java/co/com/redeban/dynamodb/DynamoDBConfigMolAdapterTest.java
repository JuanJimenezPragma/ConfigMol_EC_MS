package co.com.redeban.dynamodb;

import co.com.redeban.model.configmol.ConfigMol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import static org.mockito.Mockito.*;
import static software.amazon.awssdk.profiles.ProfileFile.Type.CONFIGURATION;

class DynamoDBConfigMolAdapterTest {

    private DynamoDbEnhancedAsyncClient enhancedClient;
    private DynamoDbAsyncClient dynamoDbAsyncClient;
    private ObjectMapper mapper;
    private DynamoDBConfigMolAdapter adapter;


    @BeforeEach
    void setUp() {
        enhancedClient = mock(DynamoDbEnhancedAsyncClient.class);
        dynamoDbAsyncClient = mock(DynamoDbAsyncClient.class);
        mapper = mock(ObjectMapper.class);

        adapter = Mockito.spy(new DynamoDBConfigMolAdapter(enhancedClient, dynamoDbAsyncClient, mapper, "test-table"));
    }

    @Test
    void testGetParameterConfiguration() {
        String modulename = "test-module";
        ConfigMol parameter = ConfigMol.builder()
                .moduleName("test-module")
                .isMol(true)
                .isDenial(false)
                .build();

        ConfigMolEntity configMolEntity = new ConfigMolEntity(modulename, true, false);

        doReturn(Mono.just(parameter)).when(adapter).getWithConsistency(modulename);

        StepVerifier.create(adapter.findByModuleName(modulename))
                .expectNextMatches(param -> param.getModuleName().equals(modulename))
                .verifyComplete();

        assert configMolEntity.getModuleName().equals(parameter.getModuleName());
        assert !configMolEntity.isMol();
        assert configMolEntity.isDenial();

        verify(adapter).getWithConsistency(modulename);
    }

    @Test
    void testConfigMolEntity(){
        ConfigMolEntity configMolEntity = new ConfigMolEntity();
        configMolEntity.setModuleName("test-module");
        configMolEntity.setMol(true);
        configMolEntity.setDenial(false);

        assert configMolEntity.getModuleName().equals("test-module");
        assert configMolEntity.isMol();
        assert !configMolEntity.isDenial();
    }


}
