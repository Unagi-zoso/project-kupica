CREATE TABLE post
(
    post_id    INT PRIMARY KEY AUTO_INCREMENT,
    password   VARCHAR(14) NOT NULL,
    source     VARCHAR(300) NOT NULL,
    caption    TEXT     NOT NULL,
    erase_flag BOOL NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);