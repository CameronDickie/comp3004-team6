# Spring Boot-React-Starter

# This is a simple Spring Boot-React application


# Setup is pretty simple.
- Clone the repository
- mvn clean, mvn install and mvn spring-boot:run
    - this will install all the dependencies you need

# Note for Mac users
- mvn install will claim that tests have failed. This is due to it being run synchronously on MacOS.
- While performing spring-boot:run there are no errors that are listed. If you want to avoid being warned of these test errors, skip testing on install using
- mvn install -Dskiptests

- These tests do infact work, if you run the Tests main function individually they all succeed. Additionally, if you are on Windows or Ubuntu, this is not an issue and mvn install can be run as normal.

# Logging into the System
- username: admin   |   password: pass
- otherwise register to be created in the system
