package daggerok.springdatajparepositoriestestcontainersexample

import org.apache.logging.log4j.LogManager
import org.assertj.core.api.Assertions
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
@ContextConfiguration(initializers = [SecondTest.Companion.Initializer::class])
internal class SecondTest(@Autowired val users: Users) {

  companion object {
    internal val log = LogManager.getLogger()

    @Container
    internal val pg = PgContainer(image = "postgres:11-alpine")
        .withExposedPorts(6543)
        .withDatabaseName("second-test")
        .withUsername("second-test")
        .withPassword("second-test")

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
      override fun initialize(context: ConfigurableApplicationContext) {
        val pairs = arrayOf(
            "spring.datasource.url=${pg.jdbcUrl}",
            "spring.datasource.username=${pg.username}",
            "spring.datasource.password=${pg.password}"
        )
        TestPropertyValues.of(*pairs).applyTo(context.environment)
      }
    }
  }

  @Test
  internal fun `find all again and assert`() = users.findAll().forEach {
    OneMoreTest.log.info("also not null: {}", it)
    Assertions.assertThat(it).isNotNull
  }
}
