package co.com.redeban.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigMolParameterRequestDTO {

    @NotBlank(message = "parameter 'fiid' is required")
    @Size(min = 4, max = 4, message = "fiid must be exactly 4 characters")
    private String fiid;

    @NotBlank(message = "parameter 'module' is required")
    private String module;

    private boolean molActive;

}
