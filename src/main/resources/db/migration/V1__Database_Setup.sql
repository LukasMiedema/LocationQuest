CREATE SCHEMA LOCATION_GAME;

-- Users
CREATE TABLE LOCATION_GAME.PLAYER (
  session_id UUID PRIMARY KEY,
  name TEXT NOT NULL
);

-- Games
CREATE TABLE LOCATION_GAME.GAME (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  admin_id UUID REFERENCES LOCATION_GAME.PLAYER(session_id),
  timestamp TIMESTAMP NOT NULL DEFAULT now(),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  allow_new_members BOOLEAN NOT NULL DEFAULT TRUE
);

-- Teams
CREATE TABLE LOCATION_GAME.TEAM (
  id SERIAL PRIMARY KEY,
  game_id INTEGER NOT NULL REFERENCES LOCATION_GAME.GAME(id),
  name VARCHAR NOT NULL,
  color INTEGER NOT NULL,
  UNIQUE KEY (game_id, name)
);

-- Team principal association table
CREATE TABLE LOCATION_GAME.TEAM_PLAYER (
  player_session_id UUID NOT NULL REFERENCES LOCATION_GAME.PLAYER(session_id),
  team_id INTEGER NOT NULL REFERENCES LOCATION_GAME.TEAM(id),
  PRIMARY KEY (player_session_id, team_id)
);


-- Question
-- A question has a track (main track and error track). Your first question is always a main track one. In case of a
-- multiple choice question, if you answer it wrong you have to do a question from the error track. After that, you
-- get back to the main track if you answer it correctly. If a question is not multiple choice, you always return
-- or stay on the main track. If all error track questions are answered, you also stay on the main track. When all
-- questions on the main track are completed, you are done.
-- Multiple choice question expand on question
-- First you read the text of the question, scan the code and if it's a multiple choice question are faced with
-- the puzzle text and the answers (see MULTIPLE_CHOICE_ANSWER) and you have to select one. If it is not a multiple
-- choice question you advance immediately as if you answered correctly.
CREATE TABLE LOCATION_GAME.QUESTION (
  question_id SERIAL PRIMARY KEY,
  game_id INTEGER NOT NULL REFERENCES LOCATION_GAME.GAME(id),
  title VARCHAR NOT NULL,
  text TEXT NOT NULL,
  qr_code UUID DEFAULT random_uuid(),
  puzzle_title TEXT, -- null = not multiple choice
  puzzle_text TEXT -- null = not multiple choice. Advance as if answered correctly
);

-- The answers to one multiple choice question
CREATE TABLE LOCATION_GAME.MULTIPLE_CHOICE_ANSWER (
  answer_id SERIAL PRIMARY KEY,
  question_id INTEGER NOT NULL REFERENCES LOCATION_GAME.QUESTION(question_id),
  answer_label TEXT NOT NULL,
  correct BOOLEAN NOT NULL
);

-- Answered question - created when a team answers a question
CREATE TABLE LOCATION_GAME.ANSWERED_QUESTION (
  question_id INTEGER NOT NULL REFERENCES LOCATION_GAME.QUESTION(question_id),
  team_id INTEGER NOT NULL REFERENCES LOCATION_GAME.TEAM(id),
  complete BOOLEAN NOT NULL DEFAULT FALSE,
  answer_id INTEGER REFERENCES LOCATION_GAME.MULTIPLE_CHOICE_ANSWER(answer_id), -- nullable
  timestamp TIMESTAMP NOT NULL DEFAULT now(),
  PRIMARY KEY (question_id, team_id)
);

-- Images hosted by the application
CREATE TABLE LOCATION_GAME.IMAGES (
  image_id UUID NOT NULL DEFAULT random_uuid(),
  image BYTEA NOT NULL
);