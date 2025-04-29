import {
  DockerComposeEnvironment,
  StartedDockerComposeEnvironment,
  Wait,
} from "testcontainers";

const composeFilePath = "../";
const composeFile = "docker-compose.yml";

export let composeEnv: StartedDockerComposeEnvironment;

export default async function setup() {
  console.log("spinning up docker");
  composeEnv = await new DockerComposeEnvironment(composeFilePath, composeFile)
    .withWaitStrategy(
      "server-1",
      Wait.forLogMessage("Started FindFirstApplication"),
    )
    .withWaitStrategy("frontend-1", Wait.forLogMessage("Ready in"))
    .withWaitStrategy("screenshot-1", Wait.forListeningPorts())
    // .withStartupTimeout(120_000) // Extend timeout to 120 seconds
    .up();
}
