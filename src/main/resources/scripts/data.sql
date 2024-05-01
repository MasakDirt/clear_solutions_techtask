INSERT INTO roles(name)
VALUES ('ADMIN'),
       ('USER');

INSERT INTO users (email, firstname, lastname, password, birth_date, city, street, number, apartment_number,
                   phone_number, role_id)      -- encoded password = '1234'
VALUES ('johndoe@example.com', 'John', 'Doe', '$2a$10$B84l9CIFwWV5mR4BayHvkeFz5A2yTl2ujBjq48ykhw.aqMCOqR2w2',
        '1990-05-15', 'New York', 'Broadway', '123', '101', '+123456789', 1),
       ('janesmith@example.com', 'Jane', 'Smith', '$2a$10$B84l9CIFwWV5mR4BayHvkeFz5A2yTl2ujBjq48ykhw.aqMCOqR2w2',
        '1985-10-20', 'Los Angeles', 'Main St', '456', NULL, '+987654321', 2),
       ('makskorniev@example.com', 'Maks', 'Korniev', '$2a$10$B84l9CIFwWV5mR4BayHvkeFz5A2yTl2ujBjq48ykhw.aqMCOqR2w2',
        '1982-03-25', 'Kyiv', 'Frankivska St', '12', NULL, NULL, 2),
       ('bobjohnson@example.com', 'Bob', 'Johnson', '$2a$10$B84l9CIFwWV5mR4BayHvkeFz5A2yTl2ujBjq48ykhw.aqMCOqR2w2',
        '1982-03-25', NULL, NULL, NULL, NULL, NULL, 2);
