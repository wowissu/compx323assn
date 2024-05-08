from faker import Faker
import random
fake = Faker()



def main():
    students = makeStudent()(1000)
    classes = makeClasses()(100)
    locations = makeLocations()(1000)
    classHasStudent = makeClassHasStudent(students, classes)(min = 3, max = 10)
    events = makeEvents(classes, locations)(min = 3, max = 10)
    
    print(classHasStudent);

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
    
def makeEvents(classes, locations):
    types = ('Lecture', 'Lab', 'Tutorial')
    events = []
    
    def gen(min, max):
        
            
        return events 
    return gen
    
main()