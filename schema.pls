DROP TABLE co_curricular_has_club CASCADE CONSTRAINTS;

DROP TABLE registered CASCADE CONSTRAINTS;

DROP TABLE co_curricular CASCADE CONSTRAINTS;

DROP TABLE class_has_student CASCADE CONSTRAINTS;

DROP TABLE curricular CASCADE CONSTRAINTS;

DROP TABLE assignment CASCADE CONSTRAINTS;

DROP TABLE meeting CASCADE CONSTRAINTS;

DROP TABLE student CASCADE CONSTRAINTS;

DROP TABLE event CASCADE CONSTRAINTS;

DROP TABLE location CASCADE CONSTRAINTS;

DROP TABLE class CASCADE CONSTRAINTS;

DROP TABLE club CASCADE CONSTRAINTS;

DROP SEQUENCE meeting_seq;

CREATE SEQUENCE meeting_seq
START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;

-- Club
CREATE TABLE club (name VARCHAR(30) PRIMARY KEY, description CLOB);

-- Class
CREATE TABLE class (name VARCHAR(30) PRIMARY KEY);

-- Location
CREATE TABLE location (room VARCHAR(10) PRIMARY KEY);

-- Event
CREATE TABLE event (
  class_name VARCHAR(30) NOT NULL,
  location_room VARCHAR(10) NOT NULL,
  type VARCHAR(30),
  time TIMESTAMP NOT NULL,
  CONSTRAINT fk_event_class FOREIGN KEY (class_name) REFERENCES class (name),
  CONSTRAINT fk_event_location FOREIGN KEY (location_room) REFERENCES location (room),
  CONSTRAINT pk_event PRIMARY KEY (class_name, location_room, type)
);

-- Student
CREATE TABLE student (
  id VARCHAR(15) PRIMARY KEY,
  phone VARCHAR(15),
  name VARCHAR(50)
);

-- Meeting
CREATE TABLE meeting (
  id NUMBER DEFAULT meeting_seq.NEXTVAL PRIMARY KEY,
  "date" TIMESTAMP NOT NULL,
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NOT NULL,
  minutes CLOB
);

-- Assignment
CREATE TABLE assignment (
  assignment_name VARCHAR(50) NOT NULL,
  class_name VARCHAR(30) NOT NULL,
  type VARCHAR(30) NOT NULL,
  time TIMESTAMP,
  CONSTRAINT fk_assignment_class FOREIGN KEY (class_name) REFERENCES class (name),
  CONSTRAINT pk_assignment PRIMARY KEY (assignment_name, class_name)
);

-- Curricular
CREATE TABLE curricular (
  meeting_id NUMBER NOT NULL,
  assignment_name VARCHAR(50) NOT NULL,
  class_name VARCHAR(30) NOT NULL,
  CONSTRAINT fk_curricular_meeting FOREIGN KEY (meeting_id) REFERENCES meeting (id),
  CONSTRAINT fk_curricular_assignment FOREIGN KEY (assignment_name, class_name) REFERENCES assignment (assignment_name, class_name),
  CONSTRAINT pk_curricular PRIMARY KEY (meeting_id, assignment_name, class_name)
);

-- Co-curricular
CREATE TABLE co_curricular (
  meeting_id NUMBER NOT NULL PRIMARY KEY,
  cost NUMBER DEFAULT 0,
  CONSTRAINT fk_co_curricular_meeting FOREIGN KEY (meeting_id) REFERENCES meeting (id)
);

-- Registered
CREATE TABLE registered (
  student_id VARCHAR(15),
  meeting_id NUMBER NOT NULL,
  attendance CHAR(1) DEFAULT 'N' CHECK (attendance IN ('Y', 'N')),
  CONSTRAINT fk_registered_student FOREIGN KEY (student_id) REFERENCES student (id),
  CONSTRAINT fk_registered_meeting FOREIGN KEY (meeting_id) REFERENCES meeting (id)
);

-- Co-curricularHasClub
CREATE TABLE co_curricular_has_club (
  meeting_id NUMBER,
  club_name VARCHAR(30),
  CONSTRAINT fk_co_has_club_meeting FOREIGN KEY (meeting_id) REFERENCES co_curricular (meeting_id),
  CONSTRAINT fk_co_has_club_club FOREIGN KEY (club_name) REFERENCES club (name),
  CONSTRAINT pk_co_has_club PRIMARY KEY (meeting_id, club_name)
);

-- StudentHasClass
CREATE TABLE class_has_student (
  student_id VARCHAR(15),
  class_name VARCHAR(30),
  CONSTRAINT fk_class_has_student_student FOREIGN KEY (student_id) REFERENCES student (id),
  CONSTRAINT fk_class_has_student_class FOREIGN KEY (class_name) REFERENCES class (name),
  CONSTRAINT pk_class_has_student_club PRIMARY KEY (student_id, class_name)
);