# Backend Project

## About the Project

The purpose of this project is to provide backend services. Users can sign up, log in as a registered user to obtain a token, and perform specific actions based on their roles. This project follows a **_microservices architecture_**, where each microservice is responsible for specific tasks. Additionally, the project has been containerized using **_Docker_** to facilitate easier deployment of the application.

**The Architecture of Backend Project:**

![Project](https://github.com/oguzhanyildiz22/BackendProject/assets/110741518/92d624a6-6b81-4d26-a3e9-b335985abf01)


## Getting Started

### Prerequisites
* Ensure that JDK 17 is installed. 
* Ensure that PostgreSQL is installed on your computer.
* Ensure that Maven is installed and being used.
* Ensure that Docker Hub is installed on your computer.

### Installation 1
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

### Installation 2
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
 - **LinkedIn**: [Oğuzhan Yıldız](https://www.linkedin.com/in/o%C4%9Fuzhan-yildiz-9b690624b/)

 Don't hesitate to reach out to me. We look forward to hearing from you!
