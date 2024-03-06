# Backend Project

## About the Project

The purpose of this project is to provide backend services. Users can sign up, log in as a registered user to obtain a token, and perform specific actions based on their roles. This project follows a **_microservices architecture_**, where each microservice is responsible for specific tasks. Additionally, the project has been containerized using **_Docker_** to facilitate easier deployment of the application.

### The Architecture of Backend Project:

![Project](https://github.com/oguzhanyildiz22/BackendProject/assets/110741518/92d624a6-6b81-4d26-a3e9-b335985abf01)

### Features of microservices: 
In this project, there are three microservices; 

1. TokenService 
2. UserService
3. DefectService
Each microservice has methods to perform certain operations. Now, let's examine each microservice separately.

**1. TokenService:** </br>
This microservice allows the user to register using the 'register' method and obtain a JWT token using the 'login' method for the registered user. This token is required to access other microservices because it contains information about the user's role. The user can register with one of three roles in this microservice: 'ADMIN', 'OPERATOR', or 'TEAM_LEADER'. Additionally, this microservice has a method called 'GetRole' which returns the role information in response to a request with a JWT token from other microservices. Spring Security is used in TokenService, and its logic is as follows:</br></br>

![image](https://github.com/oguzhanyildiz22/BackendProject/assets/110741518/03efd85b-bc2e-4c97-b3e6-bf3ac12d9a49)

**2. UserService:**</br>
In this microservice, a user with the 'ADMIN' role can perform the following operations: adding, deleting, updating users, and listing registered users. Additionally, there is a terminal listing operation that does not require role authorization.

**3. DefectService:**</br>
To use the operations in this microservice, one must have either the 'OPERATOR' or 'TEAM_LEADER' roles. There are separate operations for each role. A user with the 'OPERATOR' role can add or delete vehicles and add defects to a vehicle. A user with the 'TEAM_LEADER' role can list and sort registered vehicles and defects, and view the image of a specific defect.

### How do the microservices communicate with each other? </br>
Communication between microservices is facilitated through the RestTemplate method. In this project, when a request is made to other microservices using the token obtained from the TokenService, the token is sent back to the TokenService along with the request for role information, ensuring authentication and authorization.
</br></br>
![image](https://github.com/oguzhanyildiz22/BackendProject/assets/110741518/20025ac5-3c0e-4ec4-8717-25bab5f2bbbc)
</br></br>
![image](https://github.com/oguzhanyildiz22/BackendProject/assets/110741518/fcb7daa5-e4a2-4fb6-90b2-62a0533382b0)

## Getting Started

### Prerequisites
* Ensure that JDK 17 is installed. 
* Ensure that PostgreSQL is installed on your computer.
* Ensure that Maven is installed and being used.
* Ensure that Docker Hub is installed on your computer.

### Installation by using your local computer
To get started with the project, follow the steps below: 

1. Clone the project repositories from GitHub by running the following command : 
   ```sh
   git clone https://github.com/oguzhanyildiz22/BackendProject.git 
   ```
2. Make sure you have Java JDK 17 or a newer version installed on your computer.
 
3. Ensure that PostgreSQL is installed on your computer. You can download it from [PostgreSQL official website](https://www.postgresql.org/) and follow the installation instructions for your operating system.  

4. Verify that Maven is installed by running the following command in your terminal: 
   ```sh
   mvn -version
   ```
   If Maven is not installed, you can download it from the [Apache Maven website](https://maven.apache.org/) and follow the installation instructions.

5. Ensure that Docker Hub is installed on your computer. You can download and install Docker Desktop from the [Docker website](https://www.docker.com/products/docker-desktop). 

6. Once all the prerequisites are met, navigate to the project directory: 
   ```sh
   cd ProjectName
   ```
7. Build the project using the following command: 
   ```sh
   mvn clean install
   ```
8. Run the project: 
    ```sh
   mvn spring-boot:run
   ```
   By following these steps, you should be able to successfully install and run the project on your local machine. Feel free to modify the instructions according to your project's specific requirements.

### Installation by using Docker
To get started with the project in a Docker environment, follow the steps below:

1. Clone the project repositories from GitHub: 
  ```sh
   git clone https://github.com/oguzhanyildiz22/BackendProject.git 
   ```
2. Navigate to the project directory: 
   ```sh
   cd ProjectName
   ```
3. pull the images from Docker: 
   ```sh
   docker pull username/repository:tag
   ```
4. run the docker containers: 
   ```sh
   docker-compose up
   ```
   By following these steps, you should be able to successfully run the project on your local machine by using Docker. Feel free to modify the instructions according to your project's specific requirements.
 
 ## Usage
  
  **If you don't use Docker :** 
  
  1. Clone the repositories 
  2. Open these repositories in your IDE
  3. Run the project
  
  **If you use Docker:**
  
  1. Clone the repositories
  2. pull the images from Docker:  
   ```sh
   docker pull username/repository:tag
   ```
  3. Go to the directory where the docker-compose.yml file is located using command prompt
  4. run the docker containers: 
   ```sh
     docker-compose up
   ```
## Contributing

 We welcome contributions to improve Backend Project To contribute, please follow these steps:

 1. Fork the repository and create your branch from `master`.
 2. Clone the forked repository to your local machine.
 3. Install the necessary dependencies.
 4. Make your desired changes and ensure that the code passes any existing tests.
 5. Commit your changes with descriptive commit messages.
 6. Push your changes to your forked repository.
 7. Open a pull request to the `master` branch of the original repository.
 8. Provide a clear description of your changes and why they should be merged.
 9. Be responsive to any feedback or questions during the review process.

 Please note that we have a [Code of Conduct](CODE_OF_CONDUCT.md) in place to ensure a welcoming and inclusive community. By contributing to this project, you agree to abide by its terms.

 If you have any questions or need further assistance, feel free to reach out to us through [contact information](https://www.linkedin.com/in/o%C4%9Fuzhan-yildiz-9b690624b/).

## License

 The Backend Project project is licensed under the License.

 For more information about the license, you can refer to the [LICENSE](LICENSE) file.
 
## Contact

 If you have any questions, suggestions, or feedback regarding the project, feel free to reach out to me. I welcome your contributions and are happy to assist you with any inquiries you may have. You can contact me through the following channels:

 - **Email**: [oguzy2020@gmail.com](mailto:oguzy2020@gmail.com)
 - **GitHub**: [oguzhanyildiz22](https://github.com/oguzhanyildiz22)
 - **LinkedIn**: [Oğuzhan Yıldız](https://www.linkedin.com/in/oğuzhanyildiz)

 Don't hesitate to reach out to me. I look forward to hearing from you!
