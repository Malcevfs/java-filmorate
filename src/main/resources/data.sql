DELETE FROM users;
ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1;

INSERT INTO users (email,login,name,birthday) VALUES ('1@ya.ru','nikolas1990','Nicolas Cane','1990-10-01');
INSERT INTO users (email,login,name,birthday) VALUES ('rufus1550@mail.ru','AliansThere','Rufus Gonzales','1985-12-12');
INSERT INTO users (email,login,name,birthday) VALUES ('kitty@gmail.com','SweetLady','Sofi Luca','2001-01-07');

DELETE FROM friends_request;

INSERT INTO friends_request (user_id, friend_id, request) VALUES ('1','2','false');
INSERT INTO friends_request (user_id, friend_id, request) VALUES ('2','1','true');
INSERT INTO friends_request (user_id, friend_id, request) VALUES ('3','1','true');
INSERT INTO friends_request (user_id, friend_id, request) VALUES ('2','1','false');

DELETE FROM films;
ALTER TABLE films ALTER COLUMN film_id RESTART WITH 1;

INSERT INTO films (name,description,release_date,duration, mpa_id) VALUES ('Dune','dune description','1990-10-01','150', '1');
INSERT INTO films (name,description,release_date,duration, mpa_id) VALUES ('Tomb Rider','lara is a beautiful girl','2000-05-12','90', '2');
INSERT INTO films (name,description,release_date,duration, mpa_id) VALUES ('Star Wars','tuuu -du - tu du du du du - pam pam pam','1985-07-06','121', '3');

DELETE FROM genre;
ALTER TABLE genre ALTER COLUMN genre_id RESTART WITH 1;

INSERT INTO genre (genre) VALUES ('Comedy');
INSERT INTO genre (genre) VALUES ('Adventure');
INSERT INTO genre (genre) VALUES ('Romantic');
INSERT INTO genre (genre) VALUES ('Drama');

DELETE FROM film_genre;

INSERT INTO film_genre (film_id, genre_id) VALUES ('1','1');
INSERT INTO film_genre (film_id, genre_id) VALUES ('1','2');
INSERT INTO film_genre (film_id, genre_id) VALUES ('2','3');
INSERT INTO film_genre (film_id, genre_id) VALUES ('3','2');

DELETE FROM likes;

INSERT INTO likes(film_id, user_id) VALUES ('1','3');
INSERT INTO likes(film_id, user_id) VALUES ('3','3');
INSERT INTO likes(film_id, user_id) VALUES ('3','1');
INSERT INTO likes(film_id, user_id) VALUES ('3','2');
INSERT INTO likes(film_id, user_id) VALUES ('2','3');
INSERT INTO likes(film_id, user_id) VALUES ('2','1');

DELETE FROM mpa;
ALTER TABLE mpa ALTER COLUMN mpa_id RESTART WITH 1;

INSERT INTO mpa (mpa) VALUES ('G');
INSERT INTO mpa (mpa) VALUES ('PG');
INSERT INTO mpa (mpa) VALUES ('PG-13');
INSERT INTO mpa (mpa) VALUES ('R');
INSERT INTO mpa (mpa) VALUES ('NC-17');






