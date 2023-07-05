# Visitor Entry Pass

This repository contains the backend implementation of the Visitor Entry Pass project. The project is designed to manage visitor entries for different locations, providing functionalities such as registration, visitor lookup, search, and visit record management. It is built using the Spring framework, MongoDB for data storage, GraphQL for efficient data fetching, and REST API for programmatic interactions.

## Frontend Repo

[React JS Code](https://github.com/Suryansh-Sharma/Visitor-Entry-Pass-Frontend-React)

## Features

- **Visitor Registration**: Users can register visitors by providing their contact number, email, name, visitor image, child information (name and class), reason for visit, and location.
- **Visitor Lookup**: The system maintains a record of visitors, so when a visitor's contact number is received, it retrieves their details without the need for re-entry.
- **Visitor Image**: Visitors can upload their images as part of the registration process, which are stored for identification purposes.
- **Search Visitors**: Users can search for visitors based on various criteria, such as contact number, date, name, location, child name, etc.
- **Visit Record**: The system maintains a history of all visits made by each visitor, including the date, time, reason for visit, and location.

## Tech Stack

The project utilizes the following technologies:

- **Spring**: The backend is built using the Spring framework, which provides robust server-side functionality and integration with other components.
- **React**: The frontend is developed using React, a popular JavaScript library for building user interfaces.
- **MongoDB**: MongoDB is used as the database to store visitor and visit record information.
- **Tailwind CSS**: The project employs Tailwind CSS for efficient and responsive styling.
- **GraphQL**: GraphQL is implemented for efficient data fetching and manipulation.
- **REST API**: A RESTful API is available for interacting with the system programmatically.

## Usage

1. Clone the repository: 
2. Install the required dependencies specified in the project's pom.xml file.
3. Configure the MongoDB connection settings in the backend configuration file.
4. Build and run the backend application using your preferred IDE or the provided command-line tools.
5. Verify that the backend server is running correctly by accessing the specified API endpoints.
6. For detailed installation instructions and additional configuration options, please refer to the project documentation.



## Note

This README provides an overview of the Visitor Entry Pass project and its features. For detailed installation instructions and setup, please refer to the project documentation.

## Contributors

- [Suryansh Sharma](https://github.com/Suryansh-Sharma) - Project Lead

Feel free to contribute to the project by opening issues or submitting pull requests.

## License

The project is licensed under the [MIT License](https://opensource.org/licenses/MIT).
