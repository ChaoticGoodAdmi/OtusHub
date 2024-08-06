CREATE TABLE IF NOT EXISTS userdb.posts
(
    id         SERIAL PRIMARY KEY,
    user_id    VARCHAR NOT NULL,
    content    TEXT    NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES userdb.users (id) ON DELETE CASCADE
);

CREATE INDEX idx_post_user_id ON userdb.posts (user_id);