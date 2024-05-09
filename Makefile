.PHONY= build_server build_frontend run run_test stop test_run
.DEFAULT_GOAL := default 
CERT_FILE = ./server/src/main/resources/app.key

default: 
	$(MAKE) build_server
	$(MAKE) build_frontend

build_server: 
	cd ./server && ./gradlew clean build
ifeq ( ,$(wildcard $(CERT_FILE)))
	@echo ">Creating certificates"
	cd ./server/scripts && ./createServerKeys.sh
endif
	docker build -t findfirst-server -f ./docker/server/Dockerfile.buildlocal ./server

build_frontend: 
	docker build -t findfirst-frontend -f ./docker/frontend/Dockerfile ./frontend

run: 
	@echo ">Running frontend and server locally."
	@echo ">>Stop all compose services"
	docker compose down
ifeq ( ,$(wildcard $(CERT_FILE)))
	@echo ">Creating certificates"
	cd ./server/scripts && ./createServerKeys.sh
endif
	@echo ">>>>>>> Mailhog container starting:"
	@echo "=============================================================================="
	docker compose up mail -d

	@echo ">>>>>>> Frontend"
	@echo "=============================================================================="
	cd frontend && pnpm install 
	@echo ">> Starting Frontend"
	cd frontend && pnpm run dev > frontend.log 2>&1 & echo "$$!" > frontend.pid
	@echo "==============================================================================\n"

	@echo ">>>>>>> Backend"
	@echo "=============================================================================="
	@echo ">> Building and running backend"
	cd server && nohup ./gradlew bootrun > application.log 2>&1 & echo "$$!" > backend.pid
	@echo "==============================================================================\n"

	@echo "=============================================================================="
	@echo "WELCOME: navigate to localhost:3000"
	@echo "==============================================================================\n"
	@echo "User: jsmith"
	@echo "Password: test\n"
	@echo "==============================================================================\n"

clean: 
	cd server; ./gradlew clean; 
	cd client; npm ci

stop: 
	@echo "Stopping all started processes on host."
	docker compose down
ifneq (,$(wildcard backend.pid)) 
	kill $(file < backend.pid)
	rm -f backend.pid
endif

	@-kill -9 $$(pgrep node) $$(pgrep "next-server") $$(pgrep -f "next dev")

ifneq (,$(wildcard frontend.pid)) 
	kill $(file < frontend.pid)
	rm -f frontend.pid
endif