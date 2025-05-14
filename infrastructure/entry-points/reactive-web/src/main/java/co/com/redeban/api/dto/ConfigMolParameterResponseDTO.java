package co.com.redeban.api.dto;

import co.com.redeban.model.configmol.ConfigMol;
import co.com.redeban.model.parameterbank.ParameterBank;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response that includes ConfigMol, ParameterBank and optional error details")
public class ConfigMolParameterResponseDTO {

    @Schema(description = "Configuration for MOL", nullable = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ConfigMol configMol;

    @Schema(description = "Parameter bank information if MOL is active", nullable = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ParameterBank parameterBank;

    @Schema(description = "Error code returned when an error occurs", example = "C409-000", nullable = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String codError;

    @Schema(description = "Short error message", example = "Validation failed", nullable = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    @Schema(description = "Detailed error message", example = "No configuration found for fiid=XXX and module=YYY", nullable = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String detailError;

}
