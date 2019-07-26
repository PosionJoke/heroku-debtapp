package pl.bykowski.rectangleapp.form;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class DebtorGUIForm {
    private String textFieldName;
    private float textFieldDebt;
    private String textFieldReasonForTheDebt;
    private Long textFieldIdDebt;
}
