-- What is a student's classes & the events(location)
SELECT chs.className, e.locationRoom, e.type as eventType, to_char(e.time, 'HH24:MI:SS') as eventTime
    FROM student s 
    JOIN class_has_student chs ON s.id = chs.studentID
    JOIN class c ON c.name = chs.className
    JOIN event e ON c.name = e.className
    WHERE s.name LIKE '%John%';

SELECT e.locationRoom, e.type as eventType, to_char(e.time, 'HH24:MI:SS') as eventTime, e.className
    FROM event e 
    WHERE EXISTS (
        SELECT * FROM class c 
        WHERE c.name = e.className 
        AND EXISTS (
            SELECT * FROM class_has_student chs 
            JOIN student s ON s.id = chs.studentID 
            WHERE s.name LIKE '%John%' 
            AND c.name = chs.className
        )
    );