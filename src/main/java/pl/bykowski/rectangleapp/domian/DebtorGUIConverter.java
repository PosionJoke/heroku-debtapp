package pl.bykowski.rectangleapp.domian;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

@Configuration
public class DebtorGUIConverter {

    public String convertTextFileToString(TextField textField){
        return textField.getValue();
    }

    public float convertTextFieldToFloat(TextField textField){
        if (textField.isEmpty()) return 0;
        else
        return Float.parseFloat(textField.getValue());
    }

    public Long convertTextFIeldToLong(TextField textField){
        if (textField.isEmpty()) return 0L;
        else
        return Long.parseLong(textField.getValue());
    }
}
