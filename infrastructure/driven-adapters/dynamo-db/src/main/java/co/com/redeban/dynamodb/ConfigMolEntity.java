package co.com.redeban.dynamodb;


import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class ConfigMolEntity {

    private String moduleName;
    private boolean isDenial;
    private boolean isMol;

    public ConfigMolEntity() {
    }

    public ConfigMolEntity(String moduleName, boolean isDenial, boolean isMol) {
        this.moduleName = moduleName;
        this.isDenial = isDenial;
        this.isMol = isMol;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("module_name")
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @DynamoDbAttribute("isDenial")
    public boolean isDenial() {
        return isDenial;
    }

    public void setDenial(boolean denial) {
        isDenial = denial;
    }

    @DynamoDbAttribute("isMol")
    public boolean isMol() {
        return isMol;
    }

    public void setMol(boolean mol) {
        isMol = mol;
    }
}
