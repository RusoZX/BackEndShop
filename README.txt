This Server uses MySQL, to change it configuration go to /src/main/resources/application.properties
There you can change the url for the MySQL Server, username and password.
There you can also change the server port, I use the 8081.

The database needs to be created manually and spring boot will create the tables structure.

There is a sql file to insert some sample data to test via postman and in the front end.

To start the backend server run the main method on:
/src/main/java/com/daniilzverev/shopserver/ShopserverApplication

To test the server via Postman put the Authorization to NoAuth in /user/login , /user/signup, /product/get{prodoductId},
/product/getBy{method}, /product/getAllCategories
and in Body use raw JSON like this:
{
    "name":"test",
    "surname":"test1"
}

To test via Postman when Authorization needed you need a token, to generate it,
 send a Post request to /user/login with valid username and password, like this:
 {
 "email":"john.doe@example.com",
 "pwd":"123"
 }
The response should look like this:
{
"token":
"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlMUBleGFtcGxlLmNvbSIsInB3ZCI6ImNsaWVudCIsI
mV4cCI6MTY4NzkyNDkxMCwiaWF0IjoxNjg3ODg4OTEwfQ.vTMj279tXrOrNw8V5N0LvVYu4aUzk_5JeikfAWlunyg"
}
/-/Have in mind that the token expires in ten hours, but you can all ways generate a new one./-/

Now, if you want to request something that requires Authorization, create a new request, like /order/getAll.
In the Authorization tab select the bearer type in the dropdown and insert the token in the input to the right.
Then you can execute the request.
/-----------------------------------------------/REQUEST STRUCTURE/--------------------------------------------------/
If you want to test some post request and dont know the needed json structure for it to work, go to the ServiceImpl of
the service you want to test, for example, if you want to test /user/profile/update go to
../serviceImpl/UserServiceImpl and find the checkMap function used for the update of the profile, they are at the bottom.
In this case is:
private boolean checkUpdateMap(Map<String,String> requestMap)
Now look at the if statements, and you will know which fields are required, in this case we see that it needs to contain
at least one field of name, surname and birthday or all of them.

Knowing this construct the Json request:
{
"name":"newName"
}
or
{
"name":"newName",
"surname":"newSurname",
"birthday":"2001-09-11"
}
/-/Have in mind that the date format is yyyy-MM-dd/-/

If it is a GET or DELETE Request you will need to put the data in the path of the request.
Lets see this case from the product Rest Controller:
@GetMapping(path="/getBy{method}")
    ResponseEntity<List<ProductWrapper>> getProducts(@PathVariable String method,
                                                     @RequestParam(value = "limit") String limit,
                                                     @RequestParam(value = "search", required = false) String search);

We have a PathVariable specified in path="/getBy{method}" so all that goes after getBy until the next '/' or '?' will be the variable String method.
We have RequestParams which go after /product/getBy{method} like this  /product/getBy{method}?limit=10&search=brand,
in which ? states the start of the params and & states that the param before ends and starts a new one.

/---------------------------------------------------/URLS INFO/-------------------------------------------------------/
To change the MySQL Url you need to go into /src/main/resources/application.properties and there change the next line:
spring.datasource.url=jdbc:mysql://{{ your MySQL server url}}

In /src/main/java/com/daniilzverev/shopserver/JWT/SecurityConfig at:
@Override
    protected void configure(HttpSecurity http) throws Exception {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("YOUR FRONT END URL"));
In here you have to put your front end url so the Browser on the front end will allow the requests.

