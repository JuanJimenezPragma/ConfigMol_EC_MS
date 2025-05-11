package co.com.redeban.api.dto;

//import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigMolParameterRequestDTO {

    //@NotBlank(message = "fiid is required")
    private String fiid;

    //@NotBlank(message = "module is required")
    private String module;

    //@NotBlank(message = "molActive is required")
    private boolean molActive;

}
