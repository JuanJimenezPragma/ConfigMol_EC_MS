package co.com.redeban.model.parameterbank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ParameterBank {
    private String fiid;
    private String type;
}
