package br.com.pismo.accounts.application;

import br.com.pismo.accounts.domain.Account;
import br.com.pismo.accounts.domain.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/v1/accounts")
    public Account save(@RequestBody RequestParameters params) {
        return accountService.create(params.getAvailableCreditLimit(), params.getAvailableWithdrawalLimit());
    }

    @PatchMapping("/v1/accounts/{id}")
    public Account update(@PathVariable Long id, @RequestBody RequestParameters params) {
        return accountService.changeLimits(id, params.getAvailableCreditLimit(), params.getAvailableWithdrawalLimit());
    }

    @GetMapping("/v1/accounts")
    public List<Account> list() {
        return accountService.findAll();
    }

    @GetMapping("/v1/accounts/{id}")
    public Account findOne(@PathVariable Long id) {
        return accountService.findBy(id);
    }
}
