ALTER TABLE bookmark 
RENAME COLUMN tenant_id to user_id; 

ALTER TABLE tag 
RENAME COLUMN tenant_id to user_id; 

ALTER TABLE users
DROP COLUMN tenant_id;
