
-- Insert test data for Product
INSERT INTO product (id,title, price, category, brand, color, weight, volume, stock, total_sold)
VALUES (1,'Product 1', 10.0, 'Category 1', 'Brand 1', 'Color 1', 1.0, 1.0, 100, 50),
       (2,'Product 2', 20.0, 'Category 2', 'Brand 2', 'Color 2', 2.0, 2.0, 200, 100);

-- Insert test data for User, their passwords are 123
INSERT INTO `user` (id,name, surname, birthdate, email, pwd, role)
VALUES (1,'John', 'Doe', '2000-01-01', 'john.doe@example.com', '$2a$10$2xbOG6KMYABmPO7N0/GnPeHuAJWwXNdZSoAb5DCmld9oBoINLVn.e', 'client'),
(2,'Maria', 'Asuncion', '2001-02-03', 'employee@example.com', '$2a$10$2xbOG6KMYABmPO7N0/GnPeHuAJWwXNdZSoAb5DCmld9oBoINLVn.e', 'employee');

-- Insert test data for ShoppingCart
INSERT INTO shopping_cart (user_id, product_id, quantity)
VALUES (1, 1, 2), (1, 2, 3);

-- Insert test data for Address
INSERT INTO address (id,country, city, postal_code, street, home, apartment, user_id)
VALUES (1,'Country 1', 'City 1', '12345', 'Street 1', 'Home 1', 'Apartment 1', 1);

-- Insert test data for Order
INSERT INTO `order` (id,user_id, user_address, payment_method, delivery_method, payment_status, order_status, created_date)
VALUES (1,1, 1, 'Payment Method 1', 'Delivery Method 1', true, 'Order Status 1', '2023-07-01');

-- Insert test data for Goods
INSERT INTO goods (order_id, product_id, quantity)
VALUES (1, 1, 10), (1, 2, 5);

