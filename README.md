# Social Media Management Tool

A Spring Boot backend application for managing social media accounts with OAuth2 authentication and Facebook Graph API integration.

## Features

- Google OAuth2 authentication
- JWT token-based API security
- Facebook page linking and management
- Post publishing to Facebook pages
- MySQL database for persistent storage

## Tech Stack

- Java 17
- Spring Boot 3.4.0
- Spring Security with OAuth2
- JWT (jjwt 0.11.5)
- MySQL 8.0
- Facebook Graph API v19.0

## Prerequisites

- JDK 17 or higher
- MySQL 8.0+
- Maven 3.6+
- Google OAuth2 credentials
- Facebook Developer account (optional, for testing Facebook features)

## Setup

### 1. Database Setup

Create the MySQL database:

```sql
CREATE DATABASE flintzy;
```

Import the schema:

```bash
mysql -u root -p flintzy < flintzy_schema.sql
```

### 2. Google OAuth Configuration

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create OAuth 2.0 credentials
3. Add redirect URI: `http://localhost:8080/login/oauth2/code/google`
4. Update `application.yml` with your client ID and secret

### 3. Application Configuration

Update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    password: your_mysql_password

  security:
    oauth2:
      client:
        registration:
          google:
            client-id: your_google_client_id
            client-secret: your_google_client_secret
```

### 4. Build and Run

```bash
mvn clean install
mvn spring-boot:run
```

The application starts on `http://localhost:8080`

## API Documentation

### Authentication

**Login with Google**

```
GET http://localhost:8080/oauth2/authorization/google
```

This redirects to Google login. After authentication, you'll receive a JWT token on the success page.

### Facebook APIs

All endpoints require JWT authentication via `Authorization: Bearer <token>` header.

**Get linked Facebook pages**

```bash
curl -X GET http://localhost:8080/api/facebook/pages \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Link Facebook pages**

```bash
curl -X POST http://localhost:8080/api/facebook/link-pages \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"userAccessToken": "FACEBOOK_USER_ACCESS_TOKEN"}'
```

**Publish a post**

```bash
curl -X POST http://localhost:8080/api/facebook/post \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "pageId": "YOUR_PAGE_ID",
    "message": "Your post message here"
  }'
```

## Testing

1. Start the application
2. Open browser and navigate to `http://localhost:8080/oauth2/authorization/google`
3. Login with Google
4. Copy the JWT token from the success page
5. Use the token to test API endpoints with curl or Postman

### Getting Facebook Access Token

To test Facebook features:
1. Go to [Facebook Graph API Explorer](https://developers.facebook.com/tools/explorer/)
2. Select your app
3. Request permissions: `pages_show_list`, `pages_manage_posts`
4. Generate and copy the access token

## Project Structure

```
src/main/java/com/flintzy/socialtool/
├── config/
│   ├── AppConfig.java
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   └── FacebookController.java
├── model/
│   ├── User.java
│   └── SocialAccount.java
├── payload/
│   ├── ApiResponse.java
│   ├── FacebookPagesResponse.java
│   ├── LinkPagesRequest.java
│   └── PostRequest.java
├── repository/
│   ├── UserRepository.java
│   └── SocialAccountRepository.java
├── security/
│   ├── JwtFilter.java
│   ├── JwtUtil.java
│   └── OAuth2SuccessHandler.java
├── service/
│   └── FacebookService.java
└── SocialToolApplication.java
```

## Database Schema

### users
- id (BIGINT, PK)
- name (VARCHAR)
- email (VARCHAR, UNIQUE)
- provider (VARCHAR)
- provider_id (VARCHAR)

### social_accounts
- id (BIGINT, PK)
- page_id (VARCHAR)
- page_name (VARCHAR)
- page_access_token (VARCHAR)
- platform (VARCHAR)
- user_id (BIGINT, FK)

## Troubleshooting

**401 Unauthorized**
- Verify JWT token is valid
- Check Authorization header format: `Bearer <token>`

**Database connection error**
- Ensure MySQL is running
- Verify credentials in application.yml
- Check if database exists

**OAuth redirect error**
- Verify redirect URI in Google Console matches: `http://localhost:8080/login/oauth2/code/google`
- Clear browser cache and try again

## Notes

- JWT tokens expire after 10 days
- Facebook user access tokens are short-lived (need to be refreshed)
- Application uses stateless session management
