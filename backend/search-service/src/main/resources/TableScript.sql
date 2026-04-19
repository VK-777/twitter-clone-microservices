CREATE TABLE trending_hashtags (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   hashtag VARCHAR(100),
   tweet_count INT,
   last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);