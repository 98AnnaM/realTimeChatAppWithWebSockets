# Chat App Springboot Application

## How to run it
There is a docker-compose.yml in directory /local.
In the app there is one registered user with username: "amileva", password: "12345", role: ADMIN.
The app is running on http://localhost:8080
The mini service for mock data generation is running here: http://localhost:8082/quotes-service/quote

## The app contains:
1. CRUD operations with users.
2. Search users by different criteria (CriteriaQuery and CriteriaBuilder).
3. Logging with slf4j logger.
4. Configuration - maximum 5 users can be stored in the database.
5. Exception handling.
6. Unit and integration tests.
7. Real-time messages with WebSockets - shows users who are ONLINE, shows notification when a new message is received.
8. Swagger for the REST methods in the controllers (ChatController findChatMessages, UserStatusController findConnectedUsers)
9. Authorization with JWT stored in a cookie with name "jwtToken".
10. Communication with a mini service (mocked_quote_service) for mock data generation wrapped in the same docker-compose.
11. Docker images for the two services uploaded in Docker Hub.
12. Docker-compose.yml that includes 3 services that communicates to each other - postgres, chat-app, mocked-quote-service.
