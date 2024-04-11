CREATE TABLE category (
  cat_id INT NOT NULL,
  cat_name VARCHAR(100),
  PRIMARY KEY (cat_id)
);

CREATE TABLE rating (
  rating_id INT NOT NULL,
  rating_name VARCHAR(50),
  PRIMARY KEY (rating_id)
);

CREATE TABLE movie_role (
  mr_id INT NOT NULL,
  mr_roleName VARCHAR(100),
  PRIMARY KEY (mr_id)
);

CREATE TABLE person (
  p_id INT NOT NULL,
  p_firstName VARCHAR(100),
  p_lastName VARCHAR(100),
  p_gender VARCHAR(100),
  PRIMARY KEY (p_id)
);

CREATE TABLE movie (
  m_id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  m_title VARCHAR(255) NOT NULL,
  m_date INT, -- Adjusted to INT for the year
  m_length INT,
  cat_id INT,
  rating_id INT,
  PRIMARY KEY (m_id),
  FOREIGN KEY (cat_id) REFERENCES category(cat_id),
  FOREIGN KEY (rating_id) REFERENCES rating(rating_id)
);

CREATE TABLE movie_people_role (
  mrp_id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  mr_id INT NOT NULL,
  m_id INT NOT NULL,
  p_id INT NOT NULL,
  compensation INT,
  PRIMARY KEY (mr_id, p_id, m_id),
  FOREIGN KEY (mr_id) REFERENCES movie_role(mr_id),
  FOREIGN KEY (p_id) REFERENCES person(p_id),
  FOREIGN KEY (m_id) REFERENCES movie(m_id)
);

-- Inserting sample data
-- Categories
INSERT INTO category VALUES (1, 'Action');
INSERT INTO category VALUES (2, 'Animation');
INSERT INTO category VALUES (3, 'Comedy');
INSERT INTO category VALUES (4, 'Drama');
INSERT INTO category VALUES (5, 'Family');
INSERT INTO category VALUES (6, 'Horror');
INSERT INTO category VALUES (7, 'Romance');
INSERT INTO category VALUES (8, 'Fantasy');

-- Ratings
INSERT INTO rating VALUES (1, 'G');
INSERT INTO rating VALUES (2, 'R');
INSERT INTO rating VALUES (3, 'PG');
INSERT INTO rating VALUES (4, 'PG-13');

-- Movie Roles
INSERT INTO movie_role VALUES (1, 'Director');
INSERT INTO movie_role VALUES (2, 'Writer');
INSERT INTO movie_role VALUES (3, 'Actor');
INSERT INTO movie_role VALUES (4, 'Producer');

-- Movies with year only
INSERT INTO movie (m_title, m_date, m_length, cat_id, rating_id) VALUES ('The Shawshank Redemption', 1994, 142, 1, 3);
INSERT INTO movie (m_title, m_date, m_length, cat_id, rating_id) VALUES ('The Godfather', 1972, 175, 1, 3);
INSERT INTO movie (m_title, m_date, m_length, cat_id, rating_id) VALUES ('The Dark Knight', 2008, 152, 1, 2);
INSERT INTO movie (m_title, m_date, m_length, cat_id, rating_id) VALUES ('Life is Beautiful', 1997, 116, 2, 1);
INSERT INTO movie (m_title, m_date, m_length, cat_id, rating_id) VALUES ('Psycho', 1960, 109, 3, 3);
INSERT INTO movie (m_title, m_date, m_length, cat_id, rating_id) VALUES ('R-Rated Fantasy', 1994, 88, 8, 2);

-- reset database
delete from movie;
alter table movie modify m_id generated always as identity restart start with 1;

-- recompile java
-- this is just for reference and is also not sql
javac -classpath ".;C:\Users\zacha\Oakland\CSI 3450\apache-tomcat-9.0.87\webapps\Movie\WEB-INF\lib\javax.servlet-api-4.0.1.jar;C:\Users\zacha\Oakland\CSI 3450\apache-tomcat-9.0.87\webapps\Movie\WEB-INF\lib\ojdbc8-21.1.0.0.jar" -d "C:\Users\zacha\Oakland\CSI 3450\apache-tomcat-9.0.87\webapps\Movie\WEB-INF\classes" *.java