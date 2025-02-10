package com.ing.hub.credit.loan.component;

import com.ing.hub.credit.loan.dto.LoanDTO;
import com.ing.hub.credit.loan.dto.LoanRequestDTO;
import com.ing.hub.credit.loan.entity.CustomerEntity;
import com.ing.hub.credit.loan.entity.LoanEntity;
import com.ing.hub.credit.loan.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoanComponent {
    private final LoanRepository loanRepository;


    public List<LoanDTO> listLoans(String customerId) {
        var loanEntities = loanRepository.findByCustomerId(customerId);
        return loanEntities.stream().map(LoanEntity::toDTO).toList();
    }

    public Optional<LoanEntity> findById(String loanId) {
        return loanRepository.findById(loanId);
    }

    public LoanEntity create(LoanRequestDTO loanRequestDTO, CustomerEntity customerEntity, OffsetDateTime offsetDateTime) {
        LoanEntity loanEntity = LoanEntity.builder()
                .id(UUID.randomUUID().toString())
                .loanAmount(loanRequestDTO.amount())
                .numberOfInstallment(loanRequestDTO.numberOfInstallment())
                .createDate(offsetDateTime)
                .customerId(customerEntity.getId()).build();
        loanRepository.upsert(loanEntity);
        return loanEntity;
    }
    public void save(LoanEntity loanEntity) {
        loanRepository.upsert(loanEntity);
    }
}
