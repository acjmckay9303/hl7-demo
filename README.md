# HL7 Message Converter

This project provides a Java-based solution for converting JSON objects representing veterinary medical data into HL7 (Health Level Seven) messages. HL7 is a set of international standards for the transfer of clinical and administrative data between software applications used by various healthcare providers.

The project includes a Spring Boot application with a REST API endpoint to accept and process JSON objects and return the converted HL7 message.

## Features

- REST API to convert JSON objects containing patient, referring vet, performing vet, study, order, and observation request information into an HL7 OMI_O23 message.
- Serialize the resulting HL7 message into a string for transmission or storage.
- Test the conversion process and ensure accurate data mapping.

## Prerequisites

- Java JDK 17 or later
- Maven 3.6 or later

## Installation

1. Clone the repository:

```bash
git clone https://github.com/your-username/hl7-demo.git
```

2. Change into the project directory:

```bash
cd hl7-demo
```
3. Build the project with Maven:

```bash
mvn clean install
```
## Usage
1. Run the Spring Boot application:

```bash
mvn spring-boot:run
```

2. The REST API endpoint is available at:
```bash
POST http://localhost:8080/api/patient/send-hl7
```

3. Send a POST request to the endpoint with a JSON object containing the required data (see the example JSON object provided in the project). You can use tools like Postman or CURL to test the endpoint:

```bash
curl -X POST -H "Content-Type: application/json" -d '{"patient": {...}, "referringVet": {...}, "performingVet": {...}, "study": {...}, "order": {...}, "observationRequest": {...}}' http://localhost:8080/api/patient/send-hl7
```

4. The API will return the converted HL7 message as a response.

## Testing
To run the tests for the project, execute the following command from the project directory:
```bash
mvn test
```
## Contributing
Please read the CONTRIBUTING.md for details on the process for submitting pull requests and reporting issues.

## License
This project is licensed under the MIT License. See the LICENSE.md file for details.
