package co.com.redeban.dynamodb;

import co.com.redeban.dynamodb.helper.ConfigMolAdapterOperations;
import co.com.redeban.model.configmol.ConfigMol;
import co.com.redeban.model.configmol.gateways.ConfigMolRepository;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;


public class DynamoDBConfigMolAdapter extends ConfigMolAdapterOperations<ConfigMol, String, ConfigMolEntity> implements ConfigMolRepository {

    public DynamoDBConfigMolAdapter(DynamoDbEnhancedAsyncClient connectionFactory, DynamoDbAsyncClient dynamoDbAsyncClient, ObjectMapper mapper, String tableName) {
        super(connectionFactory, mapper, d -> mapper.map(d, ConfigMol.class), tableName);
    }

    @Override
    public Mono<ConfigMol> findByModuleName(String moduleName) {
        return getWithConsistency(moduleName);
    }
}
