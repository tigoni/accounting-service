# Accounting Service

A Java Spring Boot-based accounting service for managing loan transactions, generating and generating loan financial reports.


### Use Docker Compose To Build and Run
The project is containerized so its easy to build and run. 
Clone the repo, then build and run with:


```bash
docker compose up --build
```

The service base url is: `http://localhost:8080/api/v1/acc-service/`

## API Examples

### Loan Disbursement

```bash
curl -X POST http://localhost:8080/api/v1/acc-service/loans/disbursement \
  -H "Content-Type: application/json" \
  -d '{
    "idempotencyKey": "LOAN-DISBURSE-12345",
    "description": "Loan disbursement with origination fee",
    "reference": "LOAN-REF-12345",
    "transactionLines": [
        {
            "accountId": "loans-receivable-short-term",
            "debitAmount": 10000,
            "creditAmount": null
        },
        {
            "accountId": "m-pesa-account",
            "debitAmount": null,
            "creditAmount": 10000
        },
        {
            "accountId": "loan-origination-fee-receivable",
            "debitAmount": 500,
            "creditAmount": null
        },
        {
            "accountId": "fee-income-loan-processing",
            "debitAmount": null,
            "creditAmount": 500
        }
    ]
}'
```

### Loan Reversal

```bash
curl -X POST http://localhost:8080/api/v1/acc-service/loans/{uuid}/reversal \
-H "" \
-d '
{
    "idempotencyKey": "LOAN-DISBURSE-REVERSE-12345",
    "description":"Loan disbursement",
    "reference":"powe923",
    "transactionLines": [
        {
            "accountingId": "Loans Receivable - Short Term",
            "debitAmount": "10000",
            "creditAmount": "0"
        },
        {
            "accountingId": "Mpesa Account",
            "debitAmount": "0",
            "creditAmount": "10000"
        }
    ]
}
'
```
### Loan Writeoff

```bash
curl -X POST http://localhost:8080/api/v1/acc-service/loans/{uuid}/writeoff \
-H "" \
-d '
{
    "idempotencyKey": "LOAN-WRITEOFF-12345",
    "description":"Loan write-off tx",
    "reference":"powe923",
    "transactionLines": [
        {
            "accountId": "Loans Receivable - Short Term",
            "debitAmount": "0",
            "creditAmount": "5000"
        },
        {
            "accountId": "Bad Debt Expense",
            "debitAmount": "5000",
            "creditAmount": "0"
        }
    ]
}
'
```



### Trial Balance

```bash
curl -X GET http://localhost:8080/api/v1/acc-service/reports/trial-balance
```

### Balance Sheet

```bash
curl -X GET "http://localhost:8080/api/v1/acc-service/reports/balance-sheet?asOfDate=2024-01-01T00:00:00"
```


---

## Some Notes on Task Implementation
1. I have tried to reduce complexity and therefore did not wire up a Kafka pipeline + consumers which is ideally how I would process these endpoint requests.
2. I dockerized the project. To run and test, clone and run `docker compose up --build`. The DB will be setup and migrations triggered.  I have also included a Postmant collection(`AccountingService_Postman_Collection.json`) that can be imported into Postman ready for testing.
3. Although the job description specifically mentions PHP and/or Go as the desired languages, I asked (emailed) to do the implementation in Java as I have used it extensively in implementing similar accounting domain requirements.
4. I do have experience in Go but for middleware development (cache, routing etc). My goal is to adapt this project in Go and hopefully submit for evaluation in time for review.

