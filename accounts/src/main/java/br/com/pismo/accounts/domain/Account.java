package br.com.pismo.accounts.domain;

import br.com.pismo.accounts.domain.exceptions.InsufficientFundsException;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private BigDecimal availableCreditLimit;

    @Column(nullable = false)
    private BigDecimal availableWithdrawalLimit;

    public Account(CreditLimit availableCreditLimit, WithdrawalLimit availableWithdrawalLimit) {
        this.availableCreditLimit = availableCreditLimit != null ? availableCreditLimit.getAmount() : new BigDecimal(0) ;
        this.availableWithdrawalLimit = availableWithdrawalLimit != null ? availableWithdrawalLimit.getAmount() : new BigDecimal(0);
    }

    protected Account() {}

    public Long getId() {
        return id;
    }

    public BigDecimal getAvailableCreditLimit() {
        return availableCreditLimit;
    }

    public BigDecimal getAvailableWithdrawalLimit() {
        return availableWithdrawalLimit;
    }

    void addAvailableCreditLimit(CreditLimit creditLimit) {
        if (creditLimit != null) {
            BigDecimal newAvailableCreditLimit = availableCreditLimit.add(creditLimit.getAmount());

            if (newAvailableCreditLimit.signum() < 0) {
                throw new InsufficientFundsException("Insufficient funds for credit");
            }

            this.availableCreditLimit = newAvailableCreditLimit;
        }
    }

    void addAvailableWithdrawalLimit(WithdrawalLimit withdrawalLimit) {
        if (withdrawalLimit != null) {
            BigDecimal newAvailableWithdrawalLimit = availableWithdrawalLimit.add(withdrawalLimit.getAmount());

            if (newAvailableWithdrawalLimit.signum() < 0) {
                throw new InsufficientFundsException("Insufficient funds for withdrawal");
            }

            this.availableWithdrawalLimit = availableWithdrawalLimit.add(withdrawalLimit.getAmount());
        }
    }
}
