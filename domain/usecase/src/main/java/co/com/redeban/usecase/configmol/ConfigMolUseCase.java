package co.com.redeban.usecase.configmol;

import co.com.redeban.model.configmol.ConfigMol;
import co.com.redeban.model.configmol.gateways.ConfigMolRepository;
import co.com.redeban.model.parameterbank.ParameterBank;
import co.com.redeban.model.parameterbank.gateways.ParameterBankRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import static co.com.redeban.usecase.util.Constants.TYPE_TRANSACTIONAL;

@RequiredArgsConstructor
public class ConfigMolUseCase {
    private final ConfigMolRepository configMolRepository;
    private final ParameterBankRepository parameterBankRepository;


    public Mono<ConfigMol> getConfigMol(String moduleName) {
        return configMolRepository.findByModuleName(moduleName);
    }

    public Mono<Tuple2<ConfigMol, ParameterBank>> getConfigMolParameter(String fiid, String modulo) {
        Mono<ConfigMol> configMolMono = configMolRepository.findByModuleName(modulo);
        Mono<ParameterBank> parameterBankMono = parameterBankRepository.getParameterBank(fiid, modulo.concat(TYPE_TRANSACTIONAL));
        return Mono.zip(configMolMono, parameterBankMono);
    }
}
