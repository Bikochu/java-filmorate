CREATE TABLE [film] (
  [film_id] INT PRIMARY KEY,
  [name] VARCHAR(255) NOT NULL,
  [description] VARCHAR(200),
  [release_date] DATE NOT NULL,
  [duration] BIGINT NOT NULL,
  [genre_id] INT,
  [age_id] INT
);

CREATE TABLE [user] (
  [user_id] INT PRIMARY KEY,
  [email] VARCHAR(255) NOT NULL,
  [login] VARCHAR(255) NOT NULL,
  [name] VARCHAR(255),
  [birthday] DATE
);

CREATE TABLE [friendship] (
  [user_id] INT NOT NULL,
  [friend_id] INT NOT NULL,
  [status] VARCHAR(255) NOT NULL,
  PRIMARY KEY ([user_id], [friend_id])
);

CREATE TABLE [film_like] (
  [user_id] INT NOT NULL,
  [film_id] INT NOT NULL,
  PRIMARY KEY ([user_id], [film_id])
);

CREATE TABLE [genre] (
  [genre_id] INT PRIMARY KEY,
  [name] VARCHAR(255) NOT NULL
);

CREATE TABLE [age_rating] (
  [age_id] INT PRIMARY KEY,
  [age_rating] VARCHAR(10) NOT NULL
);

CREATE UNIQUE INDEX [unique_email] ON [user] ("email");

CREATE UNIQUE INDEX [unique_login] ON [user] ("login");

CREATE TABLE [user_friendship] (
  [user_user_id] INT,
  [friendship_user_id] INT,
  PRIMARY KEY ([user_user_id], [friendship_user_id])
);

ALTER TABLE [user_friendship] ADD FOREIGN KEY ([user_user_id]) REFERENCES [user] ([user_id]);

ALTER TABLE [user_friendship] ADD FOREIGN KEY ([friendship_user_id]) REFERENCES [friendship] ([user_id]);


CREATE TABLE [user_friendship(1)] (
  [user_user_id] INT,
  [friendship_friend_id] INT,
  PRIMARY KEY ([user_user_id], [friendship_friend_id])
);

ALTER TABLE [user_friendship(1)] ADD FOREIGN KEY ([user_user_id]) REFERENCES [user] ([user_id]);

ALTER TABLE [user_friendship(1)] ADD FOREIGN KEY ([friendship_friend_id]) REFERENCES [friendship] ([friend_id]);

ALTER TABLE [film_like] ADD FOREIGN KEY ([user_id]) REFERENCES [user] ([user_id])

ALTER TABLE [film_like] ADD FOREIGN KEY ([film_id]) REFERENCES [film] ([film_id])

ALTER TABLE [genre] ADD FOREIGN KEY ([genre_id]) REFERENCES [film] ([genre_id])

ALTER TABLE [age_rating] ADD FOREIGN KEY ([age_id]) REFERENCES [film] ([age_id])
