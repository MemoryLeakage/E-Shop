all: build-frontend build-backend start-deployment

build: build-frontend build-backend

start: start-deployment

build-frontend:
	cd ./e-shop-frontend && npm install && npm run build

build-backend:
	cd ./e-shop-backend && mvn clean install

start-deployment:
	docker-compose up
