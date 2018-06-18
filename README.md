# User Microservice


__To compile and run:__

$ mvn spring-boot:run


__Possible calls:__

1. GET All users
	- http://localhost:8091/users/

2. GET A specific user
	- http://localhost:8091/users/20001/
	- http://localhost:8091/users/20002/

3. GET Orders of a specific user (making REST calls to [order](https://github.com/fsciusa/ms-order) and [product](https://github.com/fsciusa/ms-product) microservices)
	- http://localhost:8091/users/20001/orders/
	- http://localhost:8091/users/20002/orders/

4. POST a user (create)
	- http://localhost:8091/users/

5. DELETE a user
	- http://localhost:8091/users/{id}

6. PUT a user (update)
	- http://localhost:8091/users/{id}