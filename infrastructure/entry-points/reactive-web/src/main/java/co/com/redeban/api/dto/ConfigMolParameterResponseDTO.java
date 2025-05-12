package co.com.redeban.api.dto;

import co.com.redeban.model.configmol.ConfigMol;
import co.com.redeban.model.parameterbank.ParameterBank;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigMolParameterResponseDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ConfigMol configMol;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ParameterBank parameterBank;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String codError;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String detailError;

}
