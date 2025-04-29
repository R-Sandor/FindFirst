import { DockerComposeEnvironment, StartedDockerComposeEnvironment, Wait } from "testcontainers";

const composeFilePath = '../'
const composeFile = 'docker-compose.yml'

export let composeEnv: StartedDockerComposeEnvironment;

export default async function setup() {
 composeEnv = await new DockerComposeEnvironment(composeFilePath, composeFile)
      .withWaitStrategy("server-1", Wait.forLogMessage("Started FindFirstApplication"))
      .withWaitStrategy("frontend-1", Wait.forLogMessage("Ready in"))
      .up()
}
