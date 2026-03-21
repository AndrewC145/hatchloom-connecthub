DROP TABLE IF EXISTS classified_subscriptions CASCADE;
DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS conversations CASCADE;
DROP TABLE IF EXISTS feed_actions CASCADE;
DROP TABLE IF EXISTS classified_post_applications CASCADE;
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
    assigned_to INT,
    status VARCHAR(20) NOT NULL DEFAULT 'open' CHECK (status IN ('open', 'filled', 'closed')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE classified_post_applications (
    id SERIAL PRIMARY KEY,
    classified_post_id INT NOT NULL,
    applicant_id INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'APPLIED' CHECK (status IN ('APPLIED', 'ACCEPTED', 'REJECTED')),
    applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(classified_post_id, applicant_id),
    FOREIGN KEY (classified_post_id) REFERENCES classified_posts(id) ON DELETE CASCADE
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

CREATE TABLE conversations (
    id SERIAL PRIMARY KEY,
    user1_id INT NOT NULL,
    user2_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user1_id, user2_id),
    CONSTRAINT check_user_order CHECK (user1_id < user2_id)
);

CREATE TABLE messages (
    id SERIAL PRIMARY KEY,
    conversation_id INT NOT NULL,
    sender_id INT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (conversation_id) REFERENCES conversations(id) ON DELETE CASCADE
);

CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    recipient_user_id INT NOT NULL,
    sender_user_id INT NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN ('MESSAGE', 'CLASSIFIED_CREATED')),
    message TEXT NOT NULL,
    classified_post_id INT,
    conversation_id INT,
    message_id INT,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP,
    FOREIGN KEY (classified_post_id) REFERENCES classified_posts(id) ON DELETE CASCADE,
    FOREIGN KEY (conversation_id) REFERENCES conversations(id) ON DELETE CASCADE,
    FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE
);

CREATE TABLE classified_subscriptions (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


