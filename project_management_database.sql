-- we don't know how to generate root <with-no-name> (class Root) :(
create table Importance
(
	ImportanceId integer not null
		constraint ProjectImportance_pk
			primary key autoincrement,
	ImportanceName nvarchar(30) not null
);

create table ProjectStatus
(
	StatusId integer not null
		constraint ProjectStatus_pk
			primary key autoincrement,
	StatusName nvarchar(20) not null
);

create unique index ProjectStatus_StatusId_uindex
	on ProjectStatus (StatusId);

create unique index ProjectStatus_StatusName_uindex
	on ProjectStatus (StatusName);

create table User
(
	UserId integer not null
		constraint Users_pk
			primary key autoincrement,
	UserName nvarchar(20) not null,
	Password nvarchar(20) not null
);

create table Team
(
	TeamId integer not null
		constraint Team_pk
			primary key autoincrement,
	TeamName nvarchar(50) not null,
	ManagerId integer not null
		references User
			on update restrict on delete restrict,
	Code nchar(6) not null
);

create table MemberToTeam
(
	Id integer not null
		constraint MemberToTeam_pk
			primary key autoincrement,
	MemberId integer not null
		references User
			on update restrict on delete restrict,
	TeamId integer not null
		references Team
			on update restrict on delete restrict
);

create unique index MemberToTeam_Id_uindex
	on MemberToTeam (Id);

create unique index MemberToTeam_MemberId_TeamId_uindex
	on MemberToTeam (MemberId, TeamId);

create table Project
(
	ProjectId integer not null
		constraint Project_pk
			primary key autoincrement,
	Name nvarchar(50) not null,
	TeamId integer not null
		references Team
			on update restrict on delete restrict,
	Description text,
	Deadline nvarchar(100) not null,
	AssigneeId integer not null
		references User
			on update restrict on delete restrict,
	SupervisorId integer not null
		references User
			on update restrict on delete restrict,
	StatusId integer not null
		references ProjectStatus
			on update restrict on delete restrict,
	ImportanceId integer default 1 not null
		references Importance
			on update restrict on delete restrict,
	FinishingDate nvarchar(60)
);

create table Comment
(
	CommentId integer not null
		constraint Comment_pk
			primary key autoincrement,
	ProjectId integer not null
		references Project
			on update restrict on delete restrict,
	CommentText text not null,
	SenderId integer not null
		references User
			on update restrict on delete restrict,
	DateTime nvarchar(60) not null
);

create unique index Comment_Id_uindex
	on Comment (CommentId);

create index Project_Deadline_index
	on Project (Deadline desc);

create index Project_Importance_index
	on Project (ImportanceId desc);

create unique index Project_ProjectId_uindex
	on Project (ProjectId);

create index Project_Status_index
	on Project (StatusId);

create unique index Project_Team_Name_index
	on Project (TeamId, Name);

create unique index Team_Code_uindex
	on Team (Code);

create unique index Team_TeamId_uindex
	on Team (TeamId);

create unique index Users_UserId_uindex
	on User (UserId);

create unique index Users_UserName_uindex
	on User (UserName);

INSERT INTO Importance (ImportanceId, ImportanceName) VALUES (1, 'LOW');
INSERT INTO Importance (ImportanceId, ImportanceName) VALUES (2, 'MEDIUM');
INSERT INTO Importance (ImportanceId, ImportanceName) VALUES (3, 'HIGH');

INSERT INTO ProjectStatus (StatusId, StatusName) VALUES (1, 'TO_DO');
INSERT INTO ProjectStatus (StatusId, StatusName) VALUES (2, 'IN_PROGRESS');
INSERT INTO ProjectStatus (StatusId, StatusName) VALUES (3, 'TURNED_IN');
INSERT INTO ProjectStatus (StatusId, StatusName) VALUES (4, 'FINISHED');

