
# vaping-stamps-api-stub

The Vaping Stamps API stub is a service designed to support stateful sandbox testing of the Vaping Duty Stamps – Approval Status Check API in the External Test environment. It stubs the behaviour of the Vaping Duty Stamps – Approval Status Check API microservice.

The workflow to trigger the stateful test service is as follows:
1. Submit an explicit POST request to the stub endpoint using the test data of a specific stampsReferenceNumber.
2. The stub service processes the request and dynamically generates a pre-defined test data response for that specific stampsReferenceNumber.
3. The stub returns the appropriate status code with a JSON body or the error details.

## Who/What uses this service?

This stub service is used by the Vaping Duty Stamps API microservice that makes calls to EIS/ETDS which is deployed to the External Test environment. The microservice should be configured to connect to this stub instead of a real EIS/ETDS data.

## Running the service locally

To run the service locally on port 7012:

sbt 'run 7012'

To test the stub endpoints for Vaping Stamps

POST
curl -X POST "http://localhost:7012/etds/vaping/stamps/status" \
-H "Content-Type: application/json" \
-H "Accept: application/vnd.hmrc.1.0+json" \
-d '{ "vdsEmail": "test@test.com", "stampsReferenceNumber": "GBVC0000200DS" }'

### Example stub data
| Stamps Reference Number | Status code | Description                                                                          |
|-------------------------|-------------|--------------------------------------------------------------------------------------|
| GBVC0000200DS           | 200         | Scenario for successful response with approvalStatus = APPROVED                      |
| GBVC0000266DS           | 200         | Scenario for successful response with approvalStatus = NOT_APPROVED                  |
| GBVC0000401DS           | 401         | Scenario for unauthorised request to EIS returns 502 in vaping-stamps-api            |
| GBVC0000403DS           | 403         | Scenario for forbidden request to EIS returns 502 in vaping-stamps-api               |
| GBVC0000422DS           | 422         | Scenario for missing stampsReferenceNumber in ETDS                                   |
| GBVC1000422DS           | 422         | Scenario for missing vdsEmail in ETDS                                                |
| GBVC2000422DS           | 422         | Scenario for repeated calls using the same stampsReferenceNumber                     |
| GBVC0000500DS           | 500         | Scenario that returns success response with JSON to trigger 500 in vaping-stamps-api |
| GBVC0000503DS           | 502         | Scenario that returns 502 from EIS and 502 in vaping-stamps-api                      |
| GBVC1000503DS           | 500         | Scenario that returns 500 from EIS and 502 in vaping-stamps-api                      |
| GBVC2000503DS           | 503         | Scenario that returns 503 from EIS and 502 in vaping-stamps-api                      |

**GB**VC0000200DS is the standard example response in the UK

**XI**VC0000200DS is an example response where **XI** always indicates Northern Ireland

GBVC0000**266**DS is a Not Approved example response which still returns the status code of 200

GBV**C**0000200DS is an example response where **C** can be changed to other acceptable chars


### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
