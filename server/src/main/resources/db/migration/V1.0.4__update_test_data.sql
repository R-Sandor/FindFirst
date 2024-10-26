UPDATE tag
SET created_date = CURRENT_DATE
WHERE created_date IS NULL;

UPDATE tag
SET last_modified_date = CURRENT_DATE
WHERE last_modified_date IS NULL;

UPDATE tag
SET last_modified_by = 'system'
WHERE last_modified_by IS NULL;

UPDATE tag
SET created_by = 'system'
WHERE created_by IS NULL;