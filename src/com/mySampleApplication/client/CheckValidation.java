package com.mySampleApplication.client;

public interface CheckValidation {

    String VALIDATION_ERROR_STYLE = "error-text-box";

    String VALIDATION_ERROR_COMPONENT_STYLE = "error-component";

    String VALIDATION_ERROR_STYLE_LEFT = "error-text-box-left";

    String VALIDATION_ERROR_TOOLTIP = "error-text-tooltip-text";

    void setNotNull(boolean isNotNull);

    void setMinLength(int minLength);

    boolean validateMessage();

    void setInvalidateMessage(String message);

    void setFieldValidated();


}

