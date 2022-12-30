.PHONY= build_server build_client
.DEFAULT_GOAL := default 

default: 
	cd server; ./gradlew clean build
	cp server/build/libs/bookmarkit-1.0.0.jar docker/server/
	$(MAKE) build_server

build_server: 
	docker build -t findfirst/server docker/server

build_client: 
	docker build -t findfirst/client -f ./docker/client/Dockerfile ./client
