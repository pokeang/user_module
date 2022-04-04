# user_module
Simple user module with spring boot + thymeleaf + postgresql
This is the description of the user module
1. Database name is master_db, in folder db it has 4 files:
- master_db (sql) is sql format
- master_db.psql is psql format for restore db to pgAdmin
- ERD & ERD.png is the db diagram
2. This module using spring boot version 2.6.4, spring security and thymeleaf.
UI:  http//localhost:8080
+ This the feature can see as below pages:
- login page, default user: admin@gmail.com, password: password (after restore or import master_db.psql)
- register, edit, delete user (current login user can't delete)
- list users
- It has role ADMIN or USER, but UI not check condition (not implement). all types of users can see the same page.
3. Test using mockito, test only service and repository.

Note: For connect postgrasql can change connection in application.properties, like user & password.
