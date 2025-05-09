package co.com.redeban.model.parameterbank.gateways;

import co.com.redeban.model.parameterbank.ParameterBank;
import reactor.core.publisher.Flux;

public interface ParameterBankRepository {
    Flux<ParameterBank> getParameterBank(String fiid);
}
