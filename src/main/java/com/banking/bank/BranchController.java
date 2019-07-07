package com.banking.bank;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/api/branch")

public class BranchController
{
    private final BankService bankService;

    public BranchController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping()
    public List<Branch> getBranches() {
        return bankService.getBranches();
    }

    @GetMapping("/city/{city}")
    public List<Branch> getBranchesByCity(@PathVariable String city) {

        return bankService.getBranchesByCity(city);
    }
}
