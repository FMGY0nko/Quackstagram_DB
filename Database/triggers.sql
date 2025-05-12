-- Procedure that updates a user's follower count in their bio
DELIMITER //
CREATE PROCEDURE update_follower_count(IN user_id VARCHAR(255))
BEGIN
    DECLARE follower_count INT;

    SELECT COUNT(*) INTO follower_count
    FROM user_follow
    WHERE user_followed = user_id;

    UPDATE user
    SET bio = CONCAT(IFNULL(SUBSTRING_INDEX(bio, " | Followers:", 1), " | Followers: ", follower_count)
    WHERE ID = user_id;
END //
DELIMITER ;

-- Function that checks whether a user likes their own picture
DELIMITER //
CREATE FUNCTION is_own_picture(
    p_liking_user VARCHAR(255),
    p_uploading_user VARCHAR(255)
)
RETURNS BOOLEAN
DETERMINISTIC
BEGIN
    RETURN p_liking_user = p_uploading_user;
END //
DELIMITER ;

-- Trigger that updates a user's follower count when someone follows them
DELIMITER //
CREATE TRIGGER after_user_follow
AFTER INSERT ON user_follow
FOR EACH ROW
BEGIN
    CALL update_follower_count(NEW.user_followed);

    UPDATE user
    SET bio = CONCAT(IFNULL(bio, ""), " | Following: ",
        (SELECT COUNT(*) FROM user_follow WHERE user_following = NEW.user_following))
    WHERE ID = NEW.user_following;
END //
DELIMITER ;

-- Trigger that prevents a self like
DELIMITER //
CREATE TRIGGER prevent_self_like
BEFORE INSERT ON picture_like
FOR EACH ROW
BEGIN
    IF is_own_picture(NEW.liking_user, NEW.uploading_user) THEN
        SIGNAL SQLSTATE "45000"
        SET MESSAGE_TEXT = "Users cannot like their own pictures";
    END IF;
END //
DELIMITER ;