-- View for user behaviour
CREATE VIEW user_engagement AS
SELECT
    u.ID AS user_id,
    u.bio,
    COUNT(DISTINCT up.number) AS pictures_uploaded,
    COUNT(DISTINCT pl.liking_user) AS likes_received,
    COUNT(DISTINCT uf.user_followed) AS following_count,
    COUNT(DISTINCT uf2.user_following) AS follower_count,
    (COUNT(DISTINCT pl.liking_user) / GREATEST(COUNT(DISTINCT up.number), 1)) AS avg_likes_per_post
FROM
    user u
LEFT JOIN
    uploaded_picture up ON u.ID = up.uploading_user
LEFT JOIN
    picture_like pl ON up.uploading_user = pl.uploading_user AND up.number = pl.picture_number
LEFT JOIN
    user_follow uf ON u.ID = uf.user_following
LEFT JOIN
    user_follow uf2 ON u.ID = uf2.user_followed
GROUP BY
    u.ID, u.bio
HAVING
    COUNT(DISTINCT up.number) > 0 OR COUNT(DISTINCT pl.liking_user) > 0
ORDER BY
    avg_likes_per_post DESC;

-- View for content popularity
CREATE VIEW trending_content AS
SELECT
    up.uploading_user,
    u.profile_picture,
    up.bio AS caption,
    COUNT(pl.liking_user) AS total_likes,
    COUNT(pl.liking_user) / DATEDIFF(NOW(), up.time_uploaded) AS likes_per_day,
    (SELECT AVG(like_count)
     FROM (
       SELECT COUNT(*) AS like_count
       FROM picture_like
       GROUP BY uploading_user, picture_number
     ) AS avg_likes
     ) AS platform_avg_likes
FROM
    uploaded_picture up
JOIN
    user u ON up.uploading_user = u.ID
LEFT JOIN
    picture_like pl ON up.uploading_user = pl.uploading_user AND up.number = pl.picture_number
WHERE
    up.time_uploaded > NOW() - INTERVAL 30 DAY
GROUP BY
    up.uploading_user, u.profile_picture, up.bio, up.time_uploaded
HAVING
    COUNT(pl.liking_user) > 0
ORDER BY
    likes_per_day DESC;

-- View for system analytics
CREATE VIEW platform_growth_metrics AS
SELECT
    DATE(time_uploaded) AS day,
    COUNT(DISTINCT uploading_user) AS active_uploaders,
    COUNT(*) AS new_posts,
    (SELECT COUNT(*) FROM user_follow
     WHERE DATE(time) = DATE(up.time_uploaded)) AS new_follows,
    (SELECT COUNT(DISTINCT liking_user) FROM picture_like
     WHERE DATE(time) = DATE(up.time_uploaded)) AS active_likers,
    ROUND(COUNT(*) / GREATEST(COUNT(DISTINCT uploading_user), 1), 2) AS avg_posts_per_user,
    (SELECT ROUND(COUNT(*) / GREATEST(COUNT(DISTINCT user_following), 1), 2)
     FROM user_follow WHERE DATE(time) = DATE(up.time_uploaded)) AS avg_follows_per_user
FROM
    uploaded_picture up
GROUP BY
    DATE(time_uploaded)
HAVING
    COUNT(*) > 1 OR
    (SELECT COUNT(*) FROM user_follow WHERE DATE(time) = DATE(up.time_uploaded)) > 0
ORDER BY
    day DESC;