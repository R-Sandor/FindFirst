.PHONY= build_server build_screenshot build_frontend run_local run_test stop test_run db
.DEFAULT_GOAL := default 
CERT_FILE = ./server/src/main/resources/app.key
ENV?=dev

default: 
	$(MAKE) build_server
	$(MAKE) build_screenshot
	$(MAKE) build_frontend
	$(MAKE) db

build_server: 
ifeq ( ,$(wildcard $(CERT_FILE)))
	@echo ">Creating certificates"
	cd ./server/scripts && ./createServerKeys.sh
endif
	cd ./server && ./gradlew clean build
	docker build -t findfirst-server -f ./docker/server/Dockerfile.buildlocal ./server

build_screenshot:
	cd ./screenshot && ./gradlew clean build
	docker build -t findfirst-screenshot -f ./docker/screenshot/Dockerfile.buildlocal ./screenshot

build_frontend: 
	docker build -t findfirst-frontend -f ./docker/frontend/Dockerfile --build-arg BUILDENV=$(ENV) ./frontend 

db: 
	docker build -t findfirst-db ./docker/postgres

run: 
	docker compose up

run_local: 
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

	@echo ">>>>>>> Screenshot"
	@echo "=============================================================================="
	@echo ">> Building and running screenshot"
	cd screenshot && nohup ./gradlew bootrun > application.log 2>&1 & echo "$$!" > screenshot.pid
	@echo "==============================================================================\n"

	@echo "=============================================================================="
	@echo "WELCOME: navigate to localhost:3000"
	@echo "==============================================================================\n"
	@echo "User: jsmith"
	@echo "Password: test\n"
	@echo "==============================================================================\n"

clean: 
	cd server; ./gradlew clean; 
	cd screenshot; ./gradlew clean; 
	cd client; pnpm install --frozen-lockfile;

stop: 
	@echo "Stopping all started processes on host."
	docker compose down --remove-orphans
ifneq (,$(wildcard backend.pid)) 
	kill $(file < backend.pid)
	rm -f backend.pid
endif

ifneq (,$(wildcard screenshot.pid))
	kill $(file < screenshot.pid)
	rm -f screenshot.pid
endif

	@-kill -9 $$(pgrep node) $$(pgrep "next-server") $$(pgrep -f "next dev")

ifneq (,$(wildcard frontend.pid)) 
	kill $(file < frontend.pid)
	rm -f frontend.pid
endif
