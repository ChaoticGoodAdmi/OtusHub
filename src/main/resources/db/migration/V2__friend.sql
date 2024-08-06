CREATE TABLE IF NOT EXISTS userdb.friends
(
    user_id   VARCHAR NOT NULL,
    friend_id VARCHAR NOT NULL,
    added_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES userdb.users (id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES userdb.users (id) ON DELETE CASCADE
);

CREATE INDEX idx_user_friend ON userdb.friends (user_id, friend_id);