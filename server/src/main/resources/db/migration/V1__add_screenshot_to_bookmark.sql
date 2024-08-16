-- Adds a new column for the relative path for screenshots. 
-- Calling url in case we ever switch to cdn.
ALTER TABLE bookmark 
ADD screenshot_url varchar(255);