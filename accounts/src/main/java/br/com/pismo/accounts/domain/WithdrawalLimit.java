package br.com.pismo.accounts.domain;

import java.math.BigDecimal;

public class WithdrawalLimit {

    private BigDecimal amount;

    public WithdrawalLimit(BigDecimal amount) {
        this.amount = amount;
    }

    protected WithdrawalLimit() {}

    public BigDecimal getAmount() {
        return amount != null ? amount : new BigDecimal(0);
    }
}
