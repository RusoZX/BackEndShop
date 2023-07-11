Insert into user(id, name, surname, email, birthdate, pwd, role) values(-1, "test", "test1", "example1@example.com",
 "2001-09-11", "$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu", "client");
 Insert into user(id, name, surname, email, birthdate, pwd, role) values(-2, "test", "test1", "example2@example.com",
 "2001-09-11", "$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu", "client");
Insert into product(id,brand,category,color,price,stock,title,volume,weight) values(-1,"test", "test1",
"test2",10,10,"test3",10,10);
Insert into product(id,brand,category,color,price,stock,title,volume,weight) values(-2,"test1", "test2",
"test3",10,10,"test4",10,10);
Insert into product(id,brand,category,color,price,stock,title,volume,weight) values(-3,"test1", "test2",
"test3",10,10,"test4",10,10);
Insert into shopping_cart(id,product_id,user_id,quantity) values(-1,-1,-1,3);
Insert into shopping_cart(id,product_id,user_id,quantity) values(-2,-2,-1,4);