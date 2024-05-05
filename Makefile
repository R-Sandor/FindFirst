.PHONY= build_server build_frontend run run_test stop test_run
.DEFAULT_GOAL := default 
CERT_FILE = ./server/src/main/resources/app.key

default: 
	$(MAKE) build_server
	$(MAKE) build_frontend

build_server: 
	cd ./server && ./gradlew clean build
	docker build -t findfirst/server -f ./docker/server/Dockerfile ./server

build_frontend: 
	docker build -t findfirst/frontend -f ./docker/frontend/Dockerfile ./frontend

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
	cd frontend && pnpm run dev & echo "$$!" > frontend.pid

	@echo ">Spring Boot Backend"
	cd server && nohup ./gradlew bootrun >application.log 2>&1 & echo "$$!" > backend.pid

clean: 
	cd server; ./gradlew clean; 
	cd client; npm ci

stop: 
	@echo "Stopping all started processes on host."
	docker compose down
ifneq (,$(wildcard backend.pid)) 
	kill $(file < backend.pid)
	rm backend.pid
endif
	@-kill -9 $$(cat frontend.pid) $$(pgrep "next-server") $$(pgrep -f "next dev")
	rm -f frontend.pid