use KCET

--Student registration
create table candidateRegistration(studentRegId int primary key identity(1,1),firstName varchar(100),lastName varchar(100),userName varchar(100),studentPassword varchar(20),city varchar(100),
state varchar(100),pincode varchar(20),college varchar(100),branch varchar(50),yearOfPassing date);

--College registration

create table collegeRegistration(collegeRegId int primary key identity(1,1),collegeName varchar(100),collegeId varchar(10) not NULL, 
collegePassword varchar(20),city varchar(50),state varchar(100) );

--Kea admin registration

create table keaAdminRegistration(keaAdminID int primary key identity(1,1),userName varchar(100), userId varchar(20) not Null , keaAdminPassword varchar(20),
city varchar(50), state varchar(50) );

--- selecting credentials of all user types

select userName AS username, studentPassword AS password from candidateRegistration
union
select collegeName AS username, collegePassword AS password from collegeRegistration
union
select userName AS username, keaAdminPassword AS password from keaAdminRegistration;

--- college preferences

create table collegePreferences(preferId int primary key identity(1,1),studentid int,foreign key (studentid) references candidateRegistration(studentRegId)
,collegeName varchar(100),collegeCode varchar(10),branch varchar(10),preferenceNumber int);

-- college preferences of a particular student
select * from collegePreferences where studentid='1';

--result/rank

create table result(studentid int,foreign key (studentid) references candidateRegistration(studentRegId),cetRank int);

--seat allotment

create table seatAllotment(studentid int,foreign key (studentid) references candidateRegistration(studentRegId),collegeName varchar(100),branch varchar(100),cetRound int);

--AnswerKeys

create table answerKeySetA(questionNumber int, correctOption varchar(2));
create table answerKeySetB(questionNumber int, correctOption varchar(2));
create table answerKeySetC(questionNumber int, correctOption varchar(2));

--Student Application 
create table studentapplication (
   studentid int,foreign key (studentid) references candidateregistration(studentregid),
    firstname varchar(100),lastname varchar(100),email varchar(100),address varchar(255),
    currentaddress varchar(255),city varchar(100),state varchar(100) default 'karnataka',
    zip varchar(20),phonenumber varchar(20),category varchar(50) default 'general',aadharnumber varchar(20),
    governmentid varbinary(max),passportphoto varbinary(max),markscard10th varbinary(max),markscard12th varbinary(max),
    physicsmarks int,chemistrymarks int,mathsmarks int
);

-- Student Grievance
create table studentGrievance(studentid int,foreign key (studentid) references candidateregistration(studentregid),grievance varchar(1000));

