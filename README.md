
# vaping-stamps-api-stub

The Vaping Stamps API Stub is a service to support stateful sandbox testing in the External Test environment. It stubs the behaviour of the Vaping Stamps API microservice.

It is a semi-stateful test service - in order to use it, you need to request it to set up test data for a specific Vaping Stamps Supplier. It will then generate pre-defined test data for the Supplier. The GET endpoints are called by the relevant API microservices.


## Who/What uses this service?
API microservices that make Vaping Stamps-related calls to IES/ETDS which are deployed to the External Test environment. The microservices should be configured to connect to this stub instead of a real EIS/ETDS.

## Running the service locally

To run the service locally on port 7012:

sbt 'run 7012'

To test the stub endpoints for Vaping Stamps

GET
curl -X GET http://localhost:7012/etds/vaping/stamps/GBVA0000200DS/status

POST
curl -X POST "http://localhost:7012/etds/vaping/stamps/status" \
-H "Content-Type: application/json" \
-H "Accept: application/vnd.hmrc.1.0+json" \
-d '{ "vdsEmail": "test@test.com", "stampsReferenceNumber": "GBVA0000200DS" }'

### Example stub data
| Approval Id   | Status code | Description                                                                          |
|---------------|-------------|--------------------------------------------------------------------------------------|
| GBVA0000200DS | 200         | Scenario for successful response with approvalStatus = APPROVED                      |
| XIVA0000200DS | 200         | Scenario for successful response with approvalStatus = APPROVED                      |
| GBVA0000266DS | 200         | Scenario for successful response with approvalStatus = NOT_APPROVED                  |
| XIVA0000266DS | 200         | Scenario for successful response with approvalStatus = NOT_APPROVED                  |
| GBVA0000401DS | 401         | Scenario for unauthorised request to EIS returns 502 in vaping-stamps-api            |
| XIVA0000401DS | 401         | Scenario for unauthorised request to EIS returns 502 in vaping-stamps-api            |
| GBVA0000403DS | 403         | Scenario for forbidden request to EIS returns 502 in vaping-stamps-api               |
| XIVA0000403DS | 403         | Scenario for forbidden request to EIS returns 502 in vaping-stamps-api               |
| GBVA0000422DS | 422         | Scenario for missing stampsReferenceNumber in ETDS                                   |
| XIVA0000422DS | 422         | Scenario for missing stampsReferenceNumber in ETDS                                   |
| GBVA1000422DS | 422         | Scenario for missing vdsEmail in ETDS                                                |
| XIVA1000422DS | 422         | Scenario for missing vdsEmail in ETDS                                                |
| GBVA0000500DS | 500         | Scenario that returns success response with JSON to trigger 500 in vaping-stamps-api |
| XIVA0000500DS | 500         | Scenario that returns success response with JSON to trigger 500 in vaping-stamps-api |
| GBVA0000502DS | 502         | Scenario that returns 502 from EIS and 502 in vaping-stamps-api                      |
| XIVA0000502DS | 502         | Scenario that returns 502 from EIS and 502 in vaping-stamps-api                      |
| GBVA1000502DS | 500         | Scenario that returns 500 from EIS and 502 in vaping-stamps-api                      |
| XIVA1000502DS | 500         | Scenario that returns 500 from EIS and 502 in vaping-stamps-api                      |
| GBVA2000502DS | 503         | Scenario that returns 503 from EIS and 502 in vaping-stamps-api                      |
| XIVA2000502DS | 503         | Scenario that returns 503 from EIS and 502 in vaping-stamps-api                      |

GBVA0000266DS is a Not Approved example response
*XI* VA0000200DS is a Northern Ireland example response

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
