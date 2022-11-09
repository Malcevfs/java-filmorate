# java-filmorate
Template repository for Filmorate project.
![This is an image](/src/main/resources/diagram.png)

Запрос для вывода значений таблицы film

SELECT \
fl.film_id,\
fl.name as film_name,\
fl.description,\
fl.releaseDate,\
fl.duration,\
f.name as genre,\
r.name as rate\

FROM film as fl\
INNER JOIN film_genre as f ON fl.film_id=f.film_id\
INNER JOIN film_rate as r ON fl.film_id=r.film_id;\

Запрос для вывода значений таблицы user

SELECT *\
FROM user;\

Запрос для вывода лайков

SELECT
u.user_id
u.name as user_name,
f.name as film_name
FROM user as u
INNER JOIN likes as l ON u.user_id=l.user_id
INNER JOIN film as f ON l.film_id=f.film_id;

Запрос для вывода списка друзей пользователя

SELECT
u.name as user_name
f.name as friend_name

FROM user as u
INNER JOIN friends_request as rq ON u.user_id=rq.user_id
INNER JOIN friends as f ON rq.user_id=f.user_id

