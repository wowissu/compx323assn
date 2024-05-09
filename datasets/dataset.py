from faker import Faker
import random
from datetime import datetime, time, timedelta
import csv

fake = Faker()

def main():
    clubs = genClue()(500)
    students = genStudents()(10000)
    classes = genClasses()(300)
    locations = genLocations()(1000)
    # each class has students from min to max
    (classHasStudents, classStudentsDict) = genClassHasStudents(students, classes)(min = 12, max = 30)
    # each class has locations from min to max
    events = genEvents(classes, locations)(min = 3, max = 5)
    # each class has assignments from min to max
    (assignments, classAssnDict) = genAssignments(classes)(min = 0, max = 3)

    (curriculars, meetings, registereds) = genStudentsHaveCurriculars(classStudentsDict, classAssnDict, locations)()

    # (curriculars, meetings) = genCurriculars(assignments, locations)(1000)
    (co_curriculars, co_meetings) = genCoCurriculars(locations, max_cost=1000)(1000)
    co_registereds = genStudentJoinCoCurricular(students, co_curriculars)(10000)
    
    coCurricularHasClub = genCoCurricularHasClub(co_curriculars, clubs)(10000)

    meetings = meetings + co_meetings
    registereds = registereds + co_registereds
    
    to_csv('./csv/club.csv', clubs)
    to_csv('./csv/student.csv', students)
    to_csv('./csv/class.csv', classes)
    to_csv('./csv/location.csv', locations)
    to_csv('./csv/class_has_student.csv', classHasStudents)
    to_csv('./csv/event.csv', events)
    to_csv('./csv/assignment.csv', assignments)
    to_csv('./csv/curricular.csv', curriculars)
    to_csv('./csv/co_curricular.csv', co_curriculars)
    to_csv('./csv/meeting.csv', meetings)
    to_csv('./csv/co_curricular_has_club.csv', coCurricularHasClub)
    to_csv('./csv/registered.csv', registereds)


def genStudentsHaveCurriculars(classStudentsDict, classAssnDict, locations):
    def gen():
        curriculars = []
        meetings = []
        registereds = []

        # divide students
        for cName in classStudentsDict:
            if cName in classAssnDict: 
                assns = classAssnDict[cName]
                students = classStudentsDict[cName]
                groups = [students[x:x+3] for x in range(0, len(students), 3)]

                for group in groups:
                    for assn in assns:     
                        meeting = makeMeeting(random.choice(locations))
                        curricular = makeCurricular(meeting, assn)
                        registereds += [makeRegistered(student['studentID'], curricular['meetingID']) for student in group]
                        meetings.append(meeting)
                        curriculars.append(curricular)                

        return (tuple(curriculars), tuple(meetings), tuple(registereds))
    return gen

def genStudentJoinCoCurricular(students, co_curriculars):
    def gen(amount):
        registereds = []

        for i in range(amount):
            registereds.append(makeRegistered(random.choice(students)['id'], random.choice(co_curriculars)['meetingID']))

        return tuple(registereds)
    return gen

def genStudents(): 
    def gen(amount):
        studentIDs = set(fake.unique.random_int(min=1650000, max=1750000) for i in range(amount))
        students = []
        for id in studentIDs:
            students.append({ 'id': id, 'name': fake.name(), 'phone': fake.phone_number()[:20] })
        return tuple(students)
    return gen

def makeClassNames(amount):
    prefix = ("ANTHY","APPLN","ARTSC","ARTSW","CHINE","CRIMN","CRSCI","DANCE","ENGLI","ENSLA","ENVPL","EVSOC","FPSYC","GEOGY","HDEVP","HISTY","HUMRI","INTLC","IRSST","JAPAN","KOREA","LABST","LEGAL","LINGS","MEDIA","MUSIC","PHILO","POLCY","POLSC","POPST","PSYCH","SOCIO","SOCPY","SOCWK","SPNSH","THSTS","WGSTS","WRITE","APHYS","AQCUL","AIMLX","BIOMD","ENGCB","CHEMY","ENGCV","CLIMT","CMYHE","COMPX","DATAA","DSIGN","EARTH","BIOEB","ENGEE","ENGEN","ENGEV","ENVSC","HTHPR","HLTSC","HSHUP","HTHAL","HPSCI","MARIN","ENGMP","MATHS","ENGME","BIOMO","NURSE","PHYSC","SCIEN","STATS","SDCOA","ADLNG","COUNS","DLRNG","DINST","EDART","EDLED","EDSOC","EDUCA","ENVED","GLOBE","HMDEV","LLTED","MAOED","MTHED","SCIED","SCTED","THEDR","TOEDR","TWEDR","TEEDU","TEPRO","TEACH","TECED","ACCTN","AGBUS","BUSAN","COMMS","DIGIB","ECONS","ENTIN","EXCOR","EXMBA","EXMBM","FINAN","HRMGT","INMGT","LCOMM","LEADR","MNMGT","MGSUS","MGSYS","MRKTG","PRMGT","PUBRL","SCMGT","SCMGT","THMGT","MAORI","PACIS","CAAEN","CAENL","FOUND","FOUND","INDIP")
    numbers = tuple(set(fake.unique.random_int(min=101, max=999) for i in range(30)))
    classnames = set()
    
    for i in range(amount):
        className = random.choice(prefix) + str(random.choice(numbers))
        classnames.add(className)

    return tuple(classnames)

