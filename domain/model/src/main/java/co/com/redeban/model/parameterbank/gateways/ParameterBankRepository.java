package co.com.redeban.model.parameterbank.gateways;

import co.com.redeban.model.parameterbank.ParameterBank;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ParameterBankRepository {
    Flux<ParameterBank> getParameterBank(String fiid);
    Mono<ParameterBank> getParameterBank(String fiid, String type);
}
