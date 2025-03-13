1. Prerequisites
Java 17+ (OpenJDK or Oracle JDK)

Maven (for dependency management and builds)

MySQL (for production) and H2 Database (for testing)

Postman (for API testing)

2. Running the Application
Testing (H2 Database)

Clone the Repository:
git clone <repository-url>
cd library_management_system


Run with Maven:
mvn spring-boot:run -Dspring.profiles.active=default

Access H2 Console:
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:librarydb
Username: sa
Password: (leave empty)

Production (MySQL)

Set Up MySQL:
Create a database named library_db.

Update application.properties with your MySQL credentials:
spring.datasource.url=jdbc:mysql://localhost:3306/library_db
spring.datasource.username=root
spring.datasource.password=your_password

Run the Application:
mvn spring-boot:run -Dspring.profiles.active=production

3. API Endpoints
Authentication
Basic Authentication is required for all /api/** endpoints.

Default credentials:
Username: admin
Password: admin123

Books:
Endpoint	         Method	  Description	
/api/books	        GET	    Get all books	
/api/books/{id}	    GET	    Get a book by ID	
/api/books	        POST	  Add a new book	
/api/books/{id}	    PUT	    Update a book by 
/api/books/{id}	   DELETE	  Delete a book by ID	

Patrons:
Endpoint	         Method	  Description	
/api/patrons        GET	    Get all patrons	
/api/patrons/{id}	  GET	    Get a patron by ID	
/api/patrons	      POST	  Add a new patron	
/api/patrons/{id}	  PUT	    Update a patron by ID	See below
/api/patrons/{id}	 DELETE	  Delete a patron by ID	

Borrowing:
Endpoint	                             Method	  Description
/api/borrow/{bookId}/patron/{patronId}	POST	  Borrow a book
/api/return/{bookId}/patron/{patronId}	PUT	    Return a borrowed book

4. Example Requests
Book
// POST /api/books
{
  "title": "The Great Gatsby",
  "author": "F. Scott Fitzgerald",
  "publicationYear": "1925",
  "isbn": "978-0743273565"
}

Patron
// POST /api/patrons
{
  "name": "John Doe",
  "email": "john@example.com",
  "phoneNumber": "+1234567890"
}


5. Testing with Curl
   
Get All Books (Authenticated)
curl -u admin:admin123 http://localhost:8080/api/books

Borrow a Book
curl -u admin:admin123 -X POST http://localhost:8080/api/borrow/1/patron/1

6. Features
a-Caching:
Book and patron details are cached for 10 minutes.
Cache is automatically updated on modifications.

b-Logging:
Method calls, execution times, and exceptions are logged (check application logs).

c-security:
create basic authentication to protect the API endpoints.

7. Troubleshooting
Issue	Solution
401 Unauthorized	Include Basic Auth headers (-u username:password in curl).
404 Not Found	Verify the endpoint URL and resource ID.
500 Internal Server Error	Check application logs for detailed error messages.
H2 Console not accessible	Ensure spring.h2.console.enabled=true in application.properties.

8. Security Best Practices (Production)
Change Default Credentials:

Update spring.security.user.name and spring.security.user.password in application.properties.

Use HTTPS:

Enable SSL/TLS for secure communication.

Database Security:

Restrict MySQL user permissions to SELECT, INSERT, UPDATE, DELETE.


9. Contact
For issues or questions, contact:
Mohammad Amer Alhomsi
Email: en.ameralhomsi@gmail.com
GitHub: https://github.com/ENG-AmerAlhomsi
