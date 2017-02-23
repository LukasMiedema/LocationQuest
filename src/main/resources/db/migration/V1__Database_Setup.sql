CREATE SCHEMA LOCATION_GAME;

-- Images and other files hosted by the application
CREATE TABLE LOCATION_GAME.FILE (
  file_id UUID PRIMARY KEY,
  mime_type VARCHAR(50),
  file BYTEA NOT NULL
);

-- Users
CREATE TABLE LOCATION_GAME.PLAYER (
  player_id SERIAL PRIMARY KEY,
  name VARCHAR(10) NOT NULL,
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
  game_id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  timestamp TIMESTAMP NOT NULL DEFAULT now(),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  allow_new_members BOOLEAN NOT NULL DEFAULT TRUE
);

-- Teams
CREATE TABLE LOCATION_GAME.TEAM (
  team_id SERIAL PRIMARY KEY,
  game_id INTEGER NOT NULL REFERENCES LOCATION_GAME.GAME(game_id),
  name VARCHAR NOT NULL,
  color INTEGER NOT NULL,
  UNIQUE KEY (game_id, name)
);

-- Team principal association table
CREATE TABLE LOCATION_GAME.TEAM_PLAYER (
  player_id INT NOT NULL REFERENCES LOCATION_GAME.PLAYER(player_id),
  team_id INTEGER NOT NULL REFERENCES LOCATION_GAME.TEAM(team_id),
  PRIMARY KEY (player_id, team_id)
);


-- Chapters
CREATE TABLE LOCATION_GAME.CHAPTER (
  chapter_id SERIAL PRIMARY KEY,
  game_id INTEGER NOT NULL REFERENCES LOCATION_GAME.GAME(game_id),
  color INT NOT NULL,
  name VARCHAR(50) NOT NULL
);

-- Locations, dependencies and claimed locations
CREATE TABLE LOCATION_GAME.QUEST (
  quest_id SERIAL PRIMARY KEY,
  chapter_id INTEGER NOT NULL REFERENCES LOCATION_GAME.CHAPTER(chapter_id),
  name VARCHAR(255) NOT NULL,
  fetch_text TEXT,
  scan_text TEXT NOT NULL,
  required BOOLEAN NOT NULL DEFAULT TRUE, -- required for the chapter to complete. When false, order is not checked
  CHECK (NOT required OR fetch_text IS NOT NULL), -- when required, there has to be a fetch text
  qr_code UUID NOT NULL DEFAULT random_uuid(),
  passcode_text TEXT -- NULL means no passcode
);

-- Collectibles
CREATE TABLE LOCATION_GAME.COLLECTIBLE (
  collectible_id SERIAL PRIMARY KEY,
  name VARCHAR(32) NOT NULL,
  file_id UUID REFERENCES LOCATION_GAME.FILE(file_id)
);

-- Links a location to what claiming the location requires and yields
CREATE TABLE LOCATION_GAME.QUEST_COLLECTIBLE (
  collectible_id INT NOT NULL REFERENCES LOCATION_GAME.COLLECTIBLE (collectible_id),
  quest_id INT NOT NULL REFERENCES LOCATION_GAME.QUEST(quest_id),
  requires INT NOT NULL,
  yields INT NOT NULL,

  PRIMARY KEY (collectible_id, quest_id)
);

-- For every team that claims a location
CREATE TABLE LOCATION_GAME.CLAIMED_QUEST (
  quest_id INT NOT NULL REFERENCES LOCATION_GAME.QUEST(quest_id),
  team_id INT NOT NULL REFERENCES LOCATION_GAME.TEAM(team_id),
  claimed_at TIMESTAMP NOT NULL DEFAULT now(),

  PRIMARY KEY (quest_id, team_id)
);