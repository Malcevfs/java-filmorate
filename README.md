# java-filmorate
Template repository for Filmorate project.
![This is an image](/src/main/resources/diagram.png)

**Запрос для вывода значений таблицы film:**

SELECT *\
FROM FILMS\
JOIN FILM_GENRE FG on FILMS.FILM_ID = FG.FILM_ID\
JOIN GENRE G2 on G2.GENRE_ID = FG.GENRE_ID

**Запрос для вывода значений таблицы user:**

SELECT *\
FROM user;

**Запрос для вывода лайков:**

SELECT * from USERS\
JOIN LIKES;

**Запрос для вывода списка друзей пользователей:**

SELECT * from USERS\
join FRIENDS_REQUEST FR on USERS.USER_ID = FR.USER_ID\




