This Server uses MySQL, to change it configuration go to /src/main/resources/application.properties
There you can change the url for the MySQL Server, username and password.
There you can also change the server port, I use the 8081.

In the same folder as this file will be a sql file to create the data base named createDB.sql

To start the backend server run the main method on
/src/main/java/com.daniilzverev.shopserver/ShopserverApplication

To test the server via Postman put the Authorization to NoAuth in /user/login and /user/signup
and in Body use raw JSON like this:
{
    "name":"test",
    "surname":"test1"
}

To test via Postman when Authorization needed, first, send a Post to /user/login with valid username and password.
Get the generated token, should look like this:
{
"token":
"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlMUBleGFtcGxlLmNvbSIsInB3ZCI6ImNsaWVudCIsI
mV4cCI6MTY4NzkyNDkxMCwiaWF0IjoxNjg3ODg4OTEwfQ.vTMj279tXrOrNw8V5N0LvVYu4aUzk_5JeikfAWlunyg"
}
Then take the Token String and go to Authorization in Postman, in type select Bearer Token and in the field Token
insert the token.
