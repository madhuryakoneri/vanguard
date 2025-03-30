# Trade Reporting Application

## Introduction:

The Trade Reporting Application loads all trade events into the database during application startup. Once running,
users can fetch filtered events through the provided API endpoint.

### Key Features:

**Automatic Event Ingestion:** Events are parsed from XML files and stored in the database at startup.

**Filtering API:** The endpoint /api/events/filteredEvents allows retrieving filtered trade events.

**H2 In-Memory Database:** Used for simplicity and easy development.

**Efficient Data Parsing:** Only required fields from events.xml files are parsed as per the requirement document.

**Flexible Filtering:** Uses JPA CriteriaBuilder to add dynamic filters, allowing future extensions.

**Optimized Anagram Filtering:** Performed after fetching data to avoid additional storage overhead.

**Secure Credentials Handling:** Database credentials are loaded from environment variables instead of being hardcoded.

**Prerequisites:**

Java 17+

Maven 3.6+

Git

Postman or any API testing tool (optional, for testing API endpoints)

## How to Run the Application

1. **Clone the Repository**

 git clone <repository-url>
 cd tradereporting

2. **Set Environment Variables**

Before running the application, set the following environment variables:

USER_NAME=test
PASSWORD=test
Since an H2 in-memory database is used, you can provide any username and password.

3. **Build the Project**

mvn clean install

4. **Run the Application**

mvn spring-boot:run

Or run the TradereportingApplication.java file from your IDE.

5. **Verify Database Data**

After the application starts, you can verify that event data is inserted into the H2 database by accessing:

http://localhost:8080/h2-console

Use the following credentials in the H2 console:

JDBC URL: jdbc:h2:mem:testdb

Username: test (or the value set in USER_NAME)

Password: test (or the value set in PASSWORD)

6. **Fetch Filtered Events**

The following API endpoint is available for fetching filtered events:

http://localhost:8080/api/events/filteredEvents

Use Postman or a browser to hit the above URL to retrieve events based on the filters specified in the requirement document.

## Assumptions and Trade-offs

An in-memory H2 database is used for simplicity; .

Only required fields are parsed and stored from events.xml files to optimize performance.

Anagram filtering is applied after fetching data to avoid extra database storage for sorted buyer/seller fields.

Database credentials are fetched from environment variables instead of being stored in application.properties for better security.

