# User Microservice


Three types of calls:

1. All users
	- http://localhost:8091/users/

2. A specific user
	- http://localhost:8091/users/20001/
	- http://localhost:8091/users/20002/

3. Orders of a specific user (making REST calls to [order](https://github.com/fsciusa/ms-order) and [product](https://github.com/fsciusa/ms-product) microservices)
	- http://localhost:8091/users/20001/orders/
	- http://localhost:8091/users/20002/orders/

