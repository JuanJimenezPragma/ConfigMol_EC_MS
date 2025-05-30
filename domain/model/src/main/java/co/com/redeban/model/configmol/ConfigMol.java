package co.com.redeban.model.configmol;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ConfigMol {
    private String moduleName;
    private boolean isDenial;
    private boolean isMol;
}