def genClasses():
    def gen(amount):
        return tuple([{ 'name': name } for name in makeClassNames(amount)])
    return gen
    

def genLocations():
    def gen(amount):
        return tuple([{ 'room': room } for room in makeLocationRooms(amount)])
    return gen
    
def genClue(): 
    def gen(amount):
        names = set()
        clubs = []
        # gen unique names
        while len(clubs) < amount:
            n = fake.name()
            if n in names: continue
            else: names.add(n)
            clubs.append({ 'name': n, 'description': fake.paragraph(nb_sentences = 4) })
    
        return clubs
    return gen

def genClassHasStudents(students, classes):
    def gen(min, max):
        has = []
        classStudentsDict = dict()

        for c in classes: 
            count = random.randint(min, max)
            cStudents = [{ 'studentID': s['id'], 'className': c['name'], } for s in random.sample(students, count)]
            has += cStudents
            classStudentsDict[c['name']] = cStudents
                
        return (tuple(has), classStudentsDict)
    return gen    
    
# each class has to have at least min locations
def genEvents(classes, locations):
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
def genAssignments(classes): 
    types = ('Assignment', 'Test', 'Quiz')
    # min size of assignment for a class
    # max size of assignment for a class
    def gen(min, max):
        assignments = []
        classAssnDict = dict()
        for c in classes: 
            assn = [{
                'assignmentName': fake.name(),
                'className': c['name'],
                'type': random.choice(types),
                'time': fake.date_time_this_year().strftime('%Y-%m-%d %H:00:00')
            } for l in range(random.randint(min, max))]
            assignments += assn
            classAssnDict[c['name']] = assn
        return (tuple(assignments), classAssnDict)
    return gen
    
def genCurriculars(assignments, locations): 
    def gen(amount):
        meetings = []
        curriculars = []
        
        for i in range(amount): 
            meeting = makeMeeting(random.choice(locations))
            a = random.choice(assignments)
            curriculars.append({ 'meetingID': meeting['id'], 'assignmentName': a['assignmentName'], 'className': a['className'] })
            meetings.append(meeting)
            
        return (tuple(curriculars), tuple(meetings))
    return gen

def genCoCurriculars(locations, max_cost): 
    def gen(amount):
        meetings = []
        curriculars = []
        
        for i in range(amount): 
            meeting = makeMeeting(random.choice(locations))
            curriculars.append({ 'meetingID': meeting['id'], 'cost': random.randint(0, max_cost) })
            meetings.append(meeting)
            
        return (tuple(curriculars), tuple(meetings))
    return gen

def genCoCurricularHasClub(co_curriculars, clubs):
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

def genRegistereds(classHasStudents, curriculars):
    def gen():
        studentCurriculars = dict()
        registered = []
        assignmentStudentIDs = dict()
        
        for co in curriculars: 
            assignmentStudentIDs[co['assignmentName']] = (co, [chs['studentID'] for chs in filter(lambda chs: chs['className'] == co['className'], classHasStudents)]) 
        
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

def makeLocationRooms(amount):
    buildings = ('R', 'S', 'L', 'K', 'H', 'MSB')
    floors = ('G', '1', '2', '3', '4')
    roomNumbers = tuple(str(i + 1) for i in range(30))
    locations = set()
    
    for i in range(amount):
        room = random.choice(buildings) + '.' + random.choice(floors) + '.' + random.choice(roomNumbers)
        locations.add(room)
        
    return tuple(locations)

def makeRegistered(studentID, meetingID):
    return {
        'studentID': studentID,
        'meetingID': meetingID,
        'attendance': random.choice(['Y', 'N'])
    }

def makeCurricular(meeting, assignment):
    return {
        'meetingID': meeting['id'], 'assignmentName': assignment['assignmentName'], 'className': assignment['className']
    }

def makeMeeting(location):
    h = random.randint(1, 3)
    d = fake.date_time().replace(hour=0, minute=0, second=0, microsecond=0)
    start = fake.date_time_between(d + timedelta(hours=(8)), d + timedelta(hours=(17)))
    end = start + timedelta(hours=h)
    
    return {
        'id': fake.unique.random_int(min=1000),
        'date': start.strftime('%Y-%m-%d'),
        'startTime': start.strftime('%H:00:00'),
        'endTime': end.strftime('%H:00:00'),
        'minutes': fake.paragraph(nb_sentences=5),
        'locationRoom': location['room'],
    }

def to_csv(filename, data): 
    #!python3
    with open(filename, 'w', newline='') as csv_file:
        csv_writer = csv.writer(csv_file)
        header = data[0].keys()
        csv_writer.writerow(header)
        for item in data:
            csv_writer.writerow(item.values())

main()