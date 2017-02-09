CREATE SCHEMA LOCATION_GAME;

-- Users
CREATE TABLE LOCATION_GAME.PLAYER (
  player_id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  system_admin BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE LOCATION_GAME.PLAYER_TOKEN (
  player_id INT NOT NULL REFERENCES LOCATION_GAME.PLAYER(player_id),
  token UUID PRIMARY KEY,
  last_used TIMESTAMP DEFAULT NOW()
);

CREATE TABLE LOCATION_GAME.PLAYER_CREDENTIALS (
  player_id INT PRIMARY KEY REFERENCES LOCATION_GAME.PLAYER(player_id),
  email_address VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(60) NOT NULL
);

-- Games
CREATE TABLE LOCATION_GAME.GAME (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  admin_id INT REFERENCES LOCATION_GAME.PLAYER(player_id),
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
  player_id INT NOT NULL REFERENCES LOCATION_GAME.PLAYER(player_id),
  team_id INTEGER NOT NULL REFERENCES LOCATION_GAME.TEAM(id),
  PRIMARY KEY (player_id, team_id)
);


-- Question
-- A question has a type and there are multiple types
-- However all types share one table, so lots of the fields are nullable
CREATE TABLE LOCATION_GAME.QUESTION (

  -- General fields
  question_id SERIAL PRIMARY KEY,
  game_id INTEGER NOT NULL REFERENCES LOCATION_GAME.GAME(id),
  title VARCHAR NOT NULL,
  text TEXT NOT NULL,
  type VARCHAR(32) NOT NULL, CHECK (type IN ('MULTIPLE_CHOICE', 'QR_TEXT_FETCH')),

  -- Fetch
  qr_code UUID DEFAULT NULL

  -- Multiple choice
);

-- The answers to one multiple choice question
CREATE TABLE LOCATION_GAME.MULTIPLE_CHOICE_ANSWER (
  answer_id SERIAL PRIMARY KEY,
  question_id INTEGER NOT NULL REFERENCES LOCATION_GAME.QUESTION(question_id),
  answer_label TEXT NOT NULL,
  answer_text TEXT NOT NULL,
  qr_code UUID NOT NULL,
  correct BOOLEAN NOT NULL
);

-- Answered question - created when a team answers a question
CREATE TABLE LOCATION_GAME.ANSWERED_QUESTION (
  question_id INTEGER NOT NULL REFERENCES LOCATION_GAME.QUESTION(question_id),
  team_id INTEGER NOT NULL REFERENCES LOCATION_GAME.TEAM(id),
  timestamp TIMESTAMP NOT NULL DEFAULT now(),

  -- Multiple choice
  answer_id INTEGER REFERENCES LOCATION_GAME.MULTIPLE_CHOICE_ANSWER(answer_id),
  PRIMARY KEY (question_id, team_id)
);

-- Images hosted by the application
CREATE TABLE LOCATION_GAME.IMAGES (
  image_id UUID NOT NULL DEFAULT random_uuid(),
  image BYTEA NOT NULL
);