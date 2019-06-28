### How to deploy locally

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