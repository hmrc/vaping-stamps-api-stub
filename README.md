
# vaping-stamps-api-stub

The Vaping Stamps API Stub is a service to support stateful sandbox testing in the External Test environment. It stubs the behaviour of the Vaping Stamps API microservice.

It is a semi-stateful test service - in order to use it, you need to request it to set up test data for a specific Vaping Stamps Supplier. It will then generate pre-defined test data for the Supplier. The GET endpoints are called by the relevant API microservices.


## Who/What uses this service?
API microservices that make Vaping Stamps-related calls to DES which are deployed to the External Test environment. The microservices should be configured to connect to this stub instead of a real DES.

API microservices which this stubs behaviour for are: (TBD)

- X 
- Y
- Z

## Running the service locally

To run the service locally on port 7012:

./run_local.sh

To test the stub endpoints for Individual Benefits: (TBD)

curl -X GET http://localhost:7012/year/supplier/XXXXXX/

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").



