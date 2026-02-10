
# vaping-stamps-api-stub

The Vaping Stamps API Stub is a service to support stateful sandbox testing in the External Test environment. It stubs the behaviour of the Vaping Stamps API microservice.

It is a semi-stateful test service - in order to use it, you need to request it to set up test data for a specific Vaping Stamps Supplier. It will then generate pre-defined test data for the Supplier. The GET endpoints are called by the relevant API microservices.


## Who/What uses this service?
API microservices that make Vaping Stamps-related calls to IES/ETDS which are deployed to the External Test environment. The microservices should be configured to connect to this stub instead of a real EIS/ETDS.

## Running the service locally

To run the service locally on port 7012:

sbt 'run 7012'

To test the stub endpoints for Vaping Stamps

curl -X GET http://localhost:7012/etds/vaping/stamps/XXXXXXXXXXX/status

### Example stub data
Approval Id             Status code
AAAA0000200BB           200
AAAA0000204BB           204
AAAA0000400BB           400
AAAA0000401BB           401
AAAA0000403BB           403
AAAA0000404BB           404
AAAA0000409BB           409
AAAA0000500BB           500
AAAA0000503BB           503

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").


