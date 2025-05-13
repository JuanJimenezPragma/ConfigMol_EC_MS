package co.com.redeban.dynamodb.helper;

import co.com.redeban.dynamodb.DynamoDBParameterBankAdapter;
import co.com.redeban.dynamodb.ParameterBankEntity;
import co.com.redeban.model.parameterbank.ParameterBank;
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

class ParameterBankAdapterOperationsTest {

    @Mock
    private DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;

    @Mock
    private DynamoDbAsyncTable<ParameterBankEntity> customerTable;

    @Mock
    private ObjectMapper mapper;

    private DynamoDBParameterBankAdapter adapter;

    private ParameterBankEntity parameterBankEntity;
    private ParameterBank parameterBank;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        parameterBankEntity = new ParameterBankEntity();
        parameterBankEntity.setFiid("1234");
        parameterBankEntity.setType("P2P#Transaction");
        parameterBankEntity.setDenialLimitSurpassed("false");

        parameterBank = ParameterBank.builder()
                .fiid("1234")
                .type("P2P#Transaction")
                .denialLimitSurpassed("false")
                .build();

        when(mapper.map(parameterBankEntity, ParameterBank.class)).thenReturn(parameterBank);

        adapter = spy(new DynamoDBParameterBankAdapter(
                dynamoDbEnhancedAsyncClient,
                mock(software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient.class),
                mapper,
                "table_name"
        ));
    }

    @Test
    void shouldReturnParameterBankWhenExists() {
        String fiid = "1234";
        String type = "P2P#Transaction";

        doReturn(Mono.just(parameterBank))
                .when(adapter).getWithConsistency(fiid, type);

        StepVerifier.create(adapter.getParameterBank(fiid, type))
                .expectNextMatches(param -> param.getFiid().equals(fiid) && param.getType().equals(type))
                .verifyComplete();

        verify(adapter).getWithConsistency(fiid, type);
    }

    @Test
    void entityGetterTest(){
        ParameterBankEntity parameterBankEntity2 = new ParameterBankEntity("1234", "P2P#Transaction");
        parameterBankEntity2.setDenialLimitSurpassed("false");
        assert parameterBankEntity2.getFiid().equals(parameterBank.getFiid());
        assert parameterBankEntity2.getType().equals(parameterBank.getType());
        assert parameterBankEntity2.getDenialLimitSurpassed().equals(parameterBank.getDenialLimitSurpassed());
    }
}
