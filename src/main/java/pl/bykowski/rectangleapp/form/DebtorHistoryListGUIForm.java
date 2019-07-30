package pl.bykowski.rectangleapp.form;

import lombok.Data;

@Data
public class DebtorHistoryListGUIForm {
    private String debtorNameField;
    private float newValueField;
    private Long idToDeleteTextField = 0L;
}
