package ch.opendata.energy;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.SocketUtils;

import java.time.Duration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = MeterApplication.class, webEnvironment = RANDOM_PORT, properties = {"management.server.port=${test.port}"})
@ActiveProfiles("test")
public class ActuatorTests {
  @LocalServerPort
  protected int port = 0;

  private static int managementPort;
  protected WebTestClient webClient;
  protected String baseUri;

  @BeforeAll
  public static void beforeClass() {
    managementPort = SocketUtils.findAvailableTcpPort();
    System.setProperty("test.port", String.valueOf(managementPort));
  }

  @AfterAll
  public static void afterClass() {
    System.clearProperty("test.port");
  }

  @BeforeEach
  public void setup(ApplicationContext context) {
    baseUri = "http://localhost:" + port;
    this.webClient = WebTestClient.bindToServer().responseTimeout(Duration.ofSeconds(30)).baseUrl(baseUri).build();

  }

  @Test
  public void actuatorHealth() {
    webClient.get().uri("http://localhost:" + managementPort + "/actuator/health").exchange().expectStatus()
      .isOk();
  }

  @Test
  public void actuatorInfo() {
    webClient.get().uri("http://localhost:" + managementPort + "/actuator/info").exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.app.java.target").isEqualTo("1.8")
      .jsonPath("$.git.commit.time").isNotEmpty()
      .jsonPath("$.git.commit.id").isNotEmpty()
      .jsonPath("$.build.version").isNotEmpty()
      .jsonPath("$.build.artifact").isNotEmpty()
      .jsonPath("$.build.name").isNotEmpty()
      .jsonPath("$.build.time").isNotEmpty();
  }
}
