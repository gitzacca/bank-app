package br.com.pismo.accounts.domain;

import br.com.pismo.accounts.domain.exceptions.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceDefault implements AccountService {

    private AccountRepository repository;

    @Autowired
    public AccountServiceDefault(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Account create(CreditLimit creditLimit, WithdrawalLimit withdrawalLimit) {
        Account account = new Account(creditLimit, withdrawalLimit);
        return repository.save(account);
    }

    @Override
    public Account changeLimits(Long id, CreditLimit creditLimit, WithdrawalLimit withdrawalLimit) {
        Account account = repository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account with id: " + id + " not found"));

        account.addAvailableCreditLimit(creditLimit);
        account.addAvailableWithdrawalLimit(withdrawalLimit);

        return repository.save(account);
    }

    @Override
    public List<Account> findAll() {
        List<Account> result = new ArrayList<>();
        repository.findAll().forEach(result::add);
        return result;
    }

    @Override
    public Account findBy(Long id) {
        return repository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account with id: " + id + " not found"));
    }
}
