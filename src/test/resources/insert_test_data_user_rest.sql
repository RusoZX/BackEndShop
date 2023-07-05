Insert into user(id, name, surname, email, birthdate, pwd, role)
 values(-1, "test", "test1", "example1@example.com",
 "2001-09-11", "someEncryptedData", "client"),
 (-2, "test", "test1", "employee@example.com",
 "2001-09-11", "someEncryptedData", "employee");
Insert into address(id,country,city,postal_code,street,home,apartment,user_id) values
(-1,"idk","idk","idk","idk","idk","idk",-1),
(-2,"idk1","idk1","idk1","idk1","idk1","idk1",-1),
(-3,"idk1","idk1","idk1","idk1","idk1","idk1",-2);