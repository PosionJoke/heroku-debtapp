package pl.bykowski.rectangleapp.form;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

import javax.persistence.Entity;

@Data
@Configuration
public class DebtorGUIForm {
    private String textFieldName;
    private float textFieldDebt;
    private String textFieldReasonForTheDebt;
    private Long textFieldIdDebt;
}
