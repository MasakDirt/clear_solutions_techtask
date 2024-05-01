ALTER TABLE IF EXISTS users
    DROP CONSTRAINT IF EXISTS FKp56c1712k691lhsyewcssf40f;
ALTER TABLE IF EXISTS users
    DROP CONSTRAINT IF EXISTS FKpegaor84w5wbefsf40f;
ALTER TABLE IF EXISTS refresh_token
    DROP CONSTRAINT IF EXISTS POdpsfjfdjsfjas2334f;

DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS token_blacklist CASCADE;
DROP TABLE IF EXISTS refresh_token CASCADE;

CREATE TABLE roles
(
    id   BIGSERIAL    NOT NULL,
    name varchar(255) not null unique,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS users
(
    id               SERIAL PRIMARY KEY,
    email            VARCHAR(255) NOT NULL,
    firstname        VARCHAR(255) NOT NULL,
    lastname         VARCHAR(255) NOT NULL,
    birth_date       DATE         NOT NULL,
    password             VARCHAR(255),
    city             VARCHAR(255),
    street           VARCHAR(255),
    number           VARCHAR(255),
    apartment_number VARCHAR(255),
    phone_number     VARCHAR(13),
    role_id          BIGINT,
    refresh_token_id BIGINT
);

CREATE TABLE token_blacklist
(
    id    BIGSERIAL    NOT NULL,
    token VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE refresh_token
(
    id          BIGSERIAL    NOT NULL,
    token       VARCHAR(255) NOT NULL,
    expiry_date DATE         NOT NULL,
    user_id     BIGINT,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS users
    ADD CONSTRAINT FKp56c1712k691lhsyewcssf40f FOREIGN KEY (role_id) REFERENCES roles;
ALTER TABLE IF EXISTS users
    ADD CONSTRAINT FKpegaor84w5wbefsf40f FOREIGN KEY (refresh_token_id) REFERENCES refresh_token;
ALTER TABLE IF EXISTS refresh_token
    ADD CONSTRAINT POdpsfjfdjsfjas2334f FOREIGN KEY (user_id) REFERENCES users;
