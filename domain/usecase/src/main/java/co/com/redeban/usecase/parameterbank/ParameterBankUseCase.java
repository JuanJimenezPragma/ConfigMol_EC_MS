package co.com.redeban.usecase.parameterbank;

import co.com.redeban.model.parameterbank.ParameterBank;
import co.com.redeban.model.parameterbank.gateways.ParameterBankRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ParameterBankUseCase {
    private final ParameterBankRepository parameterBankRepository;

    public Flux<ParameterBank> getParameterBank(String fiid) {
        return parameterBankRepository.getParameterBank(fiid);
    }
}