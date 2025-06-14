DROP TABLE IF EXISTS cu;
DROP TABLE IF EXISTS user_follow;
DROP TABLE IF EXISTS picture_like;
DROP TABLE IF EXISTS uploaded_picture;
DROP TABLE IF EXISTS user;

CREATE TABLE user (
    ID VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    bio VARCHAR(1000) DEFAULT 'No bio',
    profile_picture VARCHAR(255) DEFAULT 'img/default_profile.png'
);

-- Used to store user-uploaded pictures
CREATE TABLE uploaded_picture (
    uploading_user VARCHAR(255) NOT NULL,
    number INT NOT NULL,
    bio TEXT,
    time_uploaded DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (uploading_user, number),
    FOREIGN KEY (uploading_user) REFERENCES user(ID) ON DELETE CASCADE
);

-- Used to track likes on pictures
CREATE TABLE picture_like (
    liking_user VARCHAR(255) NOT NULL,
    uploading_user VARCHAR(255) NOT NULL,
    picture_number INT NOT NULL,
    number INT NOT NULL,
    time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (liking_user, uploading_user, picture_number, number),
    FOREIGN KEY (liking_user) REFERENCES user(ID) ON DELETE CASCADE,
    FOREIGN KEY (uploading_user, picture_number) REFERENCES uploaded_picture(uploading_user, number) ON DELETE CASCADE
);

-- Used to track follow relationships
CREATE TABLE user_follow (
    user_following VARCHAR(255) NOT NULL,
    user_followed VARCHAR(255) NOT NULL,
    time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_followed, user_following),
    FOREIGN KEY (user_following) REFERENCES user(ID) ON DELETE CASCADE,
    FOREIGN KEY (user_followed) REFERENCES user(ID) ON DELETE CASCADE,
    CHECK (user_following != user_followed)
);

-- Used to track logged-in users
CREATE TABLE cu (
    ID VARCHAR(255) PRIMARY KEY,
    FOREIGN KEY (ID) REFERENCES user(ID) ON DELETE CASCADE
);

-- EXAMPLE INSERT STATEMENTS
-- user example statements
INSERT INTO user (ID, password, bio, profile_picture) VALUES
("Lorin", "rdnpassword", DEFAULT, DEFAULT),
("tyreek", "anotherpass", "Big fan of the miami dolphins", "tyreek_hill.png"),
("goat", "paswordexample", "Some random example text", DEFAULT);

-- uploaded_picture example statements
INSERT INTO uploaded_picture (uploading_user, number, bio, time_uploaded) VALUES
("Lorin", 1, "lovely photo of a yugo", DEFAULT),
("tyreek", 5, "I love the dolphins", DEFAULT),
("goat", 3, "I too love the dolphins", DEFAULT);

-- picture_like example statements
INSERT INTO picture_like (liking_user, uploading_user, picture_number, number, time) VALUES
("tyreek", "goat", 3, 1, DEFAULT),
("goat", "tyreek", 5, 1, DEFAULT),
("Lorin", "goat", 3, 1, DEFAULT);

-- user_follow example statements
INSERT INTO user_follow (user_following, user_followed) VALUES
("Lorin","tyreek"),
("tyreek","Lorin"),
("goat","tyreek"),
("goat","Lorin");

-- current_user example
INSERT INTO cu (ID) VALUES ("Lorin");