CREATE DATABASE IF NOT EXISTS twitter_user_db;
USE twitter_user_db;
DROP TABLE users;
CREATE TABLE users(
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	first_name VARCHAR(255) NOT NULL,
	email VARCHAR(255) 	NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL,
	bio VARCHAR(160),
	location VARCHAR(255),
	website VARCHAR(255),
	profile_picture_url VARCHAR(500),
	cover_picture_url VARCHAR(500),
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);




Post
http://localhost:8080/api/v1/users/register
{
    "firstName": "Snehil",
    "email": "Snehil@in",
    "password":"Password@123",
    "confirmPassword":"Password@123"
}

Post
http://localhost:8080/api/v1/users/login
{
    "email": "Snehil@in",
    "password":"Password@123"
}

PUT
http://localhost:8080/api/v1/users/profile
{
    "bio": "Backend Dev",
    "location":"Mysore",
    "website":"github.com/snehil"
}

Get List from prefix
http://localhost:8080/api/v1/users/search

Get User profile
http://localhost:8080/api/v1/users/{id}/profile
