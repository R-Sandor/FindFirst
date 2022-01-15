DROP TABLE IF EXISTS public.bookmark;
DROP TABLE IF EXISTS public.tag;


CREATE TABLE public.bookmark (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    url VARCHAR(50) NOT NULL
);


-- tag --
CREATE TABLE public.tag (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  tag_title VARCHAR(50) NOT NULL
);
-- ALTER TABLE public.bookmark_tag
--   ADD CONSTRAINT fk_tag_bookmark_id FOREIGN KEY (id) REFERENCES public.bookmark(id);
-- CREATE UNIQUE INDEX ix_bookmark_tag_tag_title ON public.tag(tag_title);

CREATE TABLE public.bookmark_tag (
    bookmark_id INT,
    tag_id INT
);