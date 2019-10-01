package pl.bykowski.rectangleapp.data_privider_wip;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


//@RunWith(Parameterized.class)
public class dataProviderWIP1 {
    @DataProvider(name = "data-provider")
    public Object[][] dataProviderMethod() {
        return new Object[][] { { "data one" }, { "data two" } };
    }

    @Test(dataProvider = "data-provider")
    public void testMethod(String data) {
        System.out.println("Data is: " + data);
    }
}
