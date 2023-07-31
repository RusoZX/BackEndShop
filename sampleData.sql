
-- Insert test data for Product
INSERT INTO product (id,title, price, category, brand, color, weight, volume, stock, total_sold)
VALUES (1,'Chair', 24.99, 'Furniture', 'IKEA', 'Wood', 1.0, 1.0, 56, 32),
       (2,'Table', 35.0, 'Furniture', 'IKEA', 'White', 7.5, 2.0, 76, 25),
       (3,'Barbecue', 76.95, 'Furniture', 'Carrefour', 'Black', 4.25, 2.5, 145, 50),
       (4,'Cupboard', 150.0, 'Furniture', 'Leroy Merlin', 'Wood', 50.0, 25.0, 79, 101),
       (5,'Parasol', 4.99, 'Furniture', 'Tenzen', 'Red', 0.5, 1.0, 23, 53),
       (6,'Robot', 22.95, 'Toys', 'Tenzen', 'Black', 1.0, 0.3, 77, 24),
       (7,'Ball', 9.99, 'Toys', 'Blanes', 'White', 0.5, 1.0, 87, 14),
       (8,'BasketBall', 13.95, 'Toys', 'Blanes', 'Orange', 0.7, 1.5, 145, 50),
       (9,'Car', 150.0, 'Toys', 'Tenzen', 'Gray', 2.0, 1.0, 79, 47),
       (10,'Bear Toy', 4.99, 'Toys', 'Tenzen', 'Brown', 0.5, 1.0, 56, 12),
       (11,'Chess', 7.00, 'Toys', 'Tenzen', 'Wood', 0.5, 1.0, 78, 23),
       (12,'Spoons', 4.99, 'Kitchen', 'IKEA', 'Inox', 0.5, 0.5, 56, 32),
       (13,'Forks', 4.99, 'Kitchen', 'IKEA', 'Inox', 0.5, 0.5, 76, 25),
       (14,'Plates', 14.95, 'Kitchen', 'IKEA', 'White', 1.00, 0.5, 145, 50),
       (15,'Pan', 20.95, 'Kitchen', 'Moulinex', 'Black', 2.0, 1.0, 79, 101),
       (16,'Wok Pan', 49.99, 'Kitchen', 'Moulinex', 'Black', 0.5, 1.0, 23, 53),
       (17,'Pot', 22.95, 'Kitchen', 'Moulinex', 'Black', 1.0, 0.3, 77, 24),
       (18,'Pressure Cooker', 34.95, 'Kitchen', 'Moulinex', 'Inox', 1.5, 1.3, 87, 14),
       (19,'Strainer', 3.95, 'Kitchen', 'IKEA', 'White', 0.1, 0.2, 145, 50),
       (20,'Kitchen Clamps', 2.35, 'Kitchen', 'IKEA', 'Inox', 0.3, 0.2, 79, 47),
       (21,'Knifes', 23.99, 'Kitchen', 'Moulinex', 'Inox', 0.5, 1.0, 56, 12),
       (22,'Soup Plates', 7.00, 'Kitchen', 'IKEA', 'White', 0.5, 1.0, 78, 23),
       (23,'Microwave', 49.95, 'Electrodomestics', 'Carrefour', 'Inox', 7.0, 1.0, 79, 101),
       (24,'Fridge', 49.99, 'Electrodomestics', 'Carrefour', 'White', 30.7, 5.0, 23, 53),
       (25,'Television', 369.95, 'Electrodomestics', 'Carrefour', 'Black', 7.5, 2.5, 77, 24),
       (26,'Speaker', 34.95, 'Electrodomestics', 'Carrefour', 'Black', 0.5, 0.3, 87, 14),
       (27,'Washing Machine', 334.95 'Electrodomestics', 'Carrefour', 'White', 0.1, 0.2, 145, 50),
       (28,'Blender', 23.35, 'Electrodomestics', 'Carrefour', 'Inox', 0.3, 0.2, 79, 47),
       (29,'Toaster', 14.99, 'Electrodomestics', 'Carrefour', 'Inox', 0.5, 0.4, 56, 12),
       (30,'Oven', 235.00, 'Electrodomestics', 'Carrefour', 'Inox', 14.0, 1.5, 78, 23);

-- Insert test data for User, their passwords are 123
INSERT INTO `user` (id,name, surname, birthdate, email, pwd, role)
VALUES (0,'Admin', '', '2000-01-01', 'admin@example.com', '$2a$10$2xbOG6KMYABmPO7N0/GnPeHuAJWwXNdZSoAb5DCmld9oBoINLVn.e', 'employee'),
(1,'John', 'Doe', '2000-01-01', 'john.doe@example.com', '$2a$10$2xbOG6KMYABmPO7N0/GnPeHuAJWwXNdZSoAb5DCmld9oBoINLVn.e', 'client'),
(2,'Maria', 'Asuncion', '2001-02-03', 'employee@example.com', '$2a$10$2xbOG6KMYABmPO7N0/GnPeHuAJWwXNdZSoAb5DCmld9oBoINLVn.e', 'employee');

-- Insert test data for ShoppingCart
INSERT INTO shopping_cart (user_id, product_id, quantity)
VALUES (1, 1, 2), (1, 2, 3);

-- Insert test data for Address
INSERT INTO address (id,country, city, postal_code, street, home, apartment, user_id)
VALUES (5,'Country 1', 'City 1', '12345', 'Street 1', 'Home 1', 'Apartment 1', 1),
(1,'Spain', 'Madrid', '28001', 'España', '242', 'local', 0),
(2,'Spain', 'Barcelona', '08001', 'Catalunya', '76', 'local', 0),
(3,'Spain', 'Malaga', '29001', 'Andalucia', '14', 'local', 0),
(4,'Spain', 'Almeria', '04001', 'Rambla', '2', 'local', 0);

-- Insert test data for Order
INSERT INTO `order` (id,user_id, user_address, payment_method, delivery_method, payment_status, order_status, created_date)
VALUES (1,1, 1, 'Payment Method 1', 'Delivery Method 1', true, 'Order Status 1', '2023-07-01');

-- Insert test data for Goods
INSERT INTO goods (order_id, product_id, quantity)
VALUES (1, 1, 10), (1, 2, 5);

