package com.ing.hub.credit.loan.component;

import com.ing.hub.credit.loan.dto.InstallmentDTO;
import com.ing.hub.credit.loan.dto.LoanRequestDTO;
import com.ing.hub.credit.loan.entity.InstallmentEntity;
import com.ing.hub.credit.loan.entity.LoanEntity;
import com.ing.hub.credit.loan.repository.InstallmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class InstallmentComponent {
    private final InstallmentRepository installmentRepository;

    public List<InstallmentEntity> findByLoanId(String loanId) {
        return installmentRepository.findByLoanId(loanId);
    }
    public List<InstallmentDTO> listInstallments(String loanId) {
        var loanEntities = findByLoanId(loanId);
        return loanEntities.stream().map(InstallmentEntity::toDTO).toList();
    }
    public List<InstallmentEntity> create(LoanRequestDTO loanRequestDTO, double totalLoanAmount, LoanEntity loanEntity, OffsetDateTime offsetDateTime) {
        List<InstallmentEntity> installmentEntities = IntStream.range(0, loanRequestDTO.numberOfInstallment()).mapToObj(value -> InstallmentEntity.builder()
                .id(UUID.randomUUID().toString())
                .amount(totalLoanAmount / loanRequestDTO.numberOfInstallment())
                .dueDate(offsetDateTime.plusMonths(value+1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0))
                .paymentDate(null)
                .loanId(loanEntity.getId()).build()).toList();
        installmentRepository.upsertAll(installmentEntities);
        return installmentEntities;
    }
    public void saveAll(List<InstallmentEntity> updatedInstallmentEntities){
        installmentRepository.upsertAll(updatedInstallmentEntities);
    }

}
