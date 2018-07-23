package br.com.pismo.accounts.domain;

import br.com.pismo.accounts.domain.exceptions.AccountNotFoundException;
import br.com.pismo.accounts.domain.exceptions.InsufficientFundsException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AccountServiceDefaultTest {

    @Mock private AccountRepository accountRepository;
    private AccountServiceDefault service;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        service = new AccountServiceDefault(accountRepository);
    }

    @Test
    public void mustCreateAnAccount() {
        CreditLimit creditLimit = new CreditLimit(new BigDecimal(1000.00));
        WithdrawalLimit withdrawalLimit = new WithdrawalLimit(new BigDecimal(500.00));

        Mockito.when(accountRepository.save(Mockito.any(Account.class))).then(invocation -> invocation.getArguments()[0]);

        Account accountPersisted = service.create(creditLimit, withdrawalLimit);

        Assert.assertEquals(creditLimit.getAmount(), accountPersisted.getAvailableCreditLimit());
        Assert.assertEquals(withdrawalLimit.getAmount(), accountPersisted.getAvailableWithdrawalLimit());
    }

    @Test
    public void mustAddAccountLimits() {
        CreditLimit creditLimit = new CreditLimit(new BigDecimal(500.00));
        WithdrawalLimit withdrawalLimit = new WithdrawalLimit(new BigDecimal(500.00));
        Account account = new Account(creditLimit, withdrawalLimit);

        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(account));

        Account accountChanged = service.changeLimits(1L,
                new CreditLimit(new BigDecimal(100.00)),
                new WithdrawalLimit(new BigDecimal(100.00)));

        Assert.assertEquals(new BigDecimal(600.00), accountChanged.getAvailableCreditLimit());
        Assert.assertEquals(new BigDecimal(600.00), accountChanged.getAvailableWithdrawalLimit());
    }

    @Test
    public void mustSubtractAccountLimits() {
        CreditLimit creditLimit = new CreditLimit(new BigDecimal(500.00));
        WithdrawalLimit withdrawalLimit = new WithdrawalLimit(new BigDecimal(500.00));
        Account account = new Account(creditLimit, withdrawalLimit);

        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(account));

        Account accountChanged = service.changeLimits(1L,
                new CreditLimit(new BigDecimal(-100.00)),
                new WithdrawalLimit(new BigDecimal(-100.00)));

        Assert.assertEquals(new BigDecimal(400.00), accountChanged.getAvailableCreditLimit());
        Assert.assertEquals(new BigDecimal(400.00), accountChanged.getAvailableWithdrawalLimit());
    }

    @Test
    public void mustFindAllAccounts() {
        CreditLimit creditLimit = new CreditLimit(new BigDecimal(500.00));
        WithdrawalLimit withdrawalLimit = new WithdrawalLimit(new BigDecimal(500.00));

        Account account1 = new Account(creditLimit, withdrawalLimit);
        Account account2 = new Account(creditLimit, withdrawalLimit);

        Mockito.when(accountRepository.findAll()).thenReturn(Arrays.asList(account1, account2));

        List<Account> allAccounts = service.findAll();
        Assert.assertEquals(2, allAccounts.size());
    }

    @Test
    public void mustFindAnAccountById() {
        CreditLimit creditLimit = new CreditLimit(new BigDecimal(500.00));
        WithdrawalLimit withdrawalLimit = new WithdrawalLimit(new BigDecimal(500.00));
        Account account = new Account(creditLimit, withdrawalLimit);

        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(account));

        Account accountFound = service.findBy(1L);

        Assert.assertEquals(creditLimit.getAmount(), accountFound.getAvailableCreditLimit());
        Assert.assertEquals(withdrawalLimit.getAmount(), accountFound.getAvailableWithdrawalLimit());
    }

    @Test(expected = AccountNotFoundException.class)
    public void mustThrowExceptionWhenTryChangeLimitsOfNullAccount() {
        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        service.changeLimits(1L,
                new CreditLimit(new BigDecimal(100.00)),
                new WithdrawalLimit(new BigDecimal(100.00)));
    }

    @Test(expected = AccountNotFoundException.class)
    public void mustThrowExceptionWhenAccountNotFound() {
        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        service.findBy(1L);
    }

    @Test
    public void mustCreateAnAccountWithoutLimitsWhenGivenNullLimit() {
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).then(invocation -> invocation.getArguments()[0]);

        Account accountPersisted = service.create(null, null);

        Assert.assertEquals(new BigDecimal(0), accountPersisted.getAvailableCreditLimit());
        Assert.assertEquals(new BigDecimal(0), accountPersisted.getAvailableWithdrawalLimit());
    }

    @Test(expected = InsufficientFundsException.class)
    public void mustThrowExceptionWhenInsufficientFundsForCredit() {
        CreditLimit creditLimit = new CreditLimit(new BigDecimal(500.00));
        WithdrawalLimit withdrawalLimit = new WithdrawalLimit(new BigDecimal(500.00));
        Account account = new Account(creditLimit, withdrawalLimit);

        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(account));

       service.changeLimits(1L,
                new CreditLimit(new BigDecimal(-501.00)),
                new WithdrawalLimit(new BigDecimal(0)));

    }

    @Test(expected = InsufficientFundsException.class)
    public void mustThrowExceptionWhenInsufficientFundsForWithdrawal() {
        CreditLimit creditLimit = new CreditLimit(new BigDecimal(500.00));
        WithdrawalLimit withdrawalLimit = new WithdrawalLimit(new BigDecimal(500.00));
        Account account = new Account(creditLimit, withdrawalLimit);

        Mockito.when(accountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(account));

        service.changeLimits(1L,
                new CreditLimit(new BigDecimal(0)),
                new WithdrawalLimit(new BigDecimal(-501.00)));

    }

}
