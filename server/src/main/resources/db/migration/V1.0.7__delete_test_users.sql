DELETE FROM users WHERE username in ('jsmith', 'king');
DELETE FROM bookmark_tag WHERE bookmark_id IN (SELECT id FROM bookmark WHERE user_id in (1, 2));
DELETE FROM bookmark_tag WHERE tag_id IN (SELECT id FROM tag WHERE user_id in (1, 2));
DELETE FROM tag where user_id = 1;
DELETE FROM tag where user_id = 2;
DELETE FROM bookmark where user_id = 1;
DELETE FROM bookmark where user_id = 2;

DELETE FROM token where user_id in (1, 2);
