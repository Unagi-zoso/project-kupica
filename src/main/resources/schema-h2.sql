CREATE TABLE member
(
    member_id       INT PRIMARY KEY AUTO_INCREMENT,
    member_password VARCHAR(16)        NOT NULL,
    member_nickname VARCHAR(16) UNIQUE NOT NULL,
    created_at      DATETIME           NOT NULL,
    updated_at      DATETIME           NOT NULL
);

CREATE TABLE post
(
    post_id    INT PRIMARY KEY AUTO_INCREMENT,
    post_fk_1  INT      NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    foreign key (post_fk_1) references member (member_id)
);

CREATE TABLE mention
(
    mention_id   INT PRIMARY KEY AUTO_INCREMENT,
    mention_fk_1 INT      NOT NULL,
    member_fk_id    INT      NOT NULL,
    content      TEXT     NOT NULL,
    created_at   DATETIME NOT NULL,
    updated_at   DATETIME NOT NULL,
    foreign key (mention_fk_1) references post (post_id)
);

CREATE TABLE photo
(
    photo_id   INT PRIMARY KEY AUTO_INCREMENT,
    photo_fk_1 INT          NOT NULL,
    user_id    INT          NOT NULL,
    source     VARCHAR(300) NOT NULL,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL,
    foreign key (photo_fk_1) references post (post_id)
);

CREATE TABLE like_info
(
    like_info_id   INT PRIMARY KEY AUTO_INCREMENT,
    like_info_fk_1 INT      NOT NULL,
    user_id        INT      NOT NULL,
    created_at     DATETIME NOT NULL,
    updated_at     DATETIME NOT NULL,
    foreign key (like_info_fk_1) references post (post_id)
);