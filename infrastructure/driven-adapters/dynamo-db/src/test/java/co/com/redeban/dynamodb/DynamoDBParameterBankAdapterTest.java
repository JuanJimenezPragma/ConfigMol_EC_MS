package co.com.redeban.dynamodb;

import co.com.redeban.model.parameterbank.ParameterBank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

import static org.mockito.Mockito.*;

class DynamoDBParameterBankAdapterTest {

    private DynamoDbEnhancedAsyncClient enhancedClient;
    private ObjectMapper mapper;
    private DynamoDBParameterBankAdapter adapter;

    @BeforeEach
    void setUp() {
        enhancedClient = mock(DynamoDbEnhancedAsyncClient.class);
        mapper = mock(ObjectMapper.class);

        adapter = Mockito.spy(new DynamoDBParameterBankAdapter(enhancedClient, mapper, "test-table"));
    }

    @Test
    void testGetParameterConfiguration() {
        String fiid = "7546";
        String type = "P2P";
        ParameterBank parameter = ParameterBank.builder()
                .fiid("7546")
                .type("P2P")
                .denialLimitSurpassed("false")
                .build();

        ParameterBank parameterBank = new ParameterBank(fiid, type, "false");

        doReturn(Mono.just(parameter)).when(adapter).getWithConsistency(fiid,type);

        StepVerifier.create(adapter.getParameterBank(fiid,type))
                .expectNextMatches(param -> param.getFiid().equals(fiid))
                .verifyComplete();

        assert parameterBank.getFiid().equals(parameter.getFiid());
        assert parameterBank.getType().equals(parameter.getType());
        assert parameterBank.getDenialLimitSurpassed().equals(parameter.getDenialLimitSurpassed());

        verify(adapter).getWithConsistency(fiid,type);
    }

}
