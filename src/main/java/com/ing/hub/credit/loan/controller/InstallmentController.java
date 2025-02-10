package com.ing.hub.credit.loan.controller;

import com.ing.hub.credit.loan.component.InstallmentComponent;
import com.ing.hub.credit.loan.dto.InstallmentDTO;
import com.ing.hub.credit.loan.entity.InstallmentEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/installments")
public class InstallmentController {
    
    private final InstallmentComponent installmentComponent;

    @Operation(
            summary = "Get installments of given loan",
            description = "If nothing found then return empty array.",
            responses = {
                    @ApiResponse(
                            description = "InstallmentDTO",
                            content = @Content(schema = @Schema(implementation = InstallmentEntity.class)))

            })
    @GetMapping("/loan/{loanId}")
    public ResponseEntity<List<InstallmentDTO>> listInstallments(@PathVariable(value = "loanId")String loanId) {

        var installmentDTOS = installmentComponent.listInstallments(loanId);
        return ResponseEntity.ok(installmentDTOS);
    }

}
