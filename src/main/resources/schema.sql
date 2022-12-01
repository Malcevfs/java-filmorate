create table IF NOT EXISTS  users (

    user_id  integer not null primary key auto_increment,
    email varchar(100),
    login varchar(100),
    name varchar(100),
    birthday date,
    constraint USERS_PK primary key (user_id)
    );

CREATE TABLE IF NOT EXISTS friends_request (

    user_id integer not null,
    friend_id integer not null,
    request boolean,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS  genre (
genre_id integer not null primary key auto_increment,
genre varchar(25) not null,
constraint GENRE_ID_PK primary key (genre_id)

);

CREATE TABLE IF NOT EXISTS  mpa (
    mpa_id  integer not null primary key auto_increment,
    name varchar(25) not null,
     constraint MPA_ID_PK primary key (mpa_id)

);

CREATE TABLE IF NOT EXISTS films (

    film_id integer not null primary key auto_increment,
    name varchar(100),
    description varchar(255),
    release_date date,
    duration integer,
    rate bigint,
    mpa_id integer,
    FOREIGN KEY (mpa_id) REFERENCES mpa (mpa_id) ON DELETE CASCADE,
    constraint FILMS_PK primary key (film_id)
);


CREATE TABLE IF NOT EXISTS  film_genre (
    genre_id integer not null,
    film_id integer not null,
    FOREIGN KEY (film_id) REFERENCES films (film_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) references genre (genre_id) ON DELETE CASCADE
--     PRIMARY KEY (genre_id,film_id)
 );

CREATE TABLE IF NOT EXISTS likes (
film_id integer not null,
user_id integer not null,
FOREIGN KEY (film_id) REFERENCES films (film_id) ON DELETE CASCADE,
FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
-- PRIMARY KEY (film_id, user_id)

);




