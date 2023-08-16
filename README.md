# Dragonfly
Project task for Distribution Innovation


# Instructions

Run `mvn clean install` to generate /target/ 

Run `docker build . -t dragonfly` to create working Docker image

Run `docker run -p 8080:8080 dragonfly` to run the Docker image

Enter http://localhost:8080/swagger-ui/index.html#/ for API endpoints


# Description

The following application performs the following functionalities:

- Accepts a .csv file specifying the addresses of deliveries in a 'latitude longitude' format

- Processes the list and attaches it into a orders[] part of the input file

- Establishes the session with Spider
--- That requires separate session init and session await ready

- Initiates optimization 

- Polls optimization according to specified polling period

- Once the best solution value ceases to improve, stops the optimization

- Gets the current best routing solution from Spider

- Saves the current best solution locally

- Deletes the session in Spider

- Returns the found result back to the user in a body of a HTTP response


This process is fully available under the /fullProcess controller or decomposed into separate steps functions under each other controller.