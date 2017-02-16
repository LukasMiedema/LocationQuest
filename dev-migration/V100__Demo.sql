-- Images
INSERT INTO LOCATION_GAME.FILE (file_id, mime_type, file) VALUES
(0, 'image/png', FILE_READ('dev-migration/cookie.png'));

-- Players
INSERT INTO LOCATION_GAME.PLAYER (player_id, name) VALUES
(0, 'TestingWWW'),
(1, 'DemoUser'),
(2, 'AndyHandy'),
(3, 'Petertje'),
(4, 'RealDeal');

INSERT INTO LOCATION_GAME.PLAYER_TOKEN (player_id, token) VALUES
(0, '9317bae6-7706-45a5-88e8-ab3aa182c23e');


-- Games
INSERT INTO LOCATION_GAME.GAME (game_id, name, timestamp, active, allow_new_members) VALUES
(0, 'Test Game 1', now(), true, true),
(1, 'Test Game 2', now(), false, true),
(2, 'Test Game 3', now(), true, false),
(3, 'Test Game 4', now(), false, false);

INSERT INTO LOCATION_GAME.CHAPTER (chapter_id, game_id, name, color) VALUES
(0, 0, 'Chapter 1', 0xff00ff),
(1, 0, 'Chapter 2', 0x00ffff),
(2, 0, 'Chapter 3', 0xffccff),
(3, 0, 'Chapter 4', 0xcc0000);

-- Collectibles for the game
INSERT INTO LOCATION_GAME.COLLECTIBLE (collectible_id, name, file_id) VALUES
(0, 'Cookie', 0);

-- Locations for the game
INSERT INTO LOCATION_GAME.QUEST (quest_id, chapter_id, qr_code, required, name, fetch_text, scan_text, passcode_text) VALUES
(0, 0, 'e87c8995-cca5-41d5-828f-d4698ced3845', false, 'Top Secret Cookie Chest', null, 'Looks like its not so secret anymore', null),
(1, 0, '2f5ba29d-6ce6-4b63-b060-1f2181d7e6ad', true, 'This requires lots of cookies', 'Go find me 10 cookies please', 'There you are. About time', null),
(2, 0, '050d10ef-9940-48f2-9abc-bd0aff022a08', false,	'Got some delicious cookies for ya', null, 'Fresh from grandmas oven', 'abcd');

INSERT INTO LOCATION_GAME.QUEST_COLLECTIBLE (collectible_id, quest_id, requires, yields) VALUES
(0, 0, 0, 5),
(0, 1, 10, 0),
(0, 2, 0, 20);


-- Teams and players
INSERT INTO LOCATION_GAME.TEAM (team_id, game_id, name, color) VALUES
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