package training.account.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import training.account.constants.AccountConstants;
import training.account.dto.CustomerDTO;
import training.account.entity.Account;
import training.account.entity.Customer;
import training.account.exception.CustomerAlreadyExistsException;
import training.account.repository.AccountRepository;
import training.account.repository.CustomerRepository;
import training.account.service.AccountService;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDTO customerDTO) {

        Optional<Customer> customerOptional = customerRepository.findByMobilePhone(customerDTO.getMobilePhone());
        if (customerOptional.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    + customerDTO.getMobilePhone());
        }
        Customer customer = customerRepository.save(Customer.builder()
                .name(customerDTO.getName())
                .email(customerDTO.getEmail())
                .mobilePhone(customerDTO.getMobilePhone())
                .build());
        accountRepository.save(Account.builder()
                .accountNumber(10000000000L + new Random().nextInt(900000000))
                .customerId(customer.getCustomerId())
                .accountType(AccountConstants.SAVINGS)
                .branchAddress(AccountConstants.ADDRESS)
                .build());
    }
}
