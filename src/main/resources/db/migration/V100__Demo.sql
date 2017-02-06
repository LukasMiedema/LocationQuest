INSERT INTO LOCATION_GAME.PLAYER (player_id, name) VALUES
(0, 'Demo'),
(1, 'AdminAdmin'),
(2, 'Andy Handy'),
(3, 'Peter Bheter'),
(4, 'Definitely a real person');

INSERT INTO LOCATION_GAME.PLAYER_TOKEN (player_id, token) VALUES
(0, '00000000000000000000000000000000');

INSERT INTO LOCATION_GAME.GAME (id, name, admin_id, timestamp, active, allow_new_members) VALUES
(0, 'Test Game 1', 1, now(), true, true),
(1, 'Test Game 2', 1, now(), false, true),
(2, 'Test Game 3', 1, now(), true, true),
(3, 'Test Game 4', 1, now(), true, true);

INSERT INTO LOCATION_GAME.TEAM (id, game_id, name, color) VALUES
(0, 0, 'TestTeam', 0xff00ff),
(1, 0, 'TeamTwo', 0x00ffff),
(2, 0, 'TeamThree', 0x00ff00),
(3, 1, 'OtherGameTeam', 0xffbbff),
(4, 2, 'Best Team EVER', 0xbbbbff);

INSERT INTO LOCATION_GAME.TEAM_PLAYER (player_id, team_id) VALUES
(0, 0),
(1, 0),
(2, 0),
(3, 0);


INSERT INTO LOCATION_GAME.QUESTION (question_id, game_id, title, text, type) VALUES
(0, 0, 'How many bananas?', 'Most people say 4 but will you????', 'MULTIPLE_CHOICE'),
(1, 0, 'Go to the jumbo', 'Im not even trying', 'QR_TEXT_FETCH');

INSERT INTO LOCATION_GAME.MULTIPLE_CHOICE_ANSWER (answer_id, question_id, answer_label, answer_text, correct, qr_code) VALUES
(0, 0, 'Yes', 'It is most definitely', true, '00000000000000000000000000000001'),
(1, 0, 'No', '1', false, '00000000000000000000000000000002'),
(2, 0, 'Both', '2', false, '00000000000000000000000000000003'),
(3, 0, 'Always', '3', false, '00000000000000000000000000000004'),
(4, 0, 'Never', 'I forfeit my right to say 4', true, '00000000000000000000000000000005');