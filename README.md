# texastoc-server-v5

Upgrade [texastoc server v4](https://github.com/gpratte/texastoc-v4-integration-testing)

* Spring Boot from 2.4.4 to 3.1.2
* Java from 11 to 17

Begin with a minimal Spring Boot application and build it up bit by bit
by adding in the code
from [texastoc server v4](https://github.com/gpratte/texastoc-v4-integration-testing).

# Profiles, Building and Running

The spring boot application can be run

* in an IDE (only IntelliJ is covered)
* maven command line
* as a Docker container

### Maven Profile

Maven profiles are defined in the `<profiles>` section in the server/pom.xml. The maven profile
is used to bring in libraries (i.e. dependencies). The following profiles are defined:

* h2
* mysql

A maven profile can be specified on the command line by using the `-P` flag (e.g. `-P h2`). A maven
profile can be specified in IntelliJ by selecting a profile from the list of Profiles in the maven
tool window.

The h2 profile brings in the libraries to access an in-memory H2 database.

The mysql profile brings in the libraries to access an external MySQL database.

### Runtime Variables

Here is a snippet from the application.yml file

```
db:
  h2: true
  mysql: false
  # There are other fields but they should be set as environment variables.
  # schema: false
  # seed: false
  # populate: false
```

The properties in the application.yml file are used by the server at runtime. For example the

```
db:
  h2: true
  mysql: false
```

properties translate into the server loading the connection information to connect to the H2
database.

### Running the server

#### IntelliJ

First select either the `h2` or `mysql` profile in the maven tool window. Note that h2 is the
default.

Next right click on the `Application` class in the Project window and select `Run` from the popup
window.

After the `Application` has been run once then it will have a run profile. You can edit the run
profile to add other environment variables. For example

```
DB_SCHEMA=true;DB_SEED=true;DB_POPULATE=true
```

Running in IntelliJ is obviously the easiest way to step through code in the debugger.

#### mvn command

To run with `mvn` define the maven profile on the command line with `-P`.
Note that h2 is the default when `-P` is not specified.

Environment variables can be passed on the command line or set as variables in the terminal window.

###### pass variable on the command line

Here's an example of using the `h2` profile and some command line paramters

```
mvn -Dspring-boot.run.arguments="--db.h2=true --db.mysql=false --db.schema=true --db.seed=true --db.populate=true" -P h2 -pl server spring-boot:run
```

###### set variables in the terminal session

As an alternative to passing the variables on the command line they can be set in the terminal (Mac)
which runs the server. Set an environment variable like so

```
export DB_H2=true
export DB_MYSQL=false
export DB_SCHEMA=true
export DB_SEED=true
export DB_POPULATE=true
```

and then run the server like so

```
mvn -P h2 -pl server spring-boot:run
```

Note that an environment variable can be removed by `unset`. For example

```
unset DB_H2
```

###### run the JVM in debug mode

The `mvn` command can be appended with the following to allow a debugger to connect on port 8787

```
-Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8787"
```

#### Docker

The server can be run in a standalone container when using the in-memory H2 database or alongside a
MySQL container.

###### Running with H2

First build the jar

```
mvn -P h2 -pl server clean package
```

Next build the Docker image

```
docker build -t texastoc-v5-h2-image .
```

A snippet of how the image looks

```
REPOSITORY                TAG
texastoc-v5-h2-image      latest
```

You will set the environment either in the terminal or in the docker-compose file. Here's the
enviroment variables being set in the `docker-compose-server-h2.yml` file

```
environment:
  SPRING_APPLICATION_JSON: >
    {
      "db": {
        "h2": true,
        "mysql": false,
        "schema": true,
        "seed": true,
        "populate": true
      }
    }
```

Bring up the container

* `docker compose -f docker-compose-server-h2.yml up -d`

Check that the docker container is running

* `docker ps -a`

Watch the server logs

* `docker logs server`

Sanity check

* `curl http://localhost:8080/api/v4/settings`

Take down the container

* `docker compose -f docker-compose-server-h2.yml down`

###### Running with MYSQL

First configure MySQL standalone. Bring up the MySQL docker container (this will create a `./data`
directory fyi).

* `docker compose -f docker-compose-mysql.yml up -d`

Check that the docker container is running

* `docker ps -a`

Use the mysql client to reset the root password, create another user and create a database.

When the mysql container started and there was no data directory then mysql used the
environment variable from docker compose file to set the root password. Hence the
root password is `secret`

* `mysql -h 127.0.0.1 -P 3306 -u root -p`
* `ALTER USER 'root'@'localhost' IDENTIFIED BY 'rootpass';`
* `CREATE USER 'tocuser'@'%' IDENTIFIED BY 'tocpass';`
* `GRANT ALL PRIVILEGES ON * . * TO 'tocuser'@'%';`
* `FLUSH PRIVILEGES;`
* `quit;`
* `mysql -h 127.0.0.1 -P 3306 -u tocuser -p`
* `create database toc;`
* `quit;`

and then stop the container `docker compose -f docker-compose-mysql.yml down`

Build the jar

```
mvn -P mysql -pl server clean package
```

Next build the Docker image

```
docker build -t texastoc-v5-mysql-image .
```

A snippet of how the image looks

```
REPOSITORY                TAG
texastoc-v5-mysql-image   latest
```

Bring up both the mysql and server containers

* `docker compose -f docker-compose-server-mysql-populate.yml up -d`

Check that the containers are running

* `docker ps -a`

Tail the server logs to see the population (control c to stop the tail)

* `docker logs -f server`

Sanity check

* `curl http://localhost:8080/api/v4/settings`

Take down the containers

* `docker compose -f docker-compose-server-mysql-populate.yml down`

Now bring up mysql and the server which does not run the population

* `docker compose -f docker-compose-server-mysql.yml up -d`

Sanity check

* `curl http://localhost:8080/api/v4/settings`

Take down the containers

* `docker compose -f docker-compose-server-mysql.yml down`

# Connect to the H2 server

When running the dev server the H2 database can be access as follows:

* open `http://localhost:8080/h2-console` url.
* set the JDBC URL to `jdbc:h2:mem:testdb`
* User Name `sa`
* Leave the password empty
* Click Connect

# Testing

There are unit tests and integration tests.

### Unit tests

You can run the unit tests in IntelliJ or from the command line.

To run in IntelliJ right click on the `java` folder

* application -> src -> test -> java

and choose _Run 'All Tests'_

To run all the tests from the command line type

* mvn test

### Integration tests

To run the one or more integration tests the server first has to be started with the variable
that allows multiple seasons to be created

```
mvn -Dspring-boot.run.arguments="--db.h2=true --db.schema=true --db.seed=true --test.allowMultipleSeasons=true" -P h2 -pl server spring-boot:run
```

To run in IntelliJ right click on the java folder

* integration -> src -> test -> java

and choose _Run 'All Tests'_

# Branches

The branch labels are prefixed in the order they were developed (e.g. 01, 02, ...).

Choose the branch from the github list of branches to see the readme for that branch.

## Branch 14-build-and-run

Resurrect docker files and flesh out readme for building and running
