package com.ing.hub.credit.loan.config;

import static com.ing.hub.credit.loan.util.constants.ErrorMessage.UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.hub.credit.loan.component.CustomerComponent;
import com.ing.hub.credit.loan.dto.LoanRequestDTO;
import com.ing.hub.credit.loan.entity.CustomerEntity;
import com.ing.hub.credit.loan.exception.UnAuthorizedException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@Slf4j
@RequiredArgsConstructor
public class UserFilter implements Filter {

  private final CustomerComponent customerComponent;

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
      throws ServletException, IOException {

    HttpServletResponse response = (HttpServletResponse) servletResponse;
    HttpServletRequest request = (HttpServletRequest) servletRequest;

    if (request.getRequestURI().contains("swagger")
        || request.getRequestURI().contains("api-docs")) {
      chain.doFilter(request, response);
      return;
    }

    UserDetails userDetails =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (userDetails.getUsername().equals("admin")) {
      chain.doFilter(request, response);
    } else {

      log.info("Logging Request  {} : {}", request.getMethod(), request.getRequestURI());
      try {

        if (request.getMethod().equals("GET")
            && request.getRequestURI().contains("/installments/loan")) {
          String[] splittedUrl = request.getRequestURI().split("/");
          String loanId = splittedUrl[splittedUrl.length - 1];
          checkByLoanId(loanId, userDetails);
        } else if (request.getMethod().equals("GET")
            && request.getRequestURI().contains("/loans/customer/")) {
          String[] splittedUrl = request.getRequestURI().split("/");
          String customerId = splittedUrl[splittedUrl.length - 1];
          checkByCustomerId(customerId, userDetails);
        } else if (request.getMethod().equals("POST")
            && request.getRequestURI().contains("/loans/")) {
          String[] splittedUrl = request.getRequestURI().split("/");
          String customerId = splittedUrl[splittedUrl.length - 1];
          checkByCustomerId(customerId, userDetails);
        } else if (request.getMethod().equals("PUT")
            && request.getRequestURI().contains("/loans")) {
          String[] splittedUrl = request.getRequestURI().split("/");
          String loanId = splittedUrl[splittedUrl.length-2];
          checkByLoanId(loanId, userDetails);
        } else {
          log.info("unknown url called");
          throw new UnAuthorizedException(UNAUTHORIZED);
        }

        chain.doFilter(request, response);
      } catch (Exception e) {
        log.error(UNAUTHORIZED);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
      }
    }
  }

  private void checkByCustomerId(String customerId, UserDetails userDetails){
    CustomerEntity customerEntity = customerComponent.findById(customerId);
    if (!customerEntity.getUsername().equals(userDetails.getUsername())) {
      throw new UnAuthorizedException(UNAUTHORIZED);
    }
  }

  private void checkByLoanId(String loanId, UserDetails userDetails){
    Optional<CustomerEntity> customerEntityOptional = customerComponent.findByLoanId(loanId);
    if (customerEntityOptional.isPresent()) {
      if (!customerEntityOptional.get().getUsername().equals(userDetails.getUsername())) {
        throw new UnAuthorizedException(UNAUTHORIZED);
      }
    } else {
      log.info("Customer not found so thrown only admin takes action error.");
      throw new UnAuthorizedException(UNAUTHORIZED);
    }
  }
}
