package co.com.redeban.dynamodb.helper;

import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.lang.reflect.ParameterizedType;
import java.util.function.Function;

@Slf4j
public abstract class ParameterBankAdapterOperations<E, K, V> {
    private final Class<V> dataClass;
    private final Function<V, E> toEntityFn;
    protected ObjectMapper mapper;
    private final DynamoDbAsyncTable<V> table;
    private final DynamoDbAsyncClient dynamoDbAsyncClient;
    private final String tableName;

    @SuppressWarnings("unchecked")
    protected ParameterBankAdapterOperations(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient,
                                             DynamoDbAsyncClient dynamoDbAsyncClient,
                                             ObjectMapper mapper,
                                             Function<V, E> toEntityFn,
                                             String tableName
    ) {
        this.tableName = tableName;
        this.dynamoDbAsyncClient = dynamoDbAsyncClient;
        this.toEntityFn = toEntityFn;
        this.mapper = mapper;
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.dataClass = (Class<V>) genericSuperclass.getActualTypeArguments()[2];
        table = dynamoDbEnhancedAsyncClient.table(tableName, TableSchema.fromBean(dataClass));
    }

    public Mono<E> getWithConsistency(String partition, String sort) {
        GetItemEnhancedRequest request = GetItemEnhancedRequest.builder()
                .key(k -> k.partitionValue(AttributeValue.builder().s(partition).build()).sortValue(AttributeValue.builder().s(sort).build()))
                .consistentRead(true)
                .build();

        return Mono.fromFuture(table.getItem(request))
                .map(this::toModel);
    }

    public Flux<E> getWithConsistencys(String partitionKey) {
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(partitionKey)))
                .consistentRead(true)
                .build();

        return Flux.from(table.query(queryRequest))
                .flatMap(page -> Flux.fromIterable(page.items()))
                .map(this::toModel);
    }

    protected E toModel(V data) {
        return data != null ? toEntityFn.apply(data) : null;
    }


}
