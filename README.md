
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
-d '{ "contactEmail": "test@test.com", "vdsApprovalId": "GBVA0000200DS" }'

### Example stub data
|Approval Id  | Status code |
|-------------|-------------|
|GBVA0000200DS| 200         |
|GBVA0000204DS| 204         |
|GBVA0000400DS| 400         |
|GBVA0000401DS| 401         |
|GBVA0000403DS| 403         |
|GBVA0000404DS| 404         |
|GBVA0000409DS| 409         |
|GBVA0000500DS| 500         |
|GBVA0000503DS| 503         |

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
