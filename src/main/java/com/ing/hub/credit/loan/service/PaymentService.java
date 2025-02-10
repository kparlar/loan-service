package com.ing.hub.credit.loan.service;

import com.ing.hub.credit.loan.component.InstallmentComponent;
import com.ing.hub.credit.loan.component.LoanComponent;
import com.ing.hub.credit.loan.component.ValidationComponent;
import com.ing.hub.credit.loan.dto.PaymentResultDTO;
import com.ing.hub.credit.loan.entity.InstallmentEntity;
import com.ing.hub.credit.loan.entity.LoanEntity;
import com.ing.hub.credit.loan.exception.BusinessException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
   private static final int MAX_DUE_DATE_IN_MONTHS = 3;

   private final LoanComponent loanComponent;
   private final ValidationComponent validationComponent;
    private final InstallmentComponent installmentComponent;


    @Builder(toBuilder = true)
    private record InstallmentsToProcess(List<InstallmentEntity> installmentEntitiesNotPaidWithLessThanThreeMonths, int paidInstallment){}

    @Transactional
    public PaymentResultDTO payLoan(String loanId, double amount) {
        Optional<LoanEntity> loanEntityOptional = loanComponent.findById(loanId);
        validationComponent.validateLoan(loanEntityOptional);
        OffsetDateTime now = OffsetDateTime.now();
        InstallmentsToProcess installmentsToProcess = findInstallmentsToProcess(loanEntityOptional.get().getId(), now);
        double remainingAmount = amount;
        double totalPaidAmount = 0.0;
        List<InstallmentEntity> updatedInstallmentEntities = new ArrayList<>();
        for (InstallmentEntity installmentEntity : installmentsToProcess.installmentEntitiesNotPaidWithLessThanThreeMonths()) {
            if (isRemainingAmountBiggerOrEqual(installmentEntity, remainingAmount)) {
                var tempInstallmentEntity = payInstallment(installmentEntity, now);
                updatedInstallmentEntities.add(tempInstallmentEntity);
                remainingAmount -= tempInstallmentEntity.getPaidAmount();
                totalPaidAmount += tempInstallmentEntity.getPaidAmount();
            } else {
                break;
            }
        }
        return updateTables(updatedInstallmentEntities, installmentsToProcess.paidInstallment, loanEntityOptional.get(), totalPaidAmount);
    }

    private InstallmentsToProcess findInstallmentsToProcess(String loanId, OffsetDateTime now ){
        OffsetDateTime maxDueDate = now.plusMonths(MAX_DUE_DATE_IN_MONTHS);
        List<InstallmentEntity> installmentEntities = installmentComponent.findByLoanId(loanId);
        List<InstallmentEntity> installmentEntitiesNotPaid =
                installmentEntities.stream()
                        .filter(installmentEntity -> !installmentEntity.isPaid())
                        .toList();
        int paidInstallment = installmentEntities.size() - installmentEntitiesNotPaid.size();
        List<InstallmentEntity> installmentEntitiesNotPaidWithLessThanThreeMonths =
                installmentEntitiesNotPaid.stream()
                        .filter(installmentEntity -> installmentEntity.getDueDate().isBefore(maxDueDate))
                        .toList();
        if (isInstallmentsDueDateBiggerThanThreeMonths(installmentEntitiesNotPaid, installmentEntitiesNotPaidWithLessThanThreeMonths)) {
            throw new BusinessException(
                    "You cannot pay your installment due date for more than 3 months.");
        }
        return InstallmentsToProcess.builder()
                .installmentEntitiesNotPaidWithLessThanThreeMonths(installmentEntitiesNotPaidWithLessThanThreeMonths)
                .paidInstallment(paidInstallment)
                .build();
    }
    private static boolean isRemainingAmountBiggerOrEqual(InstallmentEntity installmentEntity, double remainingAmount) {
        return remainingAmount >= installmentEntity.getAmount();
    }

    private InstallmentEntity payInstallment(
            InstallmentEntity installmentEntity, OffsetDateTime now) {
        InstallmentEntity.InstallmentEntityBuilder tempInstallmentEntityBuilder =
                installmentEntity.toBuilder();
        tempInstallmentEntityBuilder.paymentDate(now);
        // Apply discount or penalty based on the due date
        long daysDiff = ChronoUnit.DAYS.between(installmentEntity.getDueDate(), now);
        if (daysDiff < 0) {
            tempInstallmentEntityBuilder.paidAmount(
                    installmentEntity.getAmount()
                            - installmentEntity.getAmount() * 0.001 * Math.abs(daysDiff));
        } else if (daysDiff > 0) {
            tempInstallmentEntityBuilder.paidAmount(
                    installmentEntity.getAmount() + installmentEntity.getAmount() * 0.001 * daysDiff);
        } else {
            tempInstallmentEntityBuilder.paidAmount(installmentEntity.getAmount());
        }
        tempInstallmentEntityBuilder.paid(true);
        return tempInstallmentEntityBuilder.build();
    }

    private PaymentResultDTO updateTables(List<InstallmentEntity> updatedInstallmentEntities, long paidInstallment, LoanEntity loanEntity, double totalPaidAmount) {
        if (!updatedInstallmentEntities.isEmpty()) {
            installmentComponent.saveAll(updatedInstallmentEntities);
            boolean isPaid = updatedInstallmentEntities.size() + paidInstallment == loanEntity.getNumberOfInstallment();
            loanComponent.save(loanEntity.toBuilder().paid(isPaid).build());
            return new PaymentResultDTO(updatedInstallmentEntities.size(), totalPaidAmount, isPaid);
        } else {
            throw new BusinessException("Amount paid is not closed any installment.");
        }
    }

    private boolean isInstallmentsDueDateBiggerThanThreeMonths(List<InstallmentEntity> installmentEntitiesNotPaid, List<InstallmentEntity> installmentEntitiesNotPaidWithLessThanThreeMonths) {
        return !installmentEntitiesNotPaid.isEmpty()
                && installmentEntitiesNotPaidWithLessThanThreeMonths.isEmpty();
    }

}

