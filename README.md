
### Update Using custom remote 

We have now set up our own deployment server, which can be found at: 
https://bookroom.nl/sqillsRoomBooking/

In order to long into desktop interface please use the following google account:
username: sqillsroombookinggroup37
password: SqillsProject!

If you would like to the google calendar intergration please send you email address too: 
a.j.heath@student.utwente.nl so that I add you to the calendar, there is no better way too do this as we cannot share calendars 
with account like the one given above as they are no apart of the utwente google suite.
In the case of real deployment at sqills this would not be a problem as all users would be in the same g suite.

These features will be fully show cased during the final presentation

You may need to use an incognito window, as you are likley already signed into 
google.



### How to deploy locally (see above)

Install 
- Java 8
- Maven
- Tomcat
- SASS 
- Git
- IntelliJ
- JDBC Postgresql Driver

Run locally
1. Clone the entire repository
2. `cd src/main/webapp`
3. `sass sass/desktop.sass:css/desktop.css`
4. `sass sass/tablet-design.sass:css/tablet-design.css`
5. `sass sass/book.sass:css/book.css`
6. `sass sass/specific-room.sass:css/specific-room.css`
7. Import JDBC jar to the project (just like in the tutorials)
8. Import maven dependencies
9. Run tomcat server (see tutorial instructions)
10. Go to localhost:8080
11. When you got to desktop interface and google wants you to authenticate:
username: sqillsroombookinggroup37
password: SqillsProject!

PLEASE NOTE: we tried to deploy our system in the deplpoyment environment, 
but because google authentication and calendar integration 
requires an SSL certificate, which the deployment environment doesn't have, 
the desktop interface doesn't work on the deployment server, because you can't 
autheniticate through google which means pages are not accessible/working.

Due to the lack of a real domain on the local version of the web app
the google intergtaion does not work as it requires a real domain, 
we had previously used a tunneling service during development to get around this,
the system will work as intended without google intergration (although we have not fully tested this).
We hope to soon have the project delpoyed a (private) server with SSL. Until then locally is the best we can provide, which doesnt have calendar integration