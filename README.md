## Twitter Mini
This project represents "Twitter API," is a Java-based application developed using the Spring Boot framework and Gradle as the build tool. The primary goal of the project is to demonstrate the integration of Twitter API functionality and serve as a foundation for building applications that interact with Twitter's services.

**Key Features**
- *Authentication:* The project implements a robust authentication system, allowing users to log in securely.

- *Tweet Retrieval:* Users can retrieve tweets from Twitter's API based on specific criteria or user profiles.

- *Tweet Posting:* The application supports posting tweets, enabling users to share their thoughts or content seamlessly.

- *User Interaction:* Users can follow and unfollow other Twitter accounts, fostering social interactions.

### Technologies Used

The project is built with the following major frameworks and technologies:

- *Spring Boot:* A powerful and convention-over-configuration framework for Java applications.

- *Gradle:* A modern, versatile build tool for Java and Groovy projects.

- *MongoDB:* A NoSQL database used for storing application data.

- *Groovy:* A dynamic language for the Java Virtual Machine, utilized for various aspects of the project.

- *Spock Framework:* A testing and specification framework for Java and Groovy applications.

### Get started

### Install docker and docker compose for your platform:
- #### [Linux](https://docs.docker.com/desktop/install/linux-install/)
- #### [MacOS](https://docs.docker.com/desktop/install/mac-install/)
- #### [Windows](https://docs.docker.com/desktop/windows/wsl/)

### Repo installation
- Clone the repo
   ```sh
   git clone https://github.com/RostikYakunin/Twitter-API
   ```

### General container usage
- #### [DOCKER COMPOSE DOCUMENTATION](https://docs.docker.com/compose/reference/)
- To build and run use: `"docker compose up"`, all logs will be streamed in terminal. <br>
  To stop all containers press: `"CTRL+C"`.

> NOTE: Database saves it state between runs.

### Config
All app configs are saved in `"src/main/resources/application.yaml"`

### Some examples

#### 1. Register a new user using authentication
To register a new user, make a POST request to the /api/v1/auth/ endpoint with a JSON payload containing the username, email, and password
```
POST /api/v1/auth/login/ 

{
    "username": "user"
    "email": "user@example.com",
    "password": "your_password",
}
```

#### 2. Create a new user manually
To register a new user, make a POST request to the /api/v1/users/ endpoint with a JSON payload containing the username, email, and password
```
POST /api/v1/users/ 

{
    "username": "user"
    "email": "user@example.com",
    "password": "your_password",
}
```

#### 2. Find post by id
To find a post, make a GET request to the /api/v1/posts/ endpoint and type path variable '{id}' (post`s id) 
```
GET /api/v1/posts/{id} 
```

If post with this id is exists, you will receive information in JSON format like this: 

```
{
"id": "string_value",
"content": "content_value",
"authorId": "authorId_value",
"likesId": [],
"commentsId": []
}
```
