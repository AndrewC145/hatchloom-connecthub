
DROP TABLE IF EXISTS feed_actions CASCADE;
DROP TABLE IF EXISTS classified_posts CASCADE;
DROP TABLE IF EXISTS posts CASCADE;

CREATE TABLE posts (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author INT NOT NULL,
    post_type VARCHAR(50) NOT NULL CHECK (post_type IN ('share', 'announcement', 'achievement')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE classified_posts (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author INT NOT NULL,
    project_id INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'open' CHECK (status IN ('open', 'filled', 'closed')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE feed_actions (
    id SERIAL PRIMARY KEY,
    post_id INT NOT NULL,
    user_id INT NOT NULL,
    action_type VARCHAR(20) NOT NULL CHECK (action_type IN ('like', 'comment')),
    comment_text TEXT,
    parent_action_id INT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_action_id) REFERENCES feed_actions(id) ON DELETE CASCADE,
    CONSTRAINT check_comment_text CHECK (
        (action_type = 'like' AND comment_text IS NULL) OR
        (action_type = 'comment' AND comment_text IS NOT NULL)
    )
);
