# Movie rental application
SpringBoot application with MongoDB set up on docker compose.

# Starting the application

**Prerequisite**

Docker must be installed and running to start application. 

## Start

First make new clean build

```
make clean
```
Copy new jar file to root direcory for dockerfile access

```
cp target/demo-0.0.1-SNAPSHOT.jar ./
```

At project root folder run 
```
make start
```
Or
```
docker compose start

Other commands and shortcuts available at "Makefile"

**Application**

For more convenient testing, some buttons are added to browser.

Open **localhost:8080** on browser

* Get all movies - Returns all movies from database
* Movies from json/yaml - Inserts all movies from file
NB - every new insertion rewrites old values.
* Delete movies - Deletes all movies from database
* Rented movies - Return all currently rented movies
* Available movies - Returns all available movies
* Most popular - Returns all movies sorted by most popular

Other API endpoints are in src/main/java/controller/MovieController.java

## Database/MongoExpress

To view/use database open MongoExpress **localhost:8081**

To create new database: 
* Use "Movies from json/yaml" button at **localhost:8080** and database is created automatically. 
* Otherwise select "Create Database" with "movies" as database name