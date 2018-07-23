package br.com.pismo.accounts.domain;

import java.util.List;

public interface AccountService {

    Account create(CreditLimit creditLimit, WithdrawalLimit withdrawalLimit);

    Account changeLimits(Long id, CreditLimit creditLimit, WithdrawalLimit withdrawalLimit);

    List<Account> findAll();

    Account findBy(Long id);

}
