package pl.bykowski.rectangleapp.form;

import lombok.Data;

@Data
public class DebtorGUIForm {
    private String textFieldName;
    private float textFieldDebt;
    private String textFieldReasonForTheDebt;
    private Long textFieldIdDebt = 0L;
}