INSERT INTO User (UserId, UserName, Password) VALUES (8, 'Admin', 'admin');
INSERT INTO User (UserId, UserName, Password) VALUES (9, 'Sarah', 'sarah');
INSERT INTO User (UserId, UserName, Password) VALUES (10, 'Dawn', 'dawn');
INSERT INTO User (UserId, UserName, Password) VALUES (11, 'Lilly', 'lilly');
INSERT INTO User (UserId, UserName, Password) VALUES (12, 'Ronald', 'ronald');
INSERT INTO User (UserId, UserName, Password) VALUES (13, 'Rudolf', 'rudolf');
INSERT INTO User (UserId, UserName, Password) VALUES (14, 'Emily', 'emily');
INSERT INTO User (UserId, UserName, Password) VALUES (15, 'Mark', 'mark');
INSERT INTO User (UserId, UserName, Password) VALUES (16, 'John', 'john');
INSERT INTO User (UserId, UserName, Password) VALUES (17, 'Bruno', 'bruno');

INSERT INTO Team (TeamId, TeamName, ManagerId, Code) VALUES (57, 'Java Team', 8, '871093');
INSERT INTO Team (TeamId, TeamName, ManagerId, Code) VALUES (58, 'Databases Team', 9, '275596');
INSERT INTO Team (TeamId, TeamName, ManagerId, Code) VALUES (59, 'Circuits Team', 10, '058603');
INSERT INTO Team (TeamId, TeamName, ManagerId, Code) VALUES (60, 'Programming Team', 11, '290763');
INSERT INTO Team (TeamId, TeamName, ManagerId, Code) VALUES (61, 'Arduino Team', 12, '998410');
INSERT INTO Team (TeamId, TeamName, ManagerId, Code) VALUES (62, 'Robotics Team', 8, '856291');
INSERT INTO Team (TeamId, TeamName, ManagerId, Code) VALUES (69, 'Literature Circle', 17, '237202');

INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (9, 8, 57);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (10, 9, 58);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (11, 10, 59);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (12, 11, 60);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (13, 12, 61);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (14, 8, 58);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (15, 8, 60);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (16, 8, 61);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (17, 8, 59);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (18, 8, 62);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (19, 12, 62);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (20, 11, 62);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (21, 10, 62);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (33, 17, 69);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (34, 14, 69);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (35, 9, 69);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (36, 10, 60);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (37, 13, 60);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (38, 16, 60);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (39, 11, 59);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (40, 16, 59);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (41, 16, 61);
INSERT INTO MemberToTeam (Id, MemberId, TeamId) VALUES (42, 13, 61);

