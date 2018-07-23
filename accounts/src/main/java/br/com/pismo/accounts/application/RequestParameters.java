package br.com.pismo.accounts.application;

import br.com.pismo.accounts.domain.CreditLimit;
import br.com.pismo.accounts.domain.WithdrawalLimit;

public class RequestParameters {

    private CreditLimit availableCreditLimit;
    private WithdrawalLimit availableWithdrawalLimit;

    public RequestParameters(CreditLimit availableCreditLimit, WithdrawalLimit availableWithdrawalLimit) {
        this.availableCreditLimit = availableCreditLimit;
        this.availableWithdrawalLimit = availableWithdrawalLimit;
    }

    protected RequestParameters() {}

    public CreditLimit getAvailableCreditLimit() {
        return availableCreditLimit;
    }

    public WithdrawalLimit getAvailableWithdrawalLimit() {
        return availableWithdrawalLimit;
    }
}
