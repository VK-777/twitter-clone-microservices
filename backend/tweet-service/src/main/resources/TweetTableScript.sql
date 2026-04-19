CREATE DATABASE IF NOT EXISTS twitter_tweet_db;
USE twitter_tweet_db;

-- 1. Main Tweets Table
CREATE TABLE IF NOT EXISTS tweets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    user_first_name VARCHAR(255) NOT NULL,
    content VARCHAR(280), -- Can be empty if it's just a retweet
    likes_count INT DEFAULT 0,
    retweets_count INT DEFAULT 0,
    replies_count INT DEFAULT 0,
    views_count INT DEFAULT 0,
    parent_tweet_id BIGINT DEFAULT NULL,
    tweet_type ENUM('TWEET', 'RETWEET', 'REPLY') DEFAULT 'TWEET',
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Likes Table
CREATE TABLE IF NOT EXISTS likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    tweet_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_like (user_id, tweet_id)
);

-- 3. Tweet Media Table (Links Tweets to Media MS Blob IDs)
CREATE TABLE IF NOT EXISTS tweet_media (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tweet_id BIGINT NOT NULL,
    media_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tweet_id) REFERENCES tweets(id) ON DELETE CASCADE
);

-- 4. Tweet Hashtags Table (For trending searches)
CREATE TABLE IF NOT EXISTS tweet_hashtags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tweet_id BIGINT NOT NULL,
    tag VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tweet_id) REFERENCES tweets(id) ON DELETE CASCADE
);

-- 5. Tweet Comments Table
CREATE TABLE IF NOT EXISTS tweet_comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tweet_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    comment_text VARCHAR(280) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tweet_id) REFERENCES tweets(id) ON DELETE CASCADE
);