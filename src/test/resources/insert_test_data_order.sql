Insert into user(id, name, surname, email, birthdate, pwd, role) values(-1, "test", "test1", "example1@example.com",
 "2001-09-11", "someEncryptedData", "client"),
 (-2, "test", "test1", "employee@example.com","2001-09-11", "someEncryptedData", "employee"),
 (-3, "test", "test1", "example@example.com","2001-09-11", "someEncryptedData", "client");
 Insert into product
(id,brand,category,color,price,stock,title,volume,weight) values
(-1,"test", "test1","test2",10,10,"test3",10,10),
(-2,"test1", "test2","test3",10,10,"test4",10,10);

Insert into address(id,country,city,postal_code,street,home,apartment,user_id) values
(-1,"idk","idk","idk","idk","idk","idk",-1),
(-2,"idk","idk","idk","idk","idk","idk",-3);

Insert into `order`(id, user_id,user_address, payment_method, delivery_method, payment_status, order_status) values
(-1,-1,-1,"cash","delivery",false,"pending"),
(-2,-3,-2,"cash","delivery",true,"paid");

Insert into goods(id,order_id, product_id, quantity) values(-1,-1,-1,1),(-2,-1,-2,1),(-3,-2,-2,1);