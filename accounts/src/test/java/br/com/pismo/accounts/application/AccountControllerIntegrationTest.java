package br.com.pismo.accounts.application;

import br.com.pismo.accounts.domain.Account;
import br.com.pismo.accounts.domain.AccountRepository;
import br.com.pismo.accounts.domain.CreditLimit;
import br.com.pismo.accounts.domain.WithdrawalLimit;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AccountControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AccountRepository repository;

	@Test
	public void mustPerformAPostToCreateAnAccount() throws Exception{
		String requestBody = "{\"availableCreditLimit\": {\"amount\": 100}, \"availableWithdrawalLimit\": {\"amount\": 100}}";

		mockMvc.perform(MockMvcRequestBuilders.post("/v1/accounts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.availableCreditLimit", CoreMatchers.is(100)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.availableWithdrawalLimit", CoreMatchers.is(100)));
	}

	@Test
	public void mustPerformAPathToChangeAccountLimits() throws Exception {
		String requestBody = "{\"availableCreditLimit\": {\"amount\": 100}, \"availableWithdrawalLimit\": {\"amount\": 100}}";
		Account account = createTestAccount();

		mockMvc.perform(MockMvcRequestBuilders.patch("/v1/accounts/" + account.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.availableCreditLimit", CoreMatchers.is(1100D)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.availableWithdrawalLimit", CoreMatchers.is(1100D)));

	}

	@Test
	public void mustPerformAGetToListAccounts() throws Exception {
		Account account = createTestAccount();

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/accounts/")
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].id", CoreMatchers.notNullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].availableCreditLimit", CoreMatchers.is(account.getAvailableCreditLimit().doubleValue())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].availableWithdrawalLimit", CoreMatchers.is(account.getAvailableWithdrawalLimit().doubleValue())));
	}

	@Test
	public void mustPerformAGetToFindAnAccountById() throws Exception {
		Account account = createTestAccount();

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/accounts/" + account.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(account.getId().intValue())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.availableCreditLimit", CoreMatchers.is(account.getAvailableCreditLimit().doubleValue())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.availableWithdrawalLimit", CoreMatchers.is(account.getAvailableWithdrawalLimit().doubleValue())));
	}

	@Test
	public void mustReturnError400WhenAccountNotFound() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/accounts/" + 10)
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	@Test
	public void mustReturnError401WhenInsufficientFundsForCredit() throws Exception {
		String requestBody = "{\"availableCreditLimit\": {\"amount\": -1001}}";
		Account account = createTestAccount();

		mockMvc.perform(MockMvcRequestBuilders.patch("/v1/accounts/" + account.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	@Test
	public void mustReturnError401WhenInsufficientFundsForWithdrawal() throws Exception {
		String requestBody = "{\"availableWithdrawalLimit\": {\"amount\": -1001}}";
		Account account = createTestAccount();

		mockMvc.perform(MockMvcRequestBuilders.patch("/v1/accounts/" + account.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

	private Account createTestAccount() {
		Account account = new Account(new CreditLimit(new BigDecimal(1000)), new WithdrawalLimit(new BigDecimal(1000)));
		return repository.save(account);
	}

}
