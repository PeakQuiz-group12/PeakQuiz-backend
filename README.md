# About PeakQuiz # 
PeakQuiz is an educational quiz application meant for sharing and playing quizzes. It was created during the spring semester of 2024 at NTNU for the course IDATT2105. This repository serves as the server API of the application.

## Requirements ## 
To run the application, you need to have the following installed:
- Java 21
- Maven
- Docker
- Make
## Getting started ##
1. Clone the repository:
```
git@github.com:PeakQuiz-group12/PeakQuiz-backend.git
```
2. Navigate to the repository root:
```
cd PeakQuiz-backend
```
3. Build the application (make sure Docker is running):
```
make build
```
4. Run the application. 
```
make run
```
The application now runs on port 8080.
## Other commands ##
- ```make test``` Run all tests
- ```make format``` Format source code
- ```make install``` Install the project
- ```make help``` Get list of commands

## Test data ##
The database is populated with a test user. 
```
- Username: sa
- Password: password
```

## Accessing the database
To log into the database, paste this URL into your browser
```
localhost:8080/console
```
Change the ```JDBC URL``` field to 
```
jdbc:h2:file:./data/myDB
```
Credentials: 
```
- Username: sa
- Password: password
```

## Documentation
The API is documented with [swagger](https://swagger.io/tools/swagger-ui/). You can find more information about this and other documentation in the [wiki](https://github.com/PeakQuiz-group12/PeakQuiz-backend/wiki).

## License
Distributed under the MIT License. See ```LICENSE.txt``` for more information.

