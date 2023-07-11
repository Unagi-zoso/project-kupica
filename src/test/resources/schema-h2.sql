CREATE TABLE post
(
    post_id          INT PRIMARY KEY AUTO_INCREMENT,
    password         VARCHAR(160) NOT NULL,
    source           VARCHAR(300) NOT NULL,
    cached_image_url VARCHAR(300) NOT NULL,
    caption          TEXT     NOT NULL,
    erase_flag       INT NOT NULL,
    download_key     VARCHAR(300) NOT NULL,
    created_at       DATETIME NOT NULL,
    updated_at       DATETIME NOT NULL
);