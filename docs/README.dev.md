# Getting started with FindFirst

Get acquainted with the UI. There are two ways to get started with FindFirst:

- Visit the live site: [FindFirst](https://findfirst.dev)
- `docker-compose up` on this directory and sign in with the test user `jsmith:test`.

## Required Software For Development

- [Docker](https://docs.docker.com/engine/install/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- Java 21
  - JDK & JRE
- Node 22

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

- `docker compose --profile local up`
  - This starts all the services but the backend an frontend.
  - The assumption is this is where majority of the feature are added.
  - If you're working in one of the other services either docker compose down
    that service or spin up all the services you need with `docker compose <sv1> <sv2> <...> up`
- `cd frontend; pnpm run dev`
  - All changes to the frontend code are hot reloaded.
  - Now user your favorite IDE!
- Open a new terminal tab/window.
- `cd server`
- `./gradlew build bootRun`
  - Use any IDE that you like, VSCode
  - Neovim
  - etc.
  - The project does hot reload well, if the
    IDE your using supports it with the JDTLS.
- Create another terminal tab
- `cd screenshot`
- `./gradlew bootRun`
- Open browser navigate to localhost:3000
  - Create a user or use the test account:
    - Username: jsmith
    - password: test

### Partial Host/Docker Compose

- The application supports running the app in a mixed
  environment. For example running everything but
  the backend in docker compose:

```bash
docker compose db frontend mail screenshot
# or the use of profiles.
docker compose --profiles frontend
# The profile hints to the type of work your doing.
# frontend spins everything up but the frontend in docker.
```

Then executing: `cd server && ./gradlew bootRun`

One exception is from the backend in docker compose
communicating with the screenshot service running on
host.

```bash
export SCREENSHOT_SERVICE_URL=http://172.17.0.1:8080
docker compose up mail server db frontend
```

This will allow the backend to reach localhost where the screenshot service is running.
