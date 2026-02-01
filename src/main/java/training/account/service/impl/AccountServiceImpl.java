package training.account.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.account.constants.AccountConstants;
import training.account.dto.AccountDTO;
import training.account.dto.CustomerDTO;
import training.account.entity.Account;
import training.account.entity.Customer;
import training.account.exception.CustomerAlreadyExistsException;
import training.account.exception.ResourceNotFoundException;
import training.account.repository.AccountRepository;
import training.account.repository.CustomerRepository;
import training.account.service.AccountService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    private CustomerRepository customerRepository;

    @Transactional
    @Override
    public void createAccount(CustomerDTO customerDTO) {
        Optional<Customer> customerOptional = customerRepository.findByMobileNumber(customerDTO.getMobileNumber());
        if (customerOptional.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    + customerDTO.getMobileNumber());
        }
        Customer customerEntity = Customer.builder()
                .name(customerDTO.getName())
                .email(customerDTO.getEmail())
                .mobileNumber(customerDTO.getMobileNumber())
                .build();
        customerEntity.setCreatedAt(LocalDateTime.now());
        customerEntity.setCreatedBy("Anonymous");
        Customer customer = customerRepository.save(customerEntity);
        Account account = Account.builder()
                .customerId(customer.getCustomerId())
                .accountType(AccountConstants.SAVINGS)
                .branchAddress(AccountConstants.ADDRESS)
                .build();
        account.setCreatedAt(LocalDateTime.now());
        account.setCreatedBy("Anonymous");
        accountRepository.save(account);
    }

    @Override
    public CustomerDTO fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Account account = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));
        return CustomerDTO.builder()
                .name(customer.getName())
                .email(customer.getEmail())
                .mobileNumber(customer.getMobileNumber())
                .accountDTO(AccountDTO.builder()
                        .accountNumber(account.getAccountNumber())
                        .accountType(account.getAccountType())
                        .branchAddress(account.getBranchAddress())
                        .build())
                .build();
    }

    @Override
    public boolean updateAccount(CustomerDTO customerDTO) {
        boolean isUpdated = false;
        AccountDTO accountDTO = customerDTO.getAccountDTO();
        if (accountDTO != null) {
            Account account = accountRepository.findById(accountDTO.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Account", "AccountNumber", accountDTO.getAccountNumber().toString()));
            account.setAccountType(accountDTO.getAccountType());
            account.setBranchAddress(accountDTO.getBranchAddress());
            account.setUpdatedAt(LocalDateTime.now());
            account.setUpdatedBy("Anonymous");
            accountRepository.save(account);
            Long customerId = account.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerId", customerId.toString()));
            customer.setName(customerDTO.getName());
            customer.setEmail(customerDTO.getEmail());
            customer.setMobileNumber(customerDTO.getMobileNumber());
            customer.setUpdatedAt(LocalDateTime.now());
            customer.setUpdatedBy("Anonymous");
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Transactional
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() ->
                new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        accountRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }
}
