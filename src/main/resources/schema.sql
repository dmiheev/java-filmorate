CREATE TABLE IF NOT EXISTS mpa_ratings
(
    id            int generated by default as identity primary key,
    name          varchar(255) NOT NULL UNIQUE,
    description   varchar(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres
(
    id       int generated by default as identity primary key,
    name     varchar(255) NOT NULL UNIQUE
); 

CREATE TABLE IF NOT EXISTS users
(
    id       bigint generated by default as identity primary key,
    email    varchar(255) NOT NULL UNIQUE,
    login    varchar(255) NOT NULL UNIQUE,
    name     varchar(255),
    birthday date
);

CREATE TABLE IF NOT EXISTS friends
(
    user_id   bigint REFERENCES users (id),
    friend_id bigint REFERENCES users (id),
    status    boolean
);

CREATE TABLE IF NOT EXISTS films
(
    id            bigint generated by default as identity primary key,
    name          varchar(255) NOT NULL,
    description   varchar(200),
    release_date  date         NOT NULL,
    duration      int          NOT NULL,
    mpa_rating_id int          NOT NULL REFERENCES mpa_ratings (id)
); 

CREATE TABLE IF NOT EXISTS film_likes
(
    film_id  bigint REFERENCES films (id),
    user_id  bigint REFERENCES users (id),
    PRIMARY KEY (film_id, user_id)
); 

CREATE TABLE IF NOT EXISTS film_genres
(
    film_id  bigint REFERENCES films (id),
    genre_id int REFERENCES genres (id),
    PRIMARY KEY (film_id, genre_id)
);