INSERT INTO Project (ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, StatusId, ImportanceId, FinishingDate) VALUES (10, 'Walking Robot Project', 62, 'Create a robot which can move following a line, detect obstacles and avoid them.', '2021-02-18', 8, 8, 4, 3, '2021-01-03');
INSERT INTO Project (ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, StatusId, ImportanceId, FinishingDate) VALUES (11, 'Robotic Arm Project', 62, 'Create a robotic arm, which is able to grab an object and release it. Pay attention to the strength of the grip.', '2021-01-31', 10, 8, 1, 2, null);
INSERT INTO Project (ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, StatusId, ImportanceId, FinishingDate) VALUES (12, 'Fighter Robot Project', 62, 'Create a robot, which can fight with another robot, by turning it over or pushing it until the other one falls onto the ground.
The winner will be the last standing.', '2021-03-26', 12, 8, 2, 1, null);
INSERT INTO Project (ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, StatusId, ImportanceId, FinishingDate) VALUES (13, 'Arduino Mini Project', 62, 'Create a mini project in arduino with a given number of sensors and actuators.', '2021-02-03', 8, 12, 1, 2, null);
INSERT INTO Project (ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, StatusId, ImportanceId, FinishingDate) VALUES (15, 'Christmas Lights Show', 62, 'Create an arduino project which controls the christmas lights, generating a blinking, flashing and slowly fading pattern of lights.', '2021-01-19', 8, 12, 2, 3, null);
INSERT INTO Project (ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, StatusId, ImportanceId, FinishingDate) VALUES (31, 'Shakespeare Today', 69, 'A creative story about one of Shakespeare''s creations placed in modern times.', '2021-03-25', 9, 17, 2, 2, null);
INSERT INTO Project (ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, StatusId, ImportanceId, FinishingDate) VALUES (32, 'Beginning is the end', 69, 'A discussion about the theme of Creation and Origin exploited by many great Authors in their writings.', '2021-02-27', 14, 17, 1, 1, null);
INSERT INTO Project (ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, StatusId, ImportanceId, FinishingDate) VALUES (33, 'Pride and Prejudice differently told', 69, 'A different version of the original book, offering an alternative ending, and an unexpected twist.', '2021-02-24', 17, 17, 3, 3, null);
INSERT INTO Project (ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, StatusId, ImportanceId, FinishingDate) VALUES (34, 'Web application using database', 60, 'A web application which uses a database for getting the data, and creates a front-end ui to communicate with the user.
No restrictions on the programming language.', '2021-01-14', 8, 11, 1, 3, null);
INSERT INTO Project (ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, StatusId, ImportanceId, FinishingDate) VALUES (35, 'Snake game', 60, 'Create a simple snake game.
Language: C or C++
Level: Beginners', '2021-01-04', 13, 11, 1, 2, null);
INSERT INTO Project (ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, StatusId, ImportanceId, FinishingDate) VALUES (36, 'Java mini project', 60, 'A mini project written in Java language, that is built on the principles of OOP.
Level: Beginners', '2021-01-04', 10, 11, 3, 2, null);
INSERT INTO Project (ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, StatusId, ImportanceId, FinishingDate) VALUES (37, 'Mario game', 60, 'A simple game similar to the well-known Mario game, but a simplified version of it.
It is about traversing a path with obstacles which need to be avoided, jumping on stairs.
It contains only one level.
Language: Java
Level: Intermediate', '2021-01-04', 16, 11, 1, 3, null);
INSERT INTO Project (ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, StatusId, ImportanceId, FinishingDate) VALUES (38, 'Counting LEDs', 59, 'A simple circuit that implements a chain of LEDs that turn on one after the other as if following a counter.
Works in both directions.', '2021-01-04', 10, 10, 2, 2, null);
INSERT INTO Project (ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, StatusId, ImportanceId, FinishingDate) VALUES (39, 'Circuits mini project', 59, 'A mini project of creatig a circuit with some simple functionality, such as a sequence of blinking leds, filters, bridges or some classic circuits.', '2021-01-19', 11, 10, 2, 1, null);
INSERT INTO Project (ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, StatusId, ImportanceId, FinishingDate) VALUES (40, 'Flashing LEDs', 61, 'Simple arduino project controlling some LEDs.', '2021-01-20', 8, 12, 1, 2, null);

INSERT INTO Comment (CommentId, ProjectId, CommentText, SenderId, DateTime) VALUES (1, 11, 'Pay attention to the strength of the grip!', 8, '2021-01-03T01:32:47.013425800');
INSERT INTO Comment (CommentId, ProjectId, CommentText, SenderId, DateTime) VALUES (2, 11, 'Please correct the changes requested before turning in the project.', 8, '2021-01-03T01:34:40.870996800');
INSERT INTO Comment (CommentId, ProjectId, CommentText, SenderId, DateTime) VALUES (3, 11, 'I have corrected the things you pointed out, and the tests were successful too.', 10, '2021-01-03T01:35:52.183049600');
INSERT INTO Comment (CommentId, ProjectId, CommentText, SenderId, DateTime) VALUES (4, 11, 'Alright.', 8, '2021-01-03T01:36:51.533903600');
INSERT INTO Comment (CommentId, ProjectId, CommentText, SenderId, DateTime) VALUES (10, 31, 'I''m expecting some interesting ideas here!', 17, '2021-01-04T16:43:21.944239300');
INSERT INTO Comment (CommentId, ProjectId, CommentText, SenderId, DateTime) VALUES (11, 31, 'Originality is rewarded!', 17, '2021-01-04T16:44:07.507257200');
INSERT INTO Comment (CommentId, ProjectId, CommentText, SenderId, DateTime) VALUES (12, 39, 'Original ideas are rewarded!', 10, '2021-01-04T17:05:12.846761400');
