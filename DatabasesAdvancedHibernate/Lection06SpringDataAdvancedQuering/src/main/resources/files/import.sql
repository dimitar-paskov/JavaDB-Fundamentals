USE `bookshop_system_db`;

DROP PROCEDURE IF EXISTS udp_get_number_of_books_by_author;

DELIMITER $$
CREATE PROCEDURE udp_get_number_of_books_by_author (IN fname VARCHAR(50), lname VARCHAR(50), OUT number_of_books INT)
BEGIN

SET number_of_books =
(SELECT count(*)
FROM books b
JOIN authors a
ON b.author_id = a.id
WHERE a.first_name = fname AND a.last_name = lname);

END; $$
DELIMITER ;