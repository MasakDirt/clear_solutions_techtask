INSERT INTO roles(name)
VALUES ('ADMIN'),
       ('USER');

INSERT INTO users (email, firstname, lastname, password, birth_date, city, street, number, apartment_number,
                   phone_number, role_id) -- encoded password = '1234'
VALUES ('johndoe@example.com', 'John', 'Doe', '$2a$10$B84l9CIFwWV5mR4BayHvkeFz5A2yTl2ujBjq48ykhw.aqMCOqR2w2',
        '1990-05-15', 'New York', 'Broadway', '123', '101', '+123456789', 1),
       ('janesmith@example.com', 'Jane', 'Smith', '$2a$10$B84l9CIFwWV5mR4BayHvkeFz5A2yTl2ujBjq48ykhw.aqMCOqR2w2',
        '1985-10-20', 'Los Angeles', 'Main St', '456', NULL, '+987654321', 2),
       ('makskorniev@example.com', 'Maks', 'Korniev', '$2a$10$B84l9CIFwWV5mR4BayHvkeFz5A2yTl2ujBjq48ykhw.aqMCOqR2w2',
        '2004-11-08', 'Kyiv', 'Frankivska St', '12', NULL, NULL, 2),
       ('bobjohnson@example.com', 'Bob', 'Johnson', '$2a$10$B84l9CIFwWV5mR4BayHvkeFz5A2yTl2ujBjq48ykhw.aqMCOqR2w2',
        '1982-03-25', NULL, NULL, NULL, NULL, NULL, 2);

INSERT INTO refresh_token(token, expiry_date, user_id)
VALUES ('a07ba6e2-a337-4e01-90e5-f971bce280c6', '2030-12-05', 1),
       ('54698cc3-1537-40f5-b301-22da76ddb79b', '2030-11-12', 2),
       ('ba39ebf3-2f60-4ce5-b387-adc9ec9c2d7c', '2030-07-23', 3),
       ('0cabff27-15dc-4126-9f0e-408496155bfc', '2031-01-11', 4);

UPDATE users
SET refresh_token_id =
        CASE
            WHEN id = 1 THEN 1
            WHEN id = 2 THEN 2
            WHEN id = 3 THEN 4
            WHEN id = 4 THEN 3
            ELSE refresh_token_id
            END;
