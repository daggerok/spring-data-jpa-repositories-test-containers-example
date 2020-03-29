package daggerok.springdatajparepositoriestestcontainersexample

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer

// data class PgContainer(val image: String) : PostgreSQLContainer<PgContainer>(image)
class PgContainer : PostgreSQLContainer<PgContainer> {
  init {
    addFixedExposedPort(5432, 5432)
  }
  constructor(): super()
  constructor(image: String): super(image)
}

data class Kontainer(val imageName: String = "postgres:alpine",
                     val port: Int = 5432)
  : GenericContainer<Kontainer>(imageName) {

  init {
    addFixedExposedPort(port, port)
    // withExposedPorts(port)
    // watings, health checks
  }
}
