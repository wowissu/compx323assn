from faker import Faker
import random
from datetime import datetime, time, timedelta
import csv

fake = Faker()

def main():
    clubs = makeClue()(500)
    students = makeStudent()(1000)
    classes = makeClasses()(100)
    locations = makeLocations()(1000)
    classHasStudent = makeClassHasStudent(students, classes)(min = 3, max = 10)
    events = makeEvents(classes, locations)(min = 3, max = 5)
    assignments = makeAssignments(classes)(0, 3)
    (curriculars, meetings) = makeCurriculars(assignments, locations)(1000)
    (co_curriculars, co_meetings) = makeCoCurriculars(locations, max_cost=1000)(1000)
    meetings = meetings + co_meetings
    coCurricularHasClub = makeCoCurricularHasClub(co_curriculars, clubs)(10000)
    registered = makeRegistered(classHasStudent, curriculars)()
    
    to_csv('./csv/clubs.csv', clubs)
    to_csv('./csv/students.csv', students)
    to_csv('./csv/classes.csv', classes)
    to_csv('./csv/locations.csv', locations)
    to_csv('./csv/classHasStudent.csv', classHasStudent)
    to_csv('./csv/events.csv', events)
    to_csv('./csv/assignments.csv', assignments)
    to_csv('./csv/curriculars.csv', curriculars)
    to_csv('./csv/co_curriculars.csv', co_curriculars)
    to_csv('./csv/meetings.csv', meetings)
    to_csv('./csv/coCurricularHasClub.csv', coCurricularHasClub)
    to_csv('./csv/registered.csv', registered)

def makeStudent(): 
    def gen(amount):
        studentIDs = set(fake.unique.random_int(min=1650000, max=1750000) for i in range(amount))
        students = []
        for id in studentIDs:
            students.append({ 'id': id, 'name': fake.name(), 'phone': fake.phone_number() })
        return students
    return gen

def makeClassNames(amount):
    prefix = ("ANTHY","APPLN","ARTSC","ARTSW","CHINE","CRIMN","CRSCI","DANCE","ENGLI","ENSLA","ENVPL","EVSOC","FPSYC","GEOGY","HDEVP","HISTY","HUMRI","INTLC","IRSST","JAPAN","KOREA","LABST","LEGAL","LINGS","MEDIA","MUSIC","PHILO","POLCY","POLSC","POPST","PSYCH","SOCIO","SOCPY","SOCWK","SPNSH","THSTS","WGSTS","WRITE","APHYS","AQCUL","AIMLX","BIOMD","ENGCB","CHEMY","ENGCV","CLIMT","CMYHE","COMPX","DATAA","DSIGN","EARTH","BIOEB","ENGEE","ENGEN","ENGEV","ENVSC","HTHPR","HLTSC","HSHUP","HTHAL","HPSCI","MARIN","ENGMP","MATHS","ENGME","BIOMO","NURSE","PHYSC","SCIEN","STATS","SDCOA","ADLNG","COUNS","DLRNG","DINST","EDART","EDLED","EDSOC","EDUCA","ENVED","GLOBE","HMDEV","LLTED","MAOED","MTHED","SCIED","SCTED","THEDR","TOEDR","TWEDR","TEEDU","TEPRO","TEACH","TECED","ACCTN","AGBUS","BUSAN","COMMS","DIGIB","ECONS","ENTIN","EXCOR","EXMBA","EXMBM","FINAN","HRMGT","INMGT","LCOMM","LEADR","MNMGT","MGSUS","MGSYS","MRKTG","PRMGT","PUBRL","SCMGT","SCMGT","THMGT","MAORI","PACIS","CAAEN","CAENL","FOUND","FOUND","INDIP")
    numbers = tuple(set(fake.unique.random_int(min=101, max=999) for i in range(30)))
    classnames = set()
    
    for i in range(amount):
        className = random.choice(prefix) + str(random.choice(numbers))
        classnames.add(className)

    return tuple(classnames)

def makeLocationRooms(amount):
    buildings = ('R', 'S', 'L', 'K', 'H', 'MSB')
    floors = ('G', '1', '2', '3', '4')
    roomNumbers = tuple(str(i + 1) for i in range(30))
    locations = set()
    
    for i in range(amount):
        room = random.choice(buildings) + '.' + random.choice(floors) + '.' + random.choice(roomNumbers)
        locations.add(room)
        
    return tuple(locations)

def makeClasses():
    def gen(amount):
        return tuple([{ 'name': name } for name in makeClassNames(amount)])
    return gen
    

def makeLocations():
    def gen(amount):
        return tuple([{ 'room': room } for room in makeLocationRooms(amount)])
    return gen
    
