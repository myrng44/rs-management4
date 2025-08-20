package ck4.nvb.rsmanagement.base.web.controller.api.response;

import ck4.nvb.rsmanagement.base.web.error.FieldError;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidationErrorResponse {
    private List<FieldError> fieldErrors;
    private String objectName;
    private int errorCount;

    public ValidationErrorResponse(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
        this.errorCount = fieldErrors.size();
    }

    public ValidationErrorResponse(List<FieldError> fieldErrors, String objectName) {
        this.fieldErrors = fieldErrors;
        this.objectName = objectName;
        this.errorCount = fieldErrors.size();
    }
}
