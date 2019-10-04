package pl.bykowski.rectangleapp.data_privider_wip;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.bykowski.rectangleapp.model.Debtor;

import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(JUnitParamsRunner.class)
public class dataProviderWIP2
{
//    @SuppressWarnings("unused")
    private Collection validfrobAndGorpValues() {
        return ImmutableList.of(
                new Object[] {"string1", 111},
                new Object[] {"string2", 222}
        );
    }

    @Test
    @Parameters(method = "validfrobAndGorpValues")
    public void whenGivenFrobString_thenGorpIsCorrect(
            String frobString,
            int expectedGorpValue
    ) {
        Debtor debtor = new Debtor();
        debtor.setName(frobString);
        assertThat(debtor.getName()).isEqualTo(frobString);
    }
}