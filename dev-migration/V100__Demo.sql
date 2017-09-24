-- Images
INSERT INTO "LOCATION_QUEST"."FILE" (file_id, mime_type, file) VALUES
(0, 'image/png', FILE_READ('dev-migration/cookie.png'));

-- Players
INSERT INTO "LOCATION_QUEST"."PLAYER" (player_id, name, system_admin) VALUES
(0, 'TestingWWW', false),
(1, 'AdminAdmin', true),
(2, 'AndyHandy', false),
(3, 'Petertje', false),
(4, 'RealDeal', false);

INSERT INTO "LOCATION_QUEST"."PLAYER_TOKEN" (player_id, token) VALUES
(1, '9317bae6-7706-45a5-88e8-ab3aa182c23e');

INSERT INTO "LOCATION_QUEST"."PLAYER_CREDENTIALS" (player_id, email_address, password_hash) VALUES
(1, 'test@test.net', '$2a$06$Ns5bMWsKxfEiAIpPFQlFuO6BaUZC8ExhWrT1ucZgRj0.yWIuB0fNm'); -- hashes "testing"

-- Games
INSERT INTO "LOCATION_QUEST"."GAME" (game_id, name, timestamp, active, allow_new_members) VALUES
(0, 'Test Game 1', now(), true, true),
(1, 'Test Game 2', now(), false, true),
(2, 'Test Game 3', now(), true, false),
(3, 'Test Game 4', now(), false, false);

INSERT INTO "LOCATION_QUEST"."CHAPTER" (chapter_id, game_id, color, name) VALUES
(0, 0, CAST(X'ff00ff' as INT), 'Hoofdstuk 1'),
(1, 0, CAST(X'00ffff' as INT), 'Hoofdstuk 2'),
(2, 0, CAST(X'ffccff' as INT), 'Hoofdstuk 3'),
(3, 0, CAST(X'cc0000' as INT), 'Hoofdstuk 4');

-- Collectibles for the game
INSERT INTO "LOCATION_QUEST"."COLLECTIBLE" (collectible_id, name, file_id) VALUES
(0, 'Cookie', 0);

-- Locations for the game
INSERT INTO "LOCATION_QUEST"."QUEST" (quest_id, chapter_id, qr_code, required, name, fetch_text, scan_text, passcode_text) VALUES
(0, 0, '050d10ef-9940-48f2-9abc-bd0aff022a00', false, 'Top Secret Cookie Chest', null, 'Looks like its not so secret anymore', null),
(1, 0, '050d10ef-9940-48f2-9abc-bd0aff022a01', true, 'This requires lots of cookies', '<script>alert("so yes");</script>', '#There you are.# About time', null),
(2, 0, '050d10ef-9940-48f2-9abc-bd0aff022a02', false,	'Got some delicious cookies for ya', null, 'Fresh from grandmas oven', 'abcd'),
(3, 0, '050d10ef-9940-48f2-9abc-bd0aff022a03', true,	'Q3', 'FETCH ME IF YOU CAN - TESTING', 'TESTING TESTING', null),
(4, 0, '050d10ef-9940-48f2-9abc-bd0aff022a04', true,	'Q4', 'FETCH ME IF YOU CAN - TESTING', 'TESTING TESTING', null),
(5, 1, '050d10ef-9940-48f2-9abc-bd0aff022a05', true,	'Q5', 'FETCH ME IF YOU CAN - TESTING', 'TESTING TESTING', null),
(6, 1, '050d10ef-9940-48f2-9abc-bd0aff022a06', true,	'Q6', 'FETCH ME IF YOU CAN - TESTING', 'TESTING TESTING', null),
(7, 1, '050d10ef-9940-48f2-9abc-bd0aff022a07', true,	'Q7', 'FETCH ME IF YOU CAN - TESTING', 'TESTING TESTING', null),
(8, 1, '050d10ef-9940-48f2-9abc-bd0aff022a08', true,	'Q8', 'FETCH ME IF YOU CAN - TESTING', 'TESTING TESTING', null),
(9, 2, '050d10ef-9940-48f2-9abc-bd0aff022a09', true,	'Q9', 'FETCH ME IF YOU CAN - TESTING', 'TESTING TESTING', null),
(10, 2, '050d10ef-9940-48f2-9abc-bd0aff022a10', true,	'Q10', 'FETCH ME IF YOU CAN - TESTING', 'TESTING TESTING', null),
(11, 2, '050d10ef-9940-48f2-9abc-bd0aff022a11', true,	'Q11', 'FETCH ME IF YOU CAN - TESTING', 'TESTING TESTING', null),
(12, 3, '050d10ef-9940-48f2-9abc-bd0aff022a12', true,	'Q12', 'FETCH ME IF YOU CAN - TESTING', 'TESTING TESTING', null),
(13, 3, '050d10ef-9940-48f2-9abc-bd0aff022a13', true,	'Q13', 'FETCH ME IF YOU CAN - TESTING', 'TESTING TESTING', null),
(14, 3, '050d10ef-9940-48f2-9abc-bd0aff022a14', true,	'Q14', 'FETCH ME IF YOU CAN - TESTING', 'TESTING TESTING', null);

INSERT INTO "LOCATION_QUEST"."QUEST_COLLECTIBLE" (collectible_id, quest_id, requires, yields) VALUES
(0, 0, 0, 5),
(0, 1, 10, 0),
(0, 2, 0, 20);


-- Teams and players
INSERT INTO "LOCATION_QUEST"."TEAM" (team_id, game_id, name, color) VALUES
(0, 0, 'TestTeam', CAST(X'ff00ff' as INT)),
(1, 0, 'TeamTwo', CAST(X'00ffff' as INT)),
(2, 0, 'TeamThree', CAST(X'00ff00' as INT)),
(3, 1, 'OtherGameTeam', CAST(X'ffbbff' as INT)),
(4, 2, 'Best Team EVER', CAST(X'bbbbff' as INT));

INSERT INTO "LOCATION_QUEST"."TEAM_PLAYER" (player_id, team_id) VALUES
(0, 0),
(1, 0),
(2, 0),
(3, 0);