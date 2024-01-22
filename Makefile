.PHONY= build_server build_client run run_test stop
.DEFAULT_GOAL := default 
CERT_FILE = ./server/src/main/resources/app.key

default: 
	cd server; ./gradlew clean build
	$(MAKE) build_server
	$(MAKE) build_client

build_server: 
	docker build -t findfirst/server -f ./docker/server/Dockerfile ./

build_client: 
	docker build -t findfirst/client -f ./docker/client/Dockerfile ./client

run: 
	@echo ">Running frontend and server locally."
	@echo ">>Stop all compose services"
	docker compose down
ifeq ( ,$(wildcard $(CERT_FILE)))
	@echo ">Creating certificates"
	cd ./conf && ./createServerKeys.sh
endif
	@echo ">>Mailhog container starting:"
	docker compose up mail -d

	@echo ">frontend"
	cd frontend && pnpm install 
	@echo ">>Start frontend"
	cd frontend && pnpm run dev &

	@echo ">Spring Boot Backend"
	cd server && nohup ./gradlew bootrun >application.log 2>&1 & 

clean: 
	cd server; ./gradlew clean; 
	cd client; npm ci

stop: 
	@echo "Brute force Kill All" 
	docker compose down 
	killall node
	killall java
