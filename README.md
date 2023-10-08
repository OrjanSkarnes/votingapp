# VotingApp

## Introduction

Welcome to the repository for the `VotingApp` project. This application is part of the DAT250 assignment and aims to provide a robust voting system.


![Design document](design_document.pdf)

## Table of Contents
- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
- [Setup](#setup)

## Prerequisites

- Docker
- Java
- Gradle
- MySQL
- An IDE (preferably IntelliJ)

## Setup

### Using Gradle bootRun

1. Open a terminal and navigate to the project root directory.
2. Run the following command:

    ```bash
    gradle bootRun
    ```

   This will automatically start a Docker container for MySQL and boot up the application.

### Manually (For IntelliJ or other IDEs)

1. Manually start the MySQL container with Docker:

    ```bash
    docker-compose up
    ```
2. Run the application from your IDE.

Either way, the application should be accessible at [http://localhost:8080](http://localhost:8080).