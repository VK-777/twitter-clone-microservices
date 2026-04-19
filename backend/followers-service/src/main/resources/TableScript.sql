CREATE DATABASE IF NOT EXISTS twitter_follow_db;
USE twitter_follow_db;
CREATE TABLE followers (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   follower_id BIGINT NOT NULL,
   following_id BIGINT NOT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT unique_follow UNIQUE(follower_id, following_id)
);