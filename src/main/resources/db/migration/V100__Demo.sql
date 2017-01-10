INSERT INTO LOCATION_GAME.PLAYER (session_id, name) VALUES
('00000000000000000000000000000000', 'Demo'),
('00000000000000000000000000000001', 'AdminAdmin'),;

INSERT INTO LOCATION_GAME.GAME (id, name, admin_id, timestamp, active, allow_new_members) VALUES
(0, 'Test Game 1', '00000000000000000000000000000001', now(), true, true),
(1, 'Test Game 2', '00000000000000000000000000000001', now(), false, true),
(2, 'Test Game 3', '00000000000000000000000000000001', now(), true, true),
(3, 'Test Game 4', '00000000000000000000000000000001', now(), true, true);

INSERT INTO LOCATION_GAME.TEAM (id, game_id, name, color) VALUES
(0, 0, 'TestTeam', 0xff00ff),
(1, 0, 'TeamTwo', 0x00ffff),
(2, 0, 'TeamThree', 0x00ff00),
(3, 1, 'OtherGameTeam', 0xffbbff),
(4, 2, 'Best Team EVER', 0xbbbbff);

INSERT INTO LOCATION_GAME.TEAM_PLAYER (player_session_id, team_id) VALUES
('00000000000000000000000000000000', 0);


INSERT INTO LOCATION_GAME.QUESTION (question_id, game_id, title, text, puzzle_title, puzzle_text) VALUES
(0, 0, 'Go to the Jumbo', 'Yeah thats about it', 'How many bananas?', 'Most people say 4 but will you????');

INSERT INTO LOCATION_GAME.MULTIPLE_CHOICE_ANSWER (answer_id, question_id, answer_label, correct) VALUES
(0, 0, 'It is most definitely', true),
(1, 0, '1', false),
(2, 0, '2', false),
(3, 0, '3', false),
(4, 0, 'I forfeit my right to say 4', true);