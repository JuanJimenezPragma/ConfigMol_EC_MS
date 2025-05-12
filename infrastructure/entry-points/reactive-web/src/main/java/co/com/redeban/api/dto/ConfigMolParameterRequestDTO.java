package co.com.redeban.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigMolParameterRequestDTO {

    @NotBlank(message = "parameter 'fiid' is required")
    @Size(min = 4, max = 4, message = "fiid must be exactly 4 characters")
    private String fiid;

    @NotBlank(message = "parameter 'module' is required")
    private String module;

    @NotNull(message = "molActive must be provided")
    private Boolean molActive;

}
