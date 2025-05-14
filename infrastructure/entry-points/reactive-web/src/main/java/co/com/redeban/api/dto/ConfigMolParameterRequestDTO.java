package co.com.redeban.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request DTO to retrieve ConfigMol and optionally ParameterBank data")
public class ConfigMolParameterRequestDTO {

    @NotBlank(message = "parameter 'fiid' is required")
    @Size(min = 4, max = 4, message = "fiid must be exactly 4 characters")
    @Schema(description = "FIID (4-character identifier)", example = "1234", nullable = false)
    private String fiid;

    @NotBlank(message = "parameter 'module' is required")
    @Schema(description = "Module name to identify configuration", example = "MOL", nullable = false)
    private String module;

    @NotNull(message = "molActive must be provided")
    @Schema(description = "Flag to determine if ParameterBank should be returned", example = "true", nullable = false)
    private Boolean molActive;

}
