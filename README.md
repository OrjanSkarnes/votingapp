# votingapp
Repo for working with dat250 assignment

![Design document](Design documents-1 copy.pdf])

To run the application, use the command bootRun in the root folder of the project: this will start the docker container with mysql and run the application.

```
gradle bootRun
```

If you decide to run it another way (e.g. in IntelliJ), you will need to start the mysql container manually. This can be done with the following command:
```
docker-compose up
```

The application will be available at http://localhost:8080
