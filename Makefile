.PHONY= build_server build_client
.DEFAULT_GOAL := default 

default: 
	cd server; ./gradlew clean build
	$(MAKE) build_server
	$(MAKE) build_client

build_server: 
	docker build -t findfirst/server -f ./docker/server/Dockerfile ./

build_client: 
	docker build -t findfirst/client -f ./docker/client/Dockerfile ./client

clean: 
	cd server; ./gradlew clean; 
	cd client; npm ci
