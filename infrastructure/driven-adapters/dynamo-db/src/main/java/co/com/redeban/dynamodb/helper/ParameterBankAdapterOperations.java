package co.com.redeban.dynamodb.helper;

import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.CancellationReason;
import software.amazon.awssdk.services.dynamodb.model.TransactionCanceledException;

import java.lang.reflect.ParameterizedType;
import java.util.List;
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

    public Mono<E> save(E model) {
        return Mono.fromFuture(table.putItem(toEntity(model))).thenReturn(model);
    }

    public Mono<E> getById(K id) {
        return Mono.fromFuture(table.getItem(Key.builder()
                        .partitionValue(AttributeValue.builder().s((String) id).build())
                        .build()))
                .map(this::toModel);
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

    public Mono<E> delete(E model) {
        return Mono.fromFuture(table.deleteItem(toEntity(model))).map(this::toModel);
    }

    protected V toEntity(E model) {
        return mapper.map(model, dataClass);
    }

    protected E toModel(V data) {
        return data != null ? toEntityFn.apply(data) : null;
    }


    private boolean isTransactionConflict(Throwable throwable) {
        if (throwable instanceof TransactionCanceledException tce) {
            List<CancellationReason> reasons = tce.cancellationReasons();
            if (reasons != null) {
                return reasons.stream()
                        .anyMatch(reason -> "TransactionConflict".equals(reason.code()));
            }
        }
        return false;
    }

}
