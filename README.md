MOD 4 project Group 37 setup guide

HOW TO SETUP DB: 
Run all the files in the sql folder on the database being used

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

You can run the server by compiling the .war file and deploying with tomcat

Make sure to include the appropriate postgresql .jar file that was provided on canvas for the JDBC driver if you want to compile the code

We use SASS / SCSS and bootstrap for styling. Please look at documents/UsingSass.md to see how to compile this to CSS.

Database credentials on castle.ewi.utwente.n/phppgadmin:

username: di125
password: E9+gNMnM

We did not deploy the database on the deployment environment, because it did not work for us