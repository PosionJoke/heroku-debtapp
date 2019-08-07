package pl.bykowski.rectangleapp.form;

import com.vaadin.flow.component.textfield.TextArea;
import lombok.Data;

@Data
public class DebtorGUIForm {
    private String textFieldName;
    private float textFieldDebt;
    private String textFieldReasonForTheDebt;
    private Long textFieldIdDebt = 0L;
    private TextArea areaInfo = new TextArea();

    public String getValueOfAreaInfo() {
        return areaInfo.getValue();
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo.setValue(areaInfo);
    }
}
