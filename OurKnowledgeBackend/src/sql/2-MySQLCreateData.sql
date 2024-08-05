INSERT INTO user (name, email, role, startDate)
VALUES ('Juan Riveira Veiga', 'example@example.com', 'Developer', null);
INSERT INTO user (name, email, role, startDate)
VALUES ('Roberto Riveira Veiga', 'roberto.riveira@outlook.es', 'Admin', null);


INSERT INTO technology(relevant, name, parentId) 
    VALUES (true, 'Languages', null);   /*1*/
INSERT INTO technology(relevant, name, parentId)
    VALUES (true, 'Backend', null);     /*2*/
INSERT INTO technology(relevant, name, parentId)
    VALUES (true, 'FrontEnd', null);    /*3*/
INSERT INTO technology(relevant, name, parentId) 
    VALUES (true, 'Java', 1);           /*4*/
INSERT INTO technology(relevant, name, parentId) 
    VALUES (true, 'C++', 1);            /*5*/
INSERT INTO technology(relevant, name, parentId) 
    VALUES (true, 'Python', 1);         /*6*/
INSERT INTO technology(relevant, name, parentId) 
    VALUES (true, 'Spring', 2);         /*7*/
INSERT INTO technology(relevant, name, parentId) 
    VALUES (true, 'SpringBoot', 7);     /*8*/
INSERT INTO technology(relevant, name, parentId) 
    VALUES (false, 'Maven', 2);          /*9*/
INSERT INTO technology(relevant, name, parentId) 
    VALUES (true, 'Database', null);    /*10*/
INSERT INTO technology(relevant, name, parentId) 
    VALUES (true, 'MySql', 10);         /*11*/
INSERT INTO technology(relevant, name, parentId) 
    VALUES (true, 'OracleSql', 10);     /*12*/


INSERT INTO knowledge(userId, technologyId, likeIt, mainSkill)
    VALUES (1, 1, false, false);
INSERT INTO knowledge(userId, technologyId, likeIt, mainSkill)
    VALUES (1, 4, true, true);
INSERT INTO knowledge(userId, technologyId, likeIt, mainSkill)
    VALUES (1, 5, true, false);
INSERT INTO knowledge(userId, technologyId, likeIt, mainSkill)
    VALUES (1, 6, false, false);
INSERT INTO knowledge(userId, technologyId, likeIt, mainSkill)
    VALUES (1, 2, true, true);
INSERT INTO knowledge(userId, technologyId, likeIt, mainSkill)
    VALUES (1, 7, true, false);
INSERT INTO knowledge(userId, technologyId, likeIt, mainSkill)
    VALUES (1, 8, true, true);