package com.ing.hub.credit.loan.component;

import com.ing.hub.credit.loan.entity.CustomerEntity;
import com.ing.hub.credit.loan.exception.EntityNotFoundException;
import com.ing.hub.credit.loan.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.ing.hub.credit.loan.util.constants.ErrorMessage.NOT_FOUND__CUSTOMER;

@Component
@RequiredArgsConstructor
public class CustomerComponent {

    private final CustomerRepository customerRepository;

    public void create(CustomerEntity customerEntity){
        customerRepository.upsert(customerEntity);
    }

    public CustomerEntity findById(String id){
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND__CUSTOMER));
    }

    public Optional<CustomerEntity> findByLoanId(String loanId){
        return customerRepository.findByLoanId(loanId);

    }
}
