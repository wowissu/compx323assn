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
  className VARCHAR(30) NOT NULL,
  locationRoom VARCHAR(10) NOT NULL,
  type VARCHAR(30),
  time TIMESTAMP,
  CONSTRAINT fk_event_class FOREIGN KEY (className) REFERENCES class (name),
  CONSTRAINT fk_event_location FOREIGN KEY (locationRoom) REFERENCES location (room),
  CONSTRAINT pk_event PRIMARY KEY (className, locationRoom, type, time)
);

-- Student
CREATE TABLE student (
  id VARCHAR(15) PRIMARY KEY,
  phone VARCHAR(20),
  name VARCHAR(50)
);

-- Meeting
CREATE TABLE meeting (
  id NUMBER DEFAULT meeting_seq.NEXTVAL PRIMARY KEY,
  "date" DATE NOT NULL,
  startTime TIMESTAMP NOT NULL,
  endTime TIMESTAMP NOT NULL,
  minutes CLOB,
  locationRoom VARCHAR(10),
  CONSTRAINT fk_meeting_location FOREIGN KEY (locationRoom) REFERENCES location (room)
);

-- Assignment
CREATE TABLE assignment (
  assignmentName VARCHAR(50) NOT NULL,
  className VARCHAR(30) NOT NULL,
  type VARCHAR(30) NOT NULL,
  time TIMESTAMP,
  CONSTRAINT fk_assignment_class FOREIGN KEY (className) REFERENCES class (name),
  CONSTRAINT pk_assignment PRIMARY KEY (assignmentName, className)
);

-- Curricular
CREATE TABLE curricular (
  meetingID NUMBER NOT NULL,
  assignmentName VARCHAR(50) NOT NULL,
  className VARCHAR(30) NOT NULL,
  CONSTRAINT fk_curricular_meeting FOREIGN KEY (meetingID) REFERENCES meeting (id),
  CONSTRAINT fk_curricular_assignment FOREIGN KEY (assignmentName, className) REFERENCES assignment (assignmentName, className),
  CONSTRAINT pk_curricular PRIMARY KEY (meetingID, assignmentName, className)
);

-- Co-curricular
CREATE TABLE co_curricular (
  meetingID NUMBER NOT NULL PRIMARY KEY,
  cost NUMBER DEFAULT 0,
  CONSTRAINT fk_co_curricular_meeting FOREIGN KEY (meetingID) REFERENCES meeting (id)
);

--
-- Registered
CREATE TABLE registered (
  studentID VARCHAR(15),
  meetingID NUMBER NOT NULL,
  attendance CHAR(1) DEFAULT 'N' CHECK (attendance IN ('Y', 'N')),
  CONSTRAINT fk_registered_student FOREIGN KEY (studentID) REFERENCES student (id),
  CONSTRAINT fk_registered_meeting FOREIGN KEY (meetingID) REFERENCES meeting (id)
);

-- Co-curricularHasClub
CREATE TABLE co_curricular_has_club (
  meetingID NUMBER,
  clubName VARCHAR(30),
  CONSTRAINT fk_co_has_club_meeting FOREIGN KEY (meetingID) REFERENCES co_curricular (meetingID),
  CONSTRAINT fk_co_has_club_club FOREIGN KEY (clubName) REFERENCES club (name),
  CONSTRAINT pk_co_has_club PRIMARY KEY (meetingID, clubName)
);

-- StudentHasClass
CREATE TABLE class_has_student (
  studentID VARCHAR(15),
  className VARCHAR(30),
  CONSTRAINT fk_class_has_student_student FOREIGN KEY (studentID) REFERENCES student (id),
  CONSTRAINT fk_class_has_student_class FOREIGN KEY (className) REFERENCES class (name),
  CONSTRAINT pk_class_has_student_club PRIMARY KEY (studentID, className)
);