# [Ticket-Booking](https://github.com/DawnBreaker207/Ticket-Booking)

A booking film tickets system
## Background


## 🚀 Getting Started
A detail API endpoints can be found after installation on `http://localhost:8888/swagger-ui.html`

## 🛠  Installation

### Requirements

- A server with Java 17 version or [newer](https://www.oracle.com/java/technologies/downloads/)
- A database. Supports [MySQL](https://www.mysql.com) (minimum v8.0) 
- [Docker Desktop](https://docs.docker.com/desktop/setup/install/windows-install) in Window or [Docker](https://docs.docker.com/desktop/setup/install/linux/ubuntu) in Linux if setup with Docker


### Get the Source code and Install Packages

```bash
git clone https://github.com/DawnBreaker207/Ticket-Booking
cd Ticket-Booking
```
With Maven (run with terminal)

Requirement: [Set up maven local](https://www.baeldung.com/install-maven-on-windows-linux-mac)
```bash
mvn spring-boot:run
```

[With IntelliJ](https://www.jetbrains.com/help/idea/spring-boot.html#add-starter)

[With Eclipse](https://www.geeksforgeeks.org/java/how-to-run-your-first-spring-boot-application-in-eclipse-ide)
### Start the Application

With Maven (run with terminal)
```bash
mvn spring-boot:run
```
_By default, this will launch the application on `http://localhost:8888/api/v1`


### 🐳 Installing with Docker

To build the server container and start up a MySQL database, run:
```bash
docker-compose up --build
```

## 🔄 Getting Updates
To get the latest features, simply do a pull, install any new dependencies, and rebuild:

With Maven (run with terminal)
```bash
git pull
mvn clean install
mvn spring-boot:run
```
