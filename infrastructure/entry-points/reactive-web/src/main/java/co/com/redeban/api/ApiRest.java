package co.com.redeban.api;
import co.com.redeban.model.configmol.ConfigMol;
import co.com.redeban.model.parameterbank.ParameterBank;
import co.com.redeban.usecase.configmol.ConfigMolUseCase;
import co.com.redeban.usecase.parameterbank.ParameterBankUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/config-mol", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ApiRest {
    private final ConfigMolUseCase configMolUseCase;

    private final ParameterBankUseCase parameterBankUseCase;

    @GetMapping("{module}")
    public Mono<ConfigMol> getConfigMol(@PathVariable("module") String module) {
        return configMolUseCase.getConfigMol(module);
    }

    @GetMapping("/parameter/{fiid}")
    public Flux<ParameterBank> getParameterBank(@PathVariable("fiid") String fiid) {
        return parameterBankUseCase.getParameterBank(fiid);
    }


}


// validate fiid max 4, todos obligatorios
//QueryParam
//get 3 QueryParam
// fiid, modulo, flag

//flag cuando esta activo el mol no se va a la tabla parameter
//mol true y parameter true retorno parameter

//moll todo

//datos a retornar trans fiid type denialLimitSurpassed monto

// sin ohay info 409 y COD y message
// cod, message> "entidad no existe" dos en ingles, message

// Exception handler