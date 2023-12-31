#
# password is password
INSERT INTO player
VALUES (10000, 'Gil', 'Pratte', '5121231235', 'gil@gilpratte.com',
        '$2a$10$RRxUYMgQJu99pCMsny6UP.b8I7pheP5Keq4D1JGlY9tken4LLXKi2'),
       (20000, 'Guest', 'User', '5121231235', 'guest@gilpratte.com',
        '$2a$10$RRxUYMgQJu99pCMsny6UP.b8I7pheP5Keq4D1JGlY9tken4LLXKi2'),
       (30000, 'Guest', 'Admin', '5121231235', 'admin@gilpratte.com',
        '$2a$10$RRxUYMgQJu99pCMsny6UP.b8I7pheP5Keq4D1JGlY9tken4LLXKi2');
INSERT INTO role
VALUES (10000, 'ADMIN', 10000),
       (20000, 'USER', 10000),
       (30000, 'USER', 20000),
       (40000, 'ADMIN', 30000),
       (50000, 'USER', 30000);
INSERT INTO season_payout_settings
VALUES (1, 2022,
        '[{"lowRange" : 5000,"highRange" : 7000,
           "guaranteed": [{"place" : 1,"amount" : 1400,"percent" : 20}],
           "finalTable": [{"place" : 2,"amount" : 1350,"percent" : 20},
                          {"place" : 3,"amount" : 1150,"percent" : 16},
                          {"place" : 4,"amount" : 1100,"percent" : 14},
                          {"place" : 5,"amount" : 0,"percent" : 30}]}]');
INSERT INTO season_payout_settings
VALUES (2, 2023,
        '[{"lowRange" : 5000,"highRange" : 7000,
           "guaranteed": [{"place" : 1,"amount" : 1400,"percent" : 20}],
           "finalTable": [{"place" : 2,"amount" : 1350,"percent" : 20},
                          {"place" : 3,"amount" : 1150,"percent" : 16},
                          {"place" : 4,"amount" : 1100,"percent" : 14},
                          {"place" : 5,"amount" : 0,"percent" : 30}]}]');
INSERT INTO version
VALUES (1, '4.00');
INSERT INTO settings
VALUES (1, 1);
INSERT INTO toc_config
VALUES (1, 10, 20, 20, 3, 40, 40, 20, 2023, 1);

INSERT INTO historical_season VALUES (1, '2020', '2021');
INSERT INTO historical_season_player
VALUES (3, 'Bob Roberts', 12345, 48, '2020', 1, 0);
INSERT INTO historical_season_player
VALUES (4, 'Abe Abeson', 11000, 50, '2020', 1, 1);

INSERT INTO historical_season VALUES (2, '2019', '2020');
INSERT INTO historical_season_player
VALUES (1, 'Abe Abeson', 99500, 51, '2019', 2, 0);
INSERT INTO historical_season_player
VALUES (2, 'Bob Roberts', 88000, 45, '2019', 2, 1);
