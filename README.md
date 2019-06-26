MOD 4 project Group 37 setup guide



CREATING A USER WITH YOUR OWN EMAIL
In order to have access to our system, you need to have a google
account and that email should be in the DB
In order to put your email in the DB, follow the following steps
    - Go to /src/main/java/dao/userdao
    - uncomment the main method
    - fill in your name, e-mail(the one you want to login with) 
      and true or false for whether the account should have admin rights
Example: 
String name = "Platon Frolov"
String email = "pl.frolov99@gmail.com"
boolean admin = true
    -Run the main method
    -Your email is now in the DB, you can login to our system now through google
