SET SEARCH_PATH TO max_schema;

CREATE TABLE role (
  id        SERIAL PRIMARY KEY,
  role_name CHARACTER VARYING(64) NOT NULL UNIQUE
);

CREATE TABLE sex (
  id       SERIAL PRIMARY KEY,
  sex_name CHARACTER VARYING(64) NOT NULL UNIQUE
);

ALTER TABLE gender
  RENAME COLUMN sex_name TO name;

CREATE TABLE country (
  id           BIGSERIAL PRIMARY KEY,
  country_name CHARACTER VARYING(128) NOT NULL UNIQUE
);

ALTER TABLE country
  RENAME COLUMN country_name TO name;

CREATE TABLE city (
  id        BIGSERIAL PRIMARY KEY,
  city_name CHARACTER VARYING(128) NOT NULL
);

ALTER TABLE city
  RENAME COLUMN city_name TO name;

CREATE TABLE "user" (
  id         BIGSERIAL PRIMARY KEY,
  name       CHARACTER VARYING(64)  NOT NULL,
  email      CHARACTER VARYING(128) NOT NULL UNIQUE,
  password   CHARACTER VARYING(128) NOT NULL,
  birth_date DATE                   NOT NULL,
  id_role    INTEGER REFERENCES role (id),
  id_country BIGINT REFERENCES country (id),
  id_city    BIGINT REFERENCES city (id),
  id_sex     INTEGER REFERENCES gender (id)
);

ALTER TABLE subscriber
  RENAME COLUMN id_sex TO gender_id;

ALTER TABLE subscriber
  ALTER COLUMN gender_id SET NOT NULL;

ALTER TABLE subscriber
  DROP email;
ALTER TABLE subscriber
  DROP password;

ALTER TABLE subscriber
  RENAME COLUMN id_country TO country_id;
ALTER TABLE subscriber
  RENAME COLUMN id_city TO city_id;

ALTER TABLE subscriber
  ALTER COLUMN country_id SET NOT NULL;

ALTER TABLE subscriber
  ALTER COLUMN city_id SET NOT NULL;

ALTER TABLE subscriber
  ADD COLUMN user_id BIGINT NOT NULL REFERENCES max_schema."user" (id);

ALTER TABLE "user"
  RENAME TO subscriber;

CREATE TABLE "user" (
  id       BIGSERIAL PRIMARY KEY,
  email    CHARACTER VARYING(128) NOT NULL UNIQUE,
  password CHARACTER VARYING(128) NOT NULL,
  role_id  INTEGER                NOT NULL REFERENCES role (id)
);

CREATE TABLE params (
  id_user BIGINT REFERENCES "user" (id),
  date    DATE    NOT NULL,
  height  INTEGER NOT NULL,
  weight  NUMERIC NOT NULL
);

ALTER TABLE params
  RENAME COLUMN id_user TO subscriber_id;

ALTER TABLE params
  ALTER COLUMN subscriber_id SET NOT NULL;

ALTER TABLE params
  ADD COLUMN id BIGSERIAL PRIMARY KEY;

CREATE TABLE program (
  id    SERIAL PRIMARY KEY,
  name  CHARACTER VARYING NOT NULL UNIQUE,
  price NUMERIC           NOT NULL
);

CREATE TABLE purchase (
  id_user    BIGINT REFERENCES "user" (id),
  id_program INTEGER REFERENCES program (id)
);

ALTER TABLE purchase
  ADD COLUMN id BIGSERIAL PRIMARY KEY;

CREATE TABLE theme (
  id   SERIAL PRIMARY KEY,
  name CHARACTER VARYING NOT NULL UNIQUE
);

CREATE TABLE sub_theme (
  id       BIGSERIAL PRIMARY KEY,
  name     CHARACTER VARYING NOT NULL UNIQUE,
  text     TEXT              NOT NULL UNIQUE,
  id_theme INTEGER REFERENCES theme (id)
);

ALTER TABLE theme
  RENAME COLUMN theme_name TO name;

ALTER TABLE subtheme
  RENAME COLUMN id_theme TO theme_id;

ALTER TABLE subtheme
  RENAME COLUMN subtheme_name TO name;

ALTER TABLE program
  RENAME COLUMN program_name TO name;

ALTER TABLE purchase
  DROP COLUMN id;

ALTER TABLE purchase
  RENAME COLUMN id_user TO user_id;

ALTER TABLE purchase
  RENAME COLUMN id_program TO program_id;

ALTER TABLE purchase
  ADD COLUMN id BIGSERIAL PRIMARY KEY;

ALTER TABLE purchase
  ADD COLUMN date DATE NOT NULL;