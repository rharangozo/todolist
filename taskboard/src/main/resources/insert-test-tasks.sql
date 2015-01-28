/* 
    !!! Some test relies on these records !!!
*/

insert into TASK(DESCRIPTION, TASKORDER, USER_ID) values ('First Task', 1, 'testuser');
insert into TASK(DESCRIPTION, TASKORDER, USER_ID) values ('Second Task', 2, 'testuser');

insert into TAG(TAG_NAME, FK_TASK_ID) values ('testtag', 1);