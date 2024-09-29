# Getting started with FindFirst

Get acquainted with the UI. There are two ways to get started with FindFirst:

- Visit the live site: [FindFirst](https://findfirst.dev)
- `docker-compose up` on this directory and sign in with the test user `jsmith:test`.

## Required Software For Development

- Docker
- Docker Compose
- Java 17
  - JDK & JRE
- Node 20
- OpenSSL

## Building & Running locally

There are handful of ways to develop this application and the configuration
has been maintained in a way where most of the stack can be run on local
host and docker without any problems.

### Redeploying everything in docker

- `make` and all the containers are rebuilt.
- `docker compose down --remove-orphans`
- `docker compose up`
- Open browser navigate to localhost:3000
  - Create a user or use the test account:
    - Username: jsmith
    - password: test

### Running Most the stack on Host

- `docker compose up db mail`
- `cd frontend; pnpm run dev`
  - All changes to the frontend code are hot reloaded.
  - Now user your favorite IDE!
- Open a new terminal tab/window.
- `cd server`
- `./scripts/createServerKeys.sh`
  - Requires OpenSSL installed on machine.
- `./gradlew build bootRun`
  - Use any IDE that you like, VSCode
  - Neovim
  - etc.
  - The project does hot reload well, if the
    IDE your using supports it with the JDTLS.
- Open browser navigate to localhost:3000
  - Create a user or use the test account:
    - Username: jsmith
    - password: test
