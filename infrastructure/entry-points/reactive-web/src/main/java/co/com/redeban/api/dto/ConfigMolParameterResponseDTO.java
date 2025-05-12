package co.com.redeban.api.dto;

import co.com.redeban.model.configmol.ConfigMol;
import co.com.redeban.model.parameterbank.ParameterBank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConfigMolParameterResponseDTO {
    private ConfigMol configMol;
    private ParameterBank parameterBank;
    private String codError;
    private String error;
    private String detailError;

}
