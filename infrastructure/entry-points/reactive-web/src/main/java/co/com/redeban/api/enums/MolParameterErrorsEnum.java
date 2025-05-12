package co.com.redeban.api.enums;

public enum MolParameterErrorsEnum {
    VALIDATION_ERROR("409", "Validation failed"),
    DATA_NOT_FOUND("404", "Data not found"),
    INTERNAL_ERROR("500", "Internal server error");

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
