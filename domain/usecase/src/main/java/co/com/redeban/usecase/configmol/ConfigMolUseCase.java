package co.com.redeban.usecase.configmol;

import co.com.redeban.model.cache.CacheGateway;
import co.com.redeban.model.configmol.ConfigMol;
import co.com.redeban.model.configmol.gateways.ConfigMolRepository;
import co.com.redeban.model.parameterbank.ParameterBank;
import co.com.redeban.model.parameterbank.gateways.ParameterBankRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Optional;

import static co.com.redeban.usecase.util.Constants.TYPE_TRANSACTIONAL;

@RequiredArgsConstructor
public class ConfigMolUseCase {
    private final ConfigMolRepository configMolRepository;
    private final ParameterBankRepository parameterBankRepository;
    private final CacheGateway cacheGateway;


    public Mono<ConfigMol> getConfigMol(String moduleName) {
        return configMolRepository.findByModuleName(moduleName);
    }

    public Mono<Tuple2<ConfigMol, ParameterBank>> getConfigMolParameter(String fiid, String modulo) {
        Mono<ConfigMol> configMolMono = Mono.defer(() -> {
            Optional<ConfigMol> cached = cacheGateway.get(modulo);
            return cached.<Mono<? extends ConfigMol>>map(Mono::just).orElseGet(() -> configMolRepository.findByModuleName(modulo)
                    .doOnNext(configMol -> cacheGateway.put(modulo, configMol)));
        });
        Mono<ParameterBank> parameterBankMono = parameterBankRepository.getParameterBank(fiid, modulo.concat(TYPE_TRANSACTIONAL));
        return Mono.zip(configMolMono, parameterBankMono);
    }
}
