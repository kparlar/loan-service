CREATE SCHEMA IF NOT EXISTS credit;

CREATE TABLE credit.customer
(
    id                  varchar(36)  NOT NULL,
    username            varchar(36)  NOT NULL,
    name                varchar(36)  NOT NULL,
    surname             varchar(36)  NOT NULL,
    credit_limit        decimal      NOT NULL,
    used_credit_limit   decimal      NOT NULL,
    CONSTRAINT pk_customer PRIMARY KEY (id)
);

CREATE TABLE credit.loan
(
    id                      varchar(36)  NOT NULL,
    customer_id             varchar(36) NOT NULL,
    loan_amount             decimal     NOT NULL,
    number_of_installment   decimal(2)  NOT NULL,
    create_date             timestamp with time zone default CURRENT_DATE,
    paid                    boolean      not null    default false,
    CONSTRAINT fk_loan_customer_id FOREIGN KEY (customer_id) REFERENCES credit.customer(id),
    CONSTRAINT pk_loan PRIMARY KEY (id)
);


CREATE TABLE credit.installment
(
    id                      varchar(36) NOT NULL,
    loan_id                 varchar(36) NOT NULL,
    amount                  decimal     NOT NULL,
    paid_amount             decimal     NOT NULL,
    due_date                timestamp with time zone default CURRENT_DATE,
    payment_date            timestamp with time zone default NULL,
    paid                    boolean      not null    default false,
    CONSTRAINT fk_installment_loan_id FOREIGN KEY (loan_id) REFERENCES credit.loan(id),
    CONSTRAINT pk_installment PRIMARY KEY (id)
);