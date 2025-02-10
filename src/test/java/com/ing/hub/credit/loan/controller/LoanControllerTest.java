package com.ing.hub.credit.loan.controller;

import static com.ing.hub.credit.loan.constant.TestConstants.VALID_CUSTOMER_ID;
import static com.ing.hub.credit.loan.constant.TestConstants.VALID_LOAN_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.hub.credit.loan.config.RestExceptionHandler;
import com.ing.hub.credit.loan.dto.LoanDTO;
import com.ing.hub.credit.loan.dto.LoanRequestDTO;

import java.nio.charset.StandardCharsets;

import com.ing.hub.credit.loan.service.CreditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class LoanControllerTest {
    private MockMvc mvc;

    @Mock
    private CreditService creditService;

    @InjectMocks
    private LoanController loanController;

    // This object will be magically initialized by the initFields method below.
    private JacksonTester<LoanRequestDTO> jsonLoanRequestDTO;
    private JacksonTester<LoanDTO> jsonLoanDTO;

    @BeforeEach
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    mvc =
        MockMvcBuilders.standaloneSetup(loanController)
            .setControllerAdvice(new RestExceptionHandler())
            .build();
    }

    @Test
    public void createLoan_givenValidLoanId_WhenValidationPassed_thenSuccessfull() throws Exception {
        // given
        LoanDTO expectedResponse = LoanDTO.builder()
                .id(VALID_LOAN_ID)
                .paid(false)
                .loanAmount(100)
                .numberOfInstallment(6)
                .customerId(VALID_CUSTOMER_ID)
                .build();
        LoanRequestDTO loanRequestDTO = LoanRequestDTO.builder()
                .numberOfInstallment(6)
                .interestRate(0.1)
                .amount(100)
                .build();
        given(creditService.create(VALID_CUSTOMER_ID, loanRequestDTO))
                .willReturn(expectedResponse);

        // when
        MockHttpServletResponse response = mvc.perform(
                        post("/loans/customer/"+VALID_CUSTOMER_ID).contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(jsonLoanRequestDTO.write(loanRequestDTO).getJson())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonLoanDTO.write(expectedResponse).getJson()
        );
    }

}
