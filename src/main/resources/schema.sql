CREATE TABLE IF NOT EXISTS mpa (
    mpaId INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    mpaName VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS films (
    film_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL,
    description VARCHAR(200),
    release_date DATE,
    duration INTEGER CHECK (duration > 0),
    mpa_id INTEGER REFERENCES mpa (mpaId) ON DELETE RESTRICT
);


CREATE TABLE IF NOT EXISTS genres (
    genreId INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genreName VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id BIGINT  NOT NULL REFERENCES films (film_id) ON DELETE CASCADE,
    genre_id INTEGER NOT NULL REFERENCES genres (genreId) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL,
    UNIQUE(email), UNIQUE(login)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id BIGINT not null REFERENCES films (film_id) ON DELETE CASCADE,
    user_id BIGINT not null REFERENCES users (user_id) ON DELETE CASCADE
);



CREATE TABLE IF NOT EXISTS friends (
    PRIMARY KEY (user_id, friend_id),
    user_id BIGINT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    friend_id BIGINT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE
);