def makeClue(): 
    def gen(amount):
        names = set()
        clubs = []
        # gen unique names
        while len(clubs) < amount:
            n = fake.name()
            if n in names: continue
            else: names.add(n)
            clubs.append({ 'name': n, 'description': fake.text() })
    
        return clubs
    return gen

def makeClassHasStudent(students, classes):
    def gen(min, max):
        has = []
        
        for student in students:
            count = random.randint(min, max)
            # each student must have at least 3 events
            for c in random.sample(classes, count):
                has.append({
                    'studentID': student['id'],
                    'className': c['name'],
                })
                
        return tuple(has)
    return gen    
    
# each class has to have at least min locations
def makeEvents(classes, locations):
    types = ('Lecture', 'Lab', 'Tutorial')
    
    def gen(min, max):
        events = []
        for c in classes: 
            for l in random.sample(locations, random.randint(min, max)): 
                events.append({
                    'className': c['name'],
                    'locationRoom': l['room'],
                    'type': random.choice(types),
                    'time': fake.time('%H:00')
                })
        return events 
    return gen

# each class has to have at least min assignment
def makeAssignments(classes): 
    types = ('Assignment', 'Test', 'Quiz')
    # min size of assignment for a class
    # max size of assignment for a class
    def gen(min, max):
        assignments = []
        for c in classes: 
            for l in range(random.randint(min, max)): 
                assignments.append({
                    'assignmentName': fake.name(),
                    'className': c['name'],
                    'type': random.choice(types),
                    'time': fake.date_time_this_year().strftime('%Y-%m-%d %H:00:00')
                })
        return tuple(assignments)
    return gen

def makeCurriculars(assignments, locations): 
    mm = makeMeeting(locations)
    
    def gen(amount):
        meetings = []
        curriculars = []
        
        for i in range(amount): 
            meeting = mm()
            a = random.choice(assignments)
            curriculars.append({ 'meetingID': meeting['id'], 'assignmentName': a['assignmentName'], 'className': a['className'] })
            meetings.append(meeting)
            
        return (tuple(curriculars), tuple(meetings))
    return gen

def makeCoCurriculars(locations, max_cost): 
    mm = makeMeeting(locations)
    
    def gen(amount):
        meetings = []
        curriculars = []
        
        for i in range(amount): 
            meeting = mm()
            curriculars.append({ 'meetingID': meeting['id'], 'cost': random.randint(0, max_cost) })
            meetings.append(meeting)
            
        return (tuple(curriculars), tuple(meetings))
    return gen

def makeMeeting(locations):
    def gen(): 
        h = random.randint(1, 3)
        l = random.choice(locations)
        d = fake.date_time().replace(hour=0, minute=0, second=0, microsecond=0)
        start = fake.date_time_between(d + timedelta(hours=(8)), d + timedelta(hours=(17)))
        end = start + timedelta(hours=h)
        
        return {
            'id': fake.unique.random_int(min=1000),
            'date': start.strftime('%Y-%m-%d'),
            'startTime': start.strftime('%H:00:00'),
            'endTime': end.strftime('%H:00:00'),
            'minutes': fake.text(),
            'locationRoom': l['room'],
        }
    return gen

def makeCoCurricularHasClub(co_curriculars, clubs):
    def gen(amount):
        keys = set()
        has = []
        count = 0
        
        if (len(clubs) * len(co_curriculars)) <= amount: 
            raise Exception("the number of club and co_curricular is not enought.")
        
        while len(has) < amount:  
            count += 1
            o = { 'meetID': random.choice(co_curriculars)['meetingID'], 'clubName': random.choice(clubs)['name'] }
            k = f"meetID:{o['meetID']},clubName:{o['clubName']}"
            if k in keys: continue
            keys.add(k)
            has.append(o)
            
            if count > (2 * amount): break
        
        return tuple(has)
    return gen

def makeRegistered(classHasStudent, curriculars):
    def gen():
        studentCurriculars = dict()
        registered = []
        assignmentStudentIDs = dict()
        
        for co in curriculars: 
            assignmentStudentIDs[co['assignmentName']] = (co, [chs['studentID'] for chs in filter(lambda chs: chs['className'] == co['className'], classHasStudent)]) 
        
        for aName in assignmentStudentIDs:
            (co, studentIDs) = assignmentStudentIDs[aName]
            # group by every three people
            groups = [studentIDs[i::3] for i in range(3)]
            registered += [{
                'studentID': studentID,
                'meetingID': co['meetingID'],
                'attendance': random.choice(['Y', 'N'])
            } for group in groups for studentID in group]
            
        return tuple(registered)
    return gen

def to_csv(filename, data): 
    csv_file = filename
    csv_obj = open(csv_file, 'w') 
    csv_writer = csv.writer(csv_obj)
    header = data[0].keys()
    csv_writer.writerow(header)
    for item in data:
        csv_writer.writerow(item.values())
    csv_obj.close()

main()