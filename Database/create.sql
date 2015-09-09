# Drop existing tables
DROP TABLE IF EXISTS PoIVisits;
DROP TABLE IF EXISTS Reviews;
DROP TABLE IF EXISTS UserAchievements;
DROP TABLE IF EXISTS Achievement;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS PointsOfInterest;
DROP TABLE IF EXISTS Region;
DROP TABLE IF EXISTS TypeOfPoI;

# Type of Points of Interest
CREATE TABLE TypeOfPoI
(
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  type VARCHAR(100) NOT NULL UNIQUE
);

# Region of the Points of Interest
CREATE TABLE Region
(
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name TEXT NOT NULL
);

# Users
CREATE TABLE User
(
  idFacebook VARCHAR(64) PRIMARY KEY NOT NULL,
  points INT NOT NULL
);

# Points of Interest
CREATE TABLE PointsOfInterest
(
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  userId VARCHAR(64) NOT NULL,
  typeId INT NOT NULL,
  regionId INT NOT NULL,
  name VARCHAR(256) NOT NULL,
  description TEXT NOT NULL,
  address TEXT NOT NULL,
  latitude DOUBLE NOT NULL,
  longitude DOUBLE NOT NULL,
  creationDate DATE NOT NULL,
  numLikes INT NOT NULL,
  numDislikes INT NOT NULL,
  rating DOUBLE NOT NULL,
  CONSTRAINT FOREIGN KEY (userId) REFERENCES User(idFacebook),
  CONSTRAINT FOREIGN KEY (typeId) REFERENCES TypeOfPoI(id),
  CONSTRAINT FOREIGN KEY (regionId) REFERENCES Region(id)
);

# Achievements
CREATE TABLE Achievement
(
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(256) NOT NULL,
  description TEXT NOT NULL
);

# User achievements
CREATE TABLE UserAchievements
(
  userId VARCHAR(64) NOT NULL,
  achievementId INT NOT NULL,
  unlockedDate DATE NOT NULL,
  CONSTRAINT FOREIGN KEY (userId) REFERENCES User(idFacebook),
  CONSTRAINT FOREIGN KEY (achievementId) REFERENCES Achievement(id)
);

# Reviews
CREATE TABLE Reviews
(
  userId VARCHAR(64) NOT NULL,
  poiId INT NOT NULL,
  comment TEXT NOT NULL,
  `like` TINYINT NOT NULL,
  CONSTRAINT FOREIGN KEY (userId) REFERENCES User(idFacebook),
  CONSTRAINT FOREIGN KEY (poiId) REFERENCES PointsOfInterest(id)
);

# Visits to Points of Interest
CREATE TABLE PoIVisits
(
  userId VARCHAR(64) NOT NULL,
  poiId INT NOT NULL,
  visitDate DATE NOT NULL,
  CONSTRAINT FOREIGN KEY (userId) REFERENCES User(idFacebook),
  CONSTRAINT FOREIGN KEY (poiId) REFERENCES PointsOfInterest(id)
);