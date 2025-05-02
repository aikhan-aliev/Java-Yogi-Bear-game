-- Create the highscore database
CREATE DATABASE IF NOT EXISTS highscore;

-- Use the correct database
USE highscore;

-- Create the leaderboard table
CREATE TABLE IF NOT EXISTS leaderboard (
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Current time when the score is saved
    name VARCHAR(255) NOT NULL,                   -- Player's name
    collected_baskets INT NOT NULL                -- Number of baskets collected
);

-- Display tables and describe the leaderboard table
SHOW TABLES;
DESCRIBE leaderboard;

-- Select all data from the leaderboard table
SELECT * FROM leaderboard;
