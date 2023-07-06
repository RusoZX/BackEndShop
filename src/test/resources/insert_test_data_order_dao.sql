Insert into user(id, name, surname, email, birthdate, pwd, role) values(-1, "test", "test1", "example1@example.com",
 "2001-09-11", "someEncryptedData", "client"),
 (-2, "test", "test1", "employee@example.com","2001-09-11", "someEncryptedData", "employee");

Insert into address(id,country,city,postal_code,street,home,apartment,user_id) values
(-1,"idk","idk","idk","idk","idk","idk",-1);

Insert into `order`(id, user_id,user_address, payment_method, delivery_method, payment_status, order_status) values
(-1,-1,-1,"cash","delivery",false,"pending");