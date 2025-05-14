package co.com.redeban.api.enums;

public enum MolParameterErrorsEnum {
    VALIDATION_ERROR("C409-000", "Validation failed"),
    DATA_NOT_FOUND("B404-000", "Data not found"),
    INTERNAL_ERROR("I500-000", "Internal server error");

    private final String code;
    private final String message;

    MolParameterErrorsEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
