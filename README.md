### Java/PostgreSQL and JavaScript-based room-booking web app, in the corporate style of Sqills

Made for a hackathon that a local company (Sqills) organised for CS students.

In the case of real deployment at all users would need to be in the same g suite so that Google Calendar integration can be used.

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

PLEASE NOTE: we tried to deploy our system on a local deployment environment, 
but because google authentication and calendar integration 
requires an SSL certificate, which the deployment environment doesn't have, 
the desktop interface doesn't work on the deployment server, because you can't 
autheniticate through google which means pages are not accessible/working.
Thus, deployment requires a real server with an SSL certificate.

Due to the lack of a real domain on the local version of the web app
the Google intergtaion does not work as it requires a real domain.
We had previously used a tunneling service during development to get around this,
and the system should work as intended without Google intergration (although we have not fully tested this).
