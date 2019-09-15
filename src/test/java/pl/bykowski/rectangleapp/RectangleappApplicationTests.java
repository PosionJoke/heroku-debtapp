package pl.bykowski.rectangleapp;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.bykowski.rectangleapp.model.Debtor;
import pl.bykowski.rectangleapp.model.DebtorDetails;
import pl.bykowski.rectangleapp.model.dto.DebtorDetailsDTO;
import pl.bykowski.rectangleapp.repositories.DebtorHistoryRepo;
import pl.bykowski.rectangleapp.repositories.DebtorRepo;
import pl.bykowski.rectangleapp.services.DebtorDetailsService;
import pl.bykowski.rectangleapp.services.DebtorHistoryService;
import pl.bykowski.rectangleapp.services.DebtorService;
import pl.bykowski.rectangleapp.services.UserService;


import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RectangleappApplicationTests {

	@Autowired
	private DebtorService debtorService;

	@MockBean
	private DebtorRepo debtorRepo;
	@MockBean
	private DebtorHistoryService debtorHistoryService;
	@MockBean
	private DebtorDetailsService debtorDetailsService;
	@MockBean
	private UserService userService;
	@MockBean
	private Principal principal;

	@Before
	public void setUp(){
		Debtor debtor = new Debtor();
		debtor.setName("Adrian");
		debtor.setUserName("UserName");

		Debtor debtor2 = new Debtor();
		debtor2.setName("Adrian2");
		debtor2.setUserName("UserName");

		Debtor debtor3 = new Debtor();
		debtor3.setName("Adrian3");
		debtor3.setUserName("UserName3");

		List<Debtor> debtorListByUserName = new ArrayList<>();
		debtorListByUserName.add(debtor);
		debtorListByUserName.add(debtor2);

		Mockito.when(debtorRepo.findByName(debtor.getName()))
				.thenReturn(debtor);
		Mockito.when(debtorRepo.findByUserName("UserName"))
				.thenReturn(debtorListByUserName);
	}

	@Test
	public void whenValidName_thenDebtorShouldBeFound() {
		String name = "Adrian";
		Debtor found = debtorService.findDebtorByName(name);

		assertThat(found.getName())
				.isEqualTo(name);
	}

	@Test
	public void whenValidUserName_ThenDebtorShouldBeFound(){
		String userName = "UserName";
		List<Debtor> debtorListFound = debtorService
				.findByUserName(userName);

		assertThat(debtorListFound.size())
				.isEqualTo(2);

		assertThat(debtorListFound.get(1).getName())
				.isEqualTo("Adrian2");
	}

	@Test
	public void whenDebtorNameIsValid_updateTotalDebt(){
		String debtorName = "Adrian";
		float debt = 100;
		String userName = "UserName";
		debtorService.updateTotalDebt(debtorName, debt, userName);

		assertThat(debtorService.findDebtorByName(debtorName).getTotalDebt())
				.isEqualTo(100);
	}

	@Test
	public void deleteDebtorDetailsUpdateTotalDebtMakeNewDebtorHistory(){
		Long id = 1L;
		DebtorDetails debtorDetails = new DebtorDetails();
		debtorDetails.setDebt(100);
		debtorDetails.setName("AdrianDetails");
		Debtor debtor = new Debtor();
		debtor.setName(debtorDetails.getName());

		Mockito.when(principal.getName())
				.thenReturn("UserName");
		Mockito.when(debtorRepo.findByName(debtorDetails.getName()))
				.thenReturn(debtor);
		Mockito.when(debtorDetailsService.findById(id))
				.thenReturn(java.util.Optional.of(debtorDetails));

		debtorService.deleteDebtorDetailsUpdateTotalDebtMakeNewDebtorHistory(id, principal);

		verify(debtorDetailsService).findById(id);
		verify(debtorDetailsService).deleteById(id);
		verify(debtorHistoryService).saveEntityDebtorHistory(debtorDetails);
	}

	@Test
	public void addNewDebtor(){
		Mockito.when(userService.findUserName())
				.thenReturn("UserName");

		String newDebtorName = "Adrian10";
		float debt = 101;
		String reasonForTheDebt = "Kawa";
		String userName = "UserName";
		Debtor newDebtor = new Debtor();
		newDebtor.setUserName(userName);
		newDebtor.setName(newDebtorName);
		newDebtor.setTotalDebt(debt);
		newDebtor.setDate(LocalDate.now());

		debtorService.addNewDebtor(newDebtorName, debt, reasonForTheDebt, userName);

		verify(debtorRepo).save(newDebtor);
	}

	@Test
	public void updateTotalDebtAndMakeNewDebtorDetails(){
//		public void updateTotalDebtAndMakeNewDebtorDetails(DebtorDetailsDTO debtorDetails, Debtor debtor, String userName){
//			debtorDetailsService.addNewDebtorDetails(debtorDetails.getName(), debtorDetails.getDebt(), debtorDetails.getReasonForTheDebt(), userName, debtor);
//			updateTotalDebt(debtor.getName(), debtorDetails.getDebt(), userName);
//		}
		DebtorDetailsDTO debtorDetailsDTO = new DebtorDetailsDTO();
		debtorDetailsDTO.setName("Adrian11");
		debtorDetailsDTO.setDebt(102);
		debtorDetailsDTO.setReasonForTheDebt("Milk");

		DebtorDetails debtorDetails = new DebtorDetails();
		debtorDetails.setName("Adrian11");
		debtorDetails.setDebt(102);
		debtorDetails.setReasonForTheDebt("Milk");
		debtorDetails.setUserName("UserName");
		Debtor debtor = new Debtor();
		debtor.setName(debtorDetails.getName());


		Mockito.when(debtorDetailsService.addNewDebtorDetails(debtorDetails.getName(), debtorDetails.getDebt(),
				debtorDetails.getReasonForTheDebt(), debtorDetails.getUserName(), debtor))
				.thenReturn(debtorDetails);

		verify(debtorDetailsService).addNewDebtorDetails(debtorDetails.getName(), debtorDetails.getDebt(),
				debtorDetails.getReasonForTheDebt(), debtorDetails.getUserName(), debtor);

		verify(debtorService).updateTotalDebt(debtor.getName(), debtorDetails.getDebt(), debtorDetails.getUserName());

	}

}
