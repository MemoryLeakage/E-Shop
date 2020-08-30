all: build start-deployment

build: build-frontend build-backend build-user-listener

start: start-deployment

build-frontend:
	cd ./e-shop-frontend && npm install && npm run build

build-user-listener:
	cd ./keycloak/users-event-listener && mvn clean install

build-backend:
	cd ./e-shop-backend && mvn clean install

start-deployment:
	docker-compose up
