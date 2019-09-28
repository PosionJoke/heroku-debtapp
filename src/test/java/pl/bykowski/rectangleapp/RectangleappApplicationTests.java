package pl.bykowski.rectangleapp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.repositories.DebtorRepo;
import pl.bykowski.rectangleapp.services.DebtorService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class RectangleappApplicationTests {

    @MockBean
    private DebtorService debtorService;

    @MockBean
    private DebtorRepo debtorRepo;

    // write test cases here
    @Before
    public void setUp() {
        Debtor debtor = new Debtor();
        debtor.setName("alex");

        Mockito.when(debtorRepo.findByName(debtor.getName()))
                .thenReturn(java.util.Optional.of(debtor));
    }

    @Test
    public void whenValidName_thenEmployeeShouldBeFound() {
        String name = "alex";
        Optional<Debtor> found = debtorRepo.findByName(name);

        assertThat(found.get().getName())
                .isEqualTo(name);
    }
}
