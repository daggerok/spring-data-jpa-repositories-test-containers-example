package daggerok.springdatajparepositoriestestcontainersexample

import org.apache.logging.log4j.LogManager
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
@ContextConfiguration(initializers = [OneMoreTest.Companion.Initializer::class])
internal class OneMoreTest(@Autowired val users: Users) {

  companion object {
    internal val log = LogManager.getLogger()

    @Container
    internal val pg = PgContainer("healthcheck/postgres:alpine")
        .withExposedPorts(6543)
        .withDatabaseName("healthcheck")
        .withUsername("healthcheck")
        .withPassword("healthcheck")

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
      override fun initialize(context: ConfigurableApplicationContext) {
        val pairs = arrayOf(
            "spring.datasource.url=${pg.jdbcUrl}",
            "spring.datasource.username=healthcheck",
            "spring.datasource.password=healthcheck"
        )
        TestPropertyValues.of(*pairs).applyTo(context.environment)
      }
    }
  }

  @Test
  internal fun `find all one more times and assert`() = users.findAll().forEach {
    log.info("again not null: {}", it)
    assertThat(it).isNotNull
  }
}
