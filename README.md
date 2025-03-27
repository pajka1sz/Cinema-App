# 🎬 CinemaProject
Cinema mangement window app created in **Java** using **REST**, **Spring** and **JavaFX**.
## About the project
We created a window application for managing the cinema, in which movies, rooms and screenings can be added, edited and deleted from the admin panel, where admin can also analyse statistics. Users can book tickets for screenings and give feedback on the films they have seen.  
### 🔐 Authentication and authorization
Users can sign up for a new account or log into an already existing one. Their credentials are encrypted for security. Every new user gets a default role of `CinemaUser`.

### 🎯 Functionality
User is allowed to:
* check available movies and filter them
* reserve a seat for a screening
* rate already watched movie
* check added rates and created reservations
* delete added rates and future reservations

Admin can:
* add, edit and delete movies, screenings and rooms
* delete reservations and users
* analyse statistics  

Users are notified about upcoming screenings they have reserved seats for.  
Error handling is provided when users want to delete old reservations or rate movies they haven't seen yet.

## Authors
* Michał Dydek
* Mikołaj Gosztyła
* Szymon Paja
* Tomasz Paja
