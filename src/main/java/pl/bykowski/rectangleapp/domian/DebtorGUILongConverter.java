package pl.bykowski.rectangleapp.domian;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebtorGUILongConverter implements Converter<String, Long> {
    @Override
    public Result<Long> convertToModel(String s, ValueContext valueContext) {
        Long result = Long.parseLong(s);
        return Result.ok(result);
    }

    @Override
    public String convertToPresentation(Long aLong, ValueContext valueContext) {
        return Long.toString(aLong);
    }
}
