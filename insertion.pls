-- Insert data into club table
INSERT INTO club (name, description) VALUES ('Chess Club', 'A club for chess enthusiasts');
INSERT INTO club (name, description) VALUES ('Music Club', 'A club for music lovers');
INSERT INTO club (name, description) VALUES ('Dance Club', 'A club for dance enthusiasts');

-- Insert data into class table
INSERT INTO class (name) VALUES ('Compx323');
INSERT INTO class (name) VALUES ('Compx322');
INSERT INTO class (name) VALUES ('Compx301');

-- Insert data into location table
INSERT INTO location (room) VALUES ('Room A');
INSERT INTO location (room) VALUES ('Room B');
INSERT INTO location (room) VALUES ('Room C');

-- Insert data into event table
-- Assuming the type column is optional
INSERT INTO event (className, locationRoom, type, time) VALUES ('Compx323', 'Room A', 'Workshop', '12:00:00');
INSERT INTO event (className, locationRoom, time) VALUES ('Compx322', 'Room B', '11:00:00');
INSERT INTO event (className, locationRoom, type, time) VALUES ('Compx301', 'Room C', 'Seminar', '14:00:00');

-- Insert data into student table
INSERT INTO student (id, phone, name) VALUES ('1651328', '123456789', 'John Doe');
INSERT INTO student (id, phone, name) VALUES ('1657827', '987654321', 'Jane Smith');
INSERT INTO student (id, phone, name) VALUES ('1658920', '456789123', 'Alice Johnson');

-- Insert data into meeting table
INSERT INTO meeting ("date", startTime, endTime, minutes, locationRoom) VALUES ('2024-04-30', '13:00:00', '14:00:00', 'Meeting minutes for meeting 1', 'Room A');
INSERT INTO meeting ("date", startTime, endTime, minutes, locationRoom) VALUES ('2024-04-03', '14:00:00', '15:00:00', 'Meeting minutes for meeting 2', 'Room B');

-- Insert data into assignment table
-- Assuming the time column is optional
INSERT INTO assignment (assignmentName, className, type, time) VALUES ('Assignment 1', 'Compx323', 'Homework', TO_TIMESTAMP('2024-05-30 00:00:00', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO assignment (assignmentName, className, type) VALUES ('Assignment 2', 'Compx322', 'Project');

-- Insert data into curricular table
INSERT INTO curricular (meetingID, assignmentName, className) VALUES (1000, 'Assignment 1', 'Compx323');
INSERT INTO curricular (meetingID, assignmentName, className) VALUES (1001, 'Assignment 2', 'Compx322');

-- Insert data into co_curricular table
INSERT INTO co_curricular (meetingID, cost) VALUES (1000, 50);
INSERT INTO co_curricular (meetingID, cost) VALUES (1001, 30);

-- Insert data into registered table
INSERT INTO registered (studentID, meetingID, attendance) VALUES ('1651328', 1000, 'Y');
INSERT INTO registered (studentID, meetingID, attendance) VALUES ('1657827', 1000, 'Y');
INSERT INTO registered (studentID, meetingID, attendance) VALUES ('1658920', 1001, 'N');

-- Insert data into co_curricular_has_club table
INSERT INTO co_curricular_has_club (meetingID, clubName) VALUES (1000, 'Chess Club');
INSERT INTO co_curricular_has_club (meetingID, clubName) VALUES (1001, 'Music Club');

-- Insert data into class_has_student table
INSERT INTO class_has_student (studentID, className) VALUES ('1651328', 'Compx323');
INSERT INTO class_has_student (studentID, className) VALUES ('1657827', 'Compx322');
INSERT INTO class_has_student (studentID, className) VALUES ('1658920', 'Compx301');
