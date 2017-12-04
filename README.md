Setup Instructions
--

**Fat-Jar**

The microservice customer-analytics has been implemented using gradle wrapper. Hence, to build it, simply run the following command
in a terminal, at the root of the source folder to generate the runnable fat jar:

```
./gradlew clean build
```

NB: This command will run unit and integration tests before packaging
the fat jar. If you wish to speed up the build and discard them,
simply add this `-x test` option to the above command. Also, take note that,
given the list of third party dependencies, the build process might vary from
1 to 4 mins depending on your local gradle repository and your network bandwidth.

Then, to run the application, simply type:

```
java -jar <SOURCE_FOLDER>/build/libs/customer-analytics-1.0.jar
```

Alternatively, you could also use the bootRun gradle task to run the application (skipping the jar packaging altogether):

```
./gradlew bootRun
```

The endpoints will be available at the following URLs:

```
GET http://localhost:8080/transaction-summary/{customerId}/{month}
```


**Docker**


Another option to run this application would consist in packaging it as container
and run it through a Docker installation. Please note this approach will require you
to download and install Docker (refer to the official online documentation for this).

When Docker has been installed on your machine, just run the following gradle task
to generate the docker image:

```
./gradlew buildDocker
```

Then, after a few minutes, if the task run successfully, you should be able to see the application docker image
created in the local docker registry with the following name: "ing-direct/customer-analytics".

To run the application, just type in a terminal the following docker command:

```
docker run ing-direct/customer-analytics
```


**UI**

To access the UI of this application just open your favorite browser and enter `http://localhost:8080`. Make sure port `8080`
is not uses by another process. Then you should be able to access the Customer Classification page view.



Requirement assumptions
--

Given the provided high-level requirements the following list of assumptions have been made while designing the solution:

- Even though no detailed technical requirements have been provided, the following technical points have been
considered while implementing the solution:

    - Discoverability and consistency provided by HATEAOS and HAL specification
    (only implemented for error handling using `VndError` response object)
    - Vulnerability of the system through common XSS attacks
    - Performance and scalability of the system
    - Testability of the system from the start through BDD

Design and architecture decisions
--

The solution implemented has been designed in a microservice architecture model,
even though only one fat jar (covering both transaction resource and related summary details) has been produced.

This architecture provides the application several benefits among, being loosely coupled
with potential other services, stateless, scalable, resilent to failures.

Hereafter is a shortlist of the other technical aspects characterizing the application:

**Microservice using Spring Boot**

Spring Boot being one of the most mature framework and currently leading the trend in microservice applications,
the decision has been made to use it along with Java given the wide support it has for this language.
However, note that Scala or Kotlin could also have been good alternatives.

**HATEAOS**

HATEOAS along wit HAL specification have been used mainly for error messages using VndError response object.

**Security**

A XSS filter (encoding and stripping out malicious code from request parameter) has been added to the system
and being used to check submitted request and header values.

**Performance**

A particular emphasis was place on the performance aspect of the system through
the use of NoSQL database - namely mongodb (the system is actually comprising an embedded version
of mongodb), caching strategies (using guava cache), SPA AngularJS and localstorage
to minimize network round trips.

**SPA UI**

The UI of this application has been implemented using AngularJS 1.3 for which
the source can be found in the `resources/static` folder of this project. The UI
pages are responsive and built using `Bootstrap` making the view net and concise enough to be easily navigated.

**BDD and Testability**

The system has been coded entirely using BDD approach with spring-test and RESTAssured and Spock.
