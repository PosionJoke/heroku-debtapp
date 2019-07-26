package pl.bykowski.rectangleapp.domian;

import com.vaadin.flow.data.binder.ErrorMessageProvider;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.StringToFloatConverter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebtorGUIFloatConverter extends StringToFloatConverter {

    public DebtorGUIFloatConverter(String errorMessage) {
        super(errorMessage);
    }

    public DebtorGUIFloatConverter(Float emptyValue, String errorMessage) {
        super(emptyValue, errorMessage);
    }

    public DebtorGUIFloatConverter(ErrorMessageProvider errorMessageProvider) {
        super(errorMessageProvider);
    }

    public DebtorGUIFloatConverter(Float emptyValue, ErrorMessageProvider errorMessageProvider) {
        super(emptyValue, errorMessageProvider);
    }

    @Override
    public Result<Float> convertToModel(String s, ValueContext valueContext) {
        Float result = Float.parseFloat(s);
        return Result.ok(result);
    }

    @Override
    public String convertToPresentation(Float aFloat, ValueContext valueContext) {
        return null;
    }
}
