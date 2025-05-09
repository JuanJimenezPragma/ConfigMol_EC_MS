package co.com.redeban.dynamodb;

import co.com.redeban.dynamodb.helper.ParameterBankAdapterOperations;
import co.com.redeban.model.parameterbank.ParameterBank;
import co.com.redeban.model.parameterbank.gateways.ParameterBankRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Slf4j
public class DynamoDBParameterBankAdapter extends ParameterBankAdapterOperations<ParameterBank, String, ParameterBankEntity> implements ParameterBankRepository {

    public DynamoDBParameterBankAdapter(DynamoDbEnhancedAsyncClient connectionFactory, DynamoDbAsyncClient dynamoDbAsyncClient, ObjectMapper mapper, String tableName) {
        super(connectionFactory, dynamoDbAsyncClient, mapper, d -> mapper.map(d, ParameterBank.class), tableName);
    }

    @Override
    public Flux<ParameterBank> getParameterBank(String fiid) {
        return getWithConsistencys(fiid);
    }
}
