package com.banking.bank;

import java.util.List;

public interface BankService
{
    List<Branch> getBranches();
    List<Branch> getBranchesByCity(String city);
}
