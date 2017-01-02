INSERT INTO LOCATION_GAME.PLAYER (session_id, name) VALUES
('00000000000000000000000000000000', 'Demo'),
('00000000000000000000000000000001', 'AdminAdmin'),;

INSERT INTO LOCATION_GAME.GAME (id, name, admin_id, timestamp, active, allow_new_members) VALUES
(0, 'Test Game 1', '00000000000000000000000000000001', now(), true, true),
(1, 'Test Game 2', '00000000000000000000000000000001', now(), false, true),
(2, 'Test Game 3', '00000000000000000000000000000001', now(), true, false),
(3, 'Test Game 4', '00000000000000000000000000000001', now(), false, false);

INSERT INTO LOCATION_GAME.TEAM (game_id, name, color) VALUES
(0, 'TestTeam', 0xff00ff),
(0, 'TeamTwo', 0x00ffff),
(0, 'TeamThree', 0x00ff00),
(1, 'OtherGameTeam', 0xffbbff),
(2, 'Best Team EVER', 0xbbbbff);

INSERT INTO LOCATION_GAME.TEAM_PLAYER (player_session_id, game_id, team_name) VALUES
('00000000000000000000000000000000', 0, 'TestTeam');