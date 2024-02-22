# GitHub API Controller

A simple REST API controller for retrieving GitHub repositories and branches.

## Table of Contents

- [GitHub API Controller](#github-api-controller)
  - [Table of Contents](#table-of-contents)
  - [About](#about)
  - [Endpoints](#endpoints)
    - [List Repositories](#list-repositories)
  - [Usage](#usage)
  - [Installation](#installation)
## About

The GitHub API Controller is a Spring Boot application designed to interact with the GitHub API to retrieve repositories and their branches for a given user.

## Endpoints

### List Repositories

- **URL**: `/api/github/repositories/{username}`
- **Method**: `GET`
- **Description**: Retrieves a list of repositories for the specified GitHub user.
- **Parameters**:
  - `username` (Path Variable): The GitHub username for which repositories are to be retrieved.
- **Response**:
  - **Success Response**:
    - **Status Code**: `200 OK`
    - **Content**: An array of repository objects containing information about each repository and its branches.
  - **Error Responses**:
    - **Status Code**: `404 Not Found`
      - **Content**: `{ "status": 404, "message": "User not found" }`
    - **Status Code**: `429 Too Many Requests`
      - **Content**: `{ "status": 429, "message": "API rate limit exceeded" }`
    - **Status Code**: `500 Internal Server Error`
      - **Content**: `{ "status": 500, "message": "An unexpected error occurred" }`

## Usage

To use this project, you need to deploy it to a server that supports Spring Boot applications. Once deployed, you can make requests to the defined endpoints to retrieve GitHub repository information.

## Installation

To install and run this project locally, follow these steps:

1. Clone the repository:
```console
git clone https://github.com/PWilkosz99/github-api.git
```

2. Navigate to the project directory:
```console
cd github-api
```

3. Build the project using Gradle:
```console
./gradlew build
```

4. Run the application:
```console
./gradlew bootRun
```

5. The application should now be running locally on port 8080.