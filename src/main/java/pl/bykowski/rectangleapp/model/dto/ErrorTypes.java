package pl.bykowski.rectangleapp.model.dto;

import java.util.Arrays;
import java.util.stream.Stream;

public enum ErrorTypes {
    error_403(403),error_404(404)
    ,error_500(500),error_503(503);

    ErrorTypes(Integer errorValue){
        this.errorValue = errorValue;
    }

    private Integer errorValue;

    public Integer getErrorValue() {
        return errorValue;
    }

    public static Stream<ErrorTypes> stream() {
        return Arrays.stream(ErrorTypes.values());
    }
}
