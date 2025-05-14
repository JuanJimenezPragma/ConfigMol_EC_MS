package co.com.redeban.dynamodb;

import co.com.redeban.model.configmol.ConfigMol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DynamoDBConfigMolAdapterTest {

    private DynamoDbEnhancedAsyncClient enhancedClient;
    private ObjectMapper mapper;
    private DynamoDBConfigMolAdapter adapter;


    @BeforeEach
    void setUp() {
        enhancedClient = mock(DynamoDbEnhancedAsyncClient.class);
        mapper = mock(ObjectMapper.class);

        adapter = Mockito.spy(new DynamoDBConfigMolAdapter(enhancedClient, mapper, "test-table"));
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

        assertEquals("test-module", configMolEntity.getModuleName());
        assertTrue(configMolEntity.isMol());
        assertFalse(configMolEntity.isDenial());
    }


}
