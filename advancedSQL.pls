-- Find movies that are from an earlier year than the movie "ET" (id 112).
SELECT * FROM movie WHERE to_char(year, 'YYYY') < (SELECT to_char(year, 'YYYY') FROM movie WHERE id = 112);

SELECT * FROM movie WHERE year < to_date((SELECT to_char(year, 'YYYY') FROM movie WHERE id = 112), 'YYYY');

SELECT to_date((SELECT to_char(year, 'YYYY') FROM movie WHERE id = 112), 'YYYY') FROM dual;

-- 2) Find all media from movie "ET".
-- (a) Use a simple sub-query,
SELECT * FROM media WHERE movie_id IN ( SELECT id FROM movie WHERE title = 'ET' );

--(b) use EXISTS
SELECT * FROM media m WHERE EXISTS ( SELECT * FROM movie WHERE title = 'ET' AND m.movie_id = movie.id );

-- 3) Find all customers who never loaned a movie/media
SELECT * FROM customer c WHERE NOT EXISTS ( SELECT * FROM rental WHERE mem_no = c.mem_no );

-- 4) Find all customers who did not have a movie on loan after the 5th May 2002.
SELECT * FROM customer c WHERE NOT EXISTS ( SELECT * FROM rental WHERE mem_no = c.mem_no AND ( until_date > to_date('05-05-2002', 'DD-MM-YYYY') OR until_date IS NULL ) );

-- 5) Find all customers who did not have any loans in 2002.
SELECT * FROM customer c WHERE NOT EXISTS ( SELECT * FROM rental WHERE mem_no = c.mem_no AND from_date = to_date('2002', 'YYYY') );

-- 6) Find the cheapest movie by director "Kubrick"
SELECT m.* FROM movie m WHERE pricepday <= ALL ( SELECT pricepday FROM movie) AND director = 'Kubrick';

-- 7) Find all movies that appear in all possible media formats (use counter examples!)
-- SELECT count(DISTINCT format) as type_count FROM media;
-- SELECT movie_id, count(DISTINCT format) FROM media GROUP BY movie_id;
SELECT m.* FROM movie m WHERE (SELECT count(DISTINCT format) FROM media WHERE movie_id = m.id) = (SELECT count(DISTINCT format) FROM media);

-- 8)  Find a customer whose rented movies all have category “scifi“
-- NO ALL
SELECT * FROM customer c 
  WHERE (SELECT count(*) FROM rental r WHERE NOT EXISTS (SELECT * FROM media me WHERE EXISTS (SELECT * FROM movie WHERE category = 'scifi' AND me.movie_id = id) AND r.media_id = id) AND mem_no = c.mem_no) = 0 
  AND EXISTS (SELECT * FROM rental WHERE mem_no = c.mem_no);
-- with ALL
SELECT * 
  FROM customer c 
  WHERE 'scifi' = ALL(
    SELECT m.category 
    FROM rental r, movie m, media me 
    WHERE me.movie_id = m.id 
    AND r.media_id = me.id 
    AND r.mem_no = c.mem_no
  ) 
  AND EXISTS (SELECT * FROM rental WHERE mem_no = c.mem_no);

-- 9) Customers that had rented all media
-- SELECT count(DISTINCT format) FROM media;
-- SELECT count(DISTINCT format) FROM rental r, media m WHERE m.id = r.media_id;
SELECT * FROM customer c WHERE (SELECT count(DISTINCT format) FROM rental r, media m WHERE m.id = r.media_id AND mem_no = c.mem_no) = (SELECT count(DISTINCT format) FROM media);

-- 10) Find the customer who rented all available "BluRay" media
-- all available BluRay media
-- SELECT * FROM media WHERE format = 'BluRay';
SELECT * FROM customer c 
  WHERE (
    SELECT count(DISTINCT media_id) FROM rental r 
    WHERE EXISTS ( SELECT * FROM media WHERE format = 'BluRay' AND id = r.media_id ) 
    AND r.mem_no = c.mem_no
  ) = (SELECT count(*) FROM media WHERE format = 'BluRay');


-- Grouping/Having

-- 1) Find all movies with price same as the maximum price
SELECT * FROM movie WHERE pricepday >= ALL(SELECT pricepday FROM movie);

-- 2) Find all "scifi" movies with more than 2 media
SELECT * 
  FROM movie m 
  WHERE category = 'scifi' 
  AND EXISTS (SELECT movie_id FROM media me WHERE m.id = me.movie_id GROUP BY movie_id HAVING count(*) > 2);

-- 3) Find all movies that appear in all possible media formats (use aggregation!)
SELECT * 
  FROM movie m 
  WHERE EXISTS (
    SELECT movie_id 
    FROM media 
    WHERE movie_id = m.id 
    GROUP BY movie_id 
    HAVING count(DISTINCT format) = (SELECT count(DISTINCT format) FROM media)
  );

-- 4) Show the number of media that have been on loan for each customer
SELECT c.mem_no, (SELECT count(DISTINCT media_id) FROM rental WHERE until_date IS NULL AND mem_no = c.mem_no) as on_loan 
  FROM customer c;

-- 5) Show the number of media for each customer, only for movies from after 1980
SELECT c.mem_no, (
  SELECT count(DISTINCT media_id) FROM rental r 
    WHERE until_date IS NULL 
    AND mem_no = c.mem_no 
    AND EXISTS (
      SELECT * FROM media me 
      WHERE EXISTS (SELECT * FROM movie m WHERE year > to_date('1980', 'YYYY') AND m.id = me.movie_id) 
      AND r.media_id = me.id
    )
  ) as on_loan FROM customer c;

-- 6) Show the number of media for each customer for those who loaned more then 2 media
SELECT c.mem_no, l.on_loan FROM customer c 
  JOIN (
    SELECT mem_no, count(DISTINCT media_id) as on_loan 
    FROM rental 
    WHERE until_date IS NULL 
    GROUP BY mem_no
  ) l 
  ON c.mem_no = l.mem_no 
  WHERE l.on_loan > 2;

-- 7) Show the number of media for each customer for those who loaned more then 2 media after the 2nd May 2002
SELECT c.mem_no, l.on_loan FROM customer c 
  JOIN (
    SELECT mem_no, count(DISTINCT media_id) as on_loan 
    FROM rental 
    WHERE until_date IS NULL 
    AND from_date > to_date('02-05-2002', 'DD-MM-YYYY') 
    GROUP BY mem_no
  ) l 
  ON c.mem_no = l.mem_no WHERE l.on_loan > 2;

-- 8) Show the directors and the number of their movies
SELECT director, count(id) as number_of_movie 
  FROM movie 
  GROUP BY director;

-- 9) Show the directors and the number of their movies from after 1980
SELECT director, count(id) as number_of_movie 
  FROM movie 
  WHERE year > to_date('1980', 'YYYY') 
  GROUP BY director;

-- 10) Show the directors and the number of their movies for which more than 2 media exist
SELECT m.director, count(me.id) as number_of_media 
  FROM movie m JOIN media me 
  ON me.movie_id = m.id 
  GROUP BY director HAVING count(me.id) > 2;



