package daggerok.springdatajparepositoriestestcontainersexample

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer

// data class PgContainer(val image: String) : PostgreSQLContainer<PgContainer>(image)
class PgContainer : PostgreSQLContainer<PgContainer> {
  constructor(): super()
  constructor(image: String): super(image)
}

data class Kontainer(val imageName: String = "postgres:alpine",
                     val port: Int = 6543)
  : GenericContainer<Kontainer>(imageName) {

  init {
    withExposedPorts(port)
    // watings, health checks
  }
}
