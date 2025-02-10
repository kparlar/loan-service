package com.ing.hub.credit.loan.repository;

import com.ing.hub.credit.loan.entity.InstallmentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InstallmentRepository {
    int upsertAll(@Param("installmentEntities")List<InstallmentEntity> installmentEntities);
    List<InstallmentEntity> findByLoanId(String loanId);
}
