package com.ing.hub.credit.loan.controller;

import com.ing.hub.credit.loan.component.LoanComponent;
import com.ing.hub.credit.loan.dto.LoanDTO;
import com.ing.hub.credit.loan.dto.LoanRequestDTO;
import com.ing.hub.credit.loan.dto.PaymentResultDTO;
import com.ing.hub.credit.loan.service.CreditService;
import com.ing.hub.credit.loan.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loans")
public class LoanController {

  private final CreditService creditService;
  private final PaymentService paymentService;
  private final LoanComponent loanComponent;


  @Operation(
      summary = "Create loan for given customer and related parameters",
      description = "There are validation. Please check Below",
      responses = {
        @ApiResponse(
            description = "LoanDTO",
            content = @Content(schema = @Schema(implementation = LoanDTO.class))),
        @ApiResponse(responseCode = "400", description = " #1 Customer does not have enough credit limit.<br/>"),
        @ApiResponse(responseCode = "404", description = "Customer not found."),
        @ApiResponse(
            responseCode = "412",
            description =
                "#1 Invalid number of installments. Valid values are: 6, 9, 12, 24.<br/>"
                    + "#2 Interest rate must be between 0.1 and 0.5.")
      })
  @PostMapping("/customer/{customerId}")
  public ResponseEntity<LoanDTO> createLoan(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Loan to create",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = LoanRequestDTO.class),
                      examples =
                          @ExampleObject(
                              value =
                                  "\t{\n"
                                      + "\t  \"customerId\": \"4ed6e47c-5672-4c46-8877-2819452d5bc6\",\n"
                                      + "\t  \"amount\": 1000,\n"
                                      + "\t  \"interestRate\": 0.1,\n"
                                      + "\t  \"numberOfInstallment\": 6\n"
                                      + "}")))
          @PathVariable(value = "customerId")String customerId, @RequestBody
          LoanRequestDTO loanRequestDTO) {
    var loanDTO = creditService.create(customerId, loanRequestDTO);
    return ResponseEntity.ok(loanDTO);
  }

    @Operation(
            summary = "Get loans for given customer",
            description = "If nothing found then return empty array.",
            responses = {
                    @ApiResponse(
                            description = "LoanDTO",
                            content = @Content(schema = @Schema(implementation = LoanDTO.class)))
            })
  @GetMapping("/customer/{customerId}")
  public ResponseEntity<List<LoanDTO>> listLoans(@PathVariable(value = "customerId")String customerId) {
    var loanDTOs = loanComponent.listLoans(customerId);
    return ResponseEntity.ok(loanDTOs);
  }

  @Operation(
      summary = "Pay loan for given loan id",
      description =
          "Amount is paid to close related installment. There are some business logics like <br/>"
              + "* Installments should be paid wholly or not at all. So if installments amount is 10 and you send 20, 2 installments can be paid. If you send 15, only 1 can be paid. If you send 5, no installments can be paid.<br/>"
              + "* Earliest installment should be paid first and if there are more money then you should continue to next installment.<br/>"
              + "* Installments have due date that still more than 3 calendar months cannot be paid. So if we were in January, you could pay only for January, February and March installments.<br/>"
              + "* A result should be returned to inform how many installments paid, total amount spent and if loan is paid completely.<br/>"
              + "* Necessary updates should be done in customer, loan and installment tables.<br/>",
      responses = {
        @ApiResponse(
            description = "PaymentResultDTO",
            content = @Content(schema = @Schema(implementation = PaymentResultDTO.class))),
        @ApiResponse(responseCode = "400",
                description = "#1 Amount paid is not closed any installment.<br/>" +
                              "#2 You cannot pay your installment due date for more than 3 months.<br/>" +
                              "#3 Loan has already been paid."),
        @ApiResponse(responseCode = "404", description = "Loan not found."),
      })
  @PutMapping("/{loanId}/pay")
  public ResponseEntity<PaymentResultDTO> payLoan(
      @PathVariable String loanId, @RequestBody double amount) {
        var paymentResultDTO = paymentService.payLoan(loanId, amount);
        return ResponseEntity.ok(paymentResultDTO);
    }
}
