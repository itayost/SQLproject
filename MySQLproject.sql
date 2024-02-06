use mysqlproject;

/* Create Tables */ 

create table Question (
serialNum INT auto_increment PRIMARY KEY,
Qtext varchar(100) ,
UNIQUE(Qtext),
UNIQUE(serialNum))
Engine = InnoDB;

create table Answer (
answer_id INT auto_increment PRIMARY KEY ,
Atext varchar(100),
isTrue bool)
Engine = InnoDB;

create table OpenQuestion (
OpenQuestion_serialNum INT,
OpenAnswer_id INT ,
primary key (OpenQuestion_serialNum) ,
 foreign KEY (OpenQuestion_serialNum) REFERENCES question(serialNum) ON UPDATE CASCADE
 ON DELETE cascade,
 foreign KEY (OpenAnswer_id) REFERENCES answer(answer_id) ON UPDATE CASCADE
ON DELETE CASCADE)
Engine = InnoDB;

create table MultiChoiceQuestion (
MultiQuestion_serialNum INT,
primary key (MultiQuestion_serialNum),
foreign KEY (MultiQuestion_serialNum) REFERENCES question(serialNum) ON UPDATE CASCADE
 ON DELETE CASCADE
)
Engine = InnoDB;

create table MultiChoiceAnswers (
MultiQuestion_id INT ,
MultiAnswer_id INT,
foreign key(MultiQuestion_id) REFERENCES MultiChoiceQuestion(MultiQuestion_serialNum) ON UPDATE CASCADE
 ON DELETE CASCADE,
 foreign key(MultiAnswer_id) REFERENCES Answer(answer_id) ON UPDATE CASCADE
 ON DELETE CASCADE
)
Engine = InnoDB;

/* Drops Tables */ 

drop table MultiChoiceAnswers;
drop table MultiChoiceQuestion;
drop table OpenQuestion;
drop table Question;
drop table answer;

/* Insert Questions */ 

insert into Question(Qtext) values('What Are The Names Of The Project Members?');
insert into answer(Atext,isTrue) values ( 'Yarin And Itay' , true);
insert into Question(Qtext) values('Is It Worth 100 Points?');
insert into answer(Atext,isTrue) values ( 'Yes' , true);

insert into Question(Qtext) values('How Good Was The Project?');
insert into answer(Atext,isTrue) values ( '100' , true);
insert into answer(Atext,isTrue) values ( '95' , false);
insert into answer(Atext,isTrue) values ( '90' , false);
insert into Question(Qtext) values('Did You Enjoy The Project?');
insert into answer(Atext,isTrue) values ( 'No' , false);

/* Connect Tables */ 

insert into OpenQuestion values (1,1);
insert into OpenQuestion values (2,2);

insert into MultiChoiceQuestion values (3);
insert into MultiChoiceQuestion values (4);

insert into MultiChoiceAnswers values (3,3);
insert into MultiChoiceAnswers values (3,4);
insert into MultiChoiceAnswers values (3,5);
insert into MultiChoiceAnswers values (4,2);
insert into MultiChoiceAnswers values (4,6);

/* Show Connected Tables */ 

SELECT Question.Qtext, answer.Atext 
FROM OpenQuestion 
inner join Question ON OpenQuestion.OpenQuestion_serialNum = Question.serialNum 
inner join answer ON answer.answer_id = OpenQuestion.OpenAnswer_id ;
 
SELECT Question.Qtext, answer.Atext, answer.isTrue 
FROM MultiChoiceAnswers 
inner join Question ON MultiChoiceAnswers.MultiQuestion_id = Question.serialNum
inner join answer ON answer.answer_id = MultiChoiceAnswers.MultiAnswer_id ;

/* Show All Tables */ 

SELECT * FROM Question;
SELECT * FROM Answer;

/* Update Table */ 

UPDATE question SET Qtext = 'Is this project Worth 1000 Points?' WHERE Qtext = 'Is It Worth 100 Points?';

/* Delete Table Column */ 

DELETE MultiChoiceAnswers FROM MultiChoiceAnswers 
inner join Question ON MultiChoiceAnswers.MultiQuestion_id = Question.serialNum 
inner join Answer ON MultiChoiceAnswers.MultiAnswer_id = Answer.answer_id
WHERE Question.Qtext = 'How Good Was The Project?' AND Answer.Atext = '90';

