package co.com.redeban.model.parameterbank.gateways;

import co.com.redeban.model.parameterbank.ParameterBank;
import reactor.core.publisher.Mono;

public interface ParameterBankRepository {
    Mono<ParameterBank> getParameterBank(String fiid, String type);
}
