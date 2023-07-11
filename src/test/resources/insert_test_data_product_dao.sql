Insert into user(id, name, surname, email, birthdate, pwd, role) values(-1, "test", "test1", "example1@example.com",
 "2001-09-11", "$2a$10$ZHead6J4P26hDO92na.lpeTe4pP6vJk01gEbqC28ojZk8873SgDcu", "client");
Insert into product
(id,brand,category,color,price,stock,title,volume,weight, total_sold) values
(-1,"test", "test1","test2",1,10,"test3",10,10,5),
(-2,"test1", "test2","test3",2,10,"test4",10,10,7),
(-3,"brand", "test2","test3",3,10,"test4",10,10,20),
(-4,"brand", "test2","test3",4,10,"test4",10,10,4),
(-5,"brand", "category","test3",5,10,"test3",10,10,0),
(-6,"brand", "category","test3",6,10,"test3",10,10,0),
(-7,"test1", "category","test3",7,10,"test3",10,10,1),
(-8,"test1", "category","color",8,10,"test3",10,10,7),
(-9,"test1", "category","color",9,10,"test3",10,10,11),
(-10,"test1", "test2","color",10,10,"test4",10,10,14),
(-11,"test1", "test2","color",11,10,"title",10,10,23),
(-12,"test1", "test2","color",12,10,"title",10,10,120),
(-13,"test1", "test2","test3",13,10,"title",10,10,3),
(-14,"test1", "test2","test3",14,10,"title",10,10,46),
(-15,"test1", "test2","test3",15,10,"title",10,10,96)
;
Insert into shopping_cart(id,product_id,user_id,quantity) values(-1,-1,-1,3);
Insert into shopping_cart(id,product_id,user_id,quantity) values(-2,-2,-1,4);