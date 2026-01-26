package training.account.service.impl;

import org.springframework.stereotype.Service;
import training.account.dto.CustomerDTO;
import training.account.repository.AccountRepository;
import training.account.repository.CustomerRepository;
import training.account.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDTO customerDTO) {

    }
}
