package co.com.redeban.dynamodb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class ParameterBankEntity {

    private String fiid;
    private String type;
    private String denialLimitSurpassed;

    public ParameterBankEntity() {
    }

    public ParameterBankEntity(String fiid, String type) {
        this.fiid = fiid;
        this.type = type;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("fiid")
    public String getFiid() {
        return fiid;
    }

    public void setFiid(String fiid) {
        this.fiid = fiid;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @DynamoDbAttribute("denialLimitSurpassed")
    public String getDenialLimitSurpassed() {
        return denialLimitSurpassed;
    }

    public void setDenialLimitSurpassed(String denialLimitSurpassed) {
        this.denialLimitSurpassed = denialLimitSurpassed;
    }
}
