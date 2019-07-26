package pl.bykowski.rectangleapp.domian;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

@Configuration
public class DebtorGUIFloatConverter implements Converter<String, Float> {

    @Override
    public Result<Float> convertToModel(String s, ValueContext valueContext) {
        Float result = Float.parseFloat(s);

        return Result.ok(result);
    }

    @Override
    public String convertToPresentation(Float aFloat, ValueContext valueContext) {
        return Float.toString(aFloat);
    }
}
