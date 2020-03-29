package daggerok.springdatajparepositoriestestcontainersexample

import org.apache.logging.log4j.LogManager
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

val log = LogManager.getLogger()

@Entity
@Table(name = "users")
data class User(
    var name: String? = null,
    @Id @GeneratedValue var id: Long? = null
)

interface Users : JpaRepository<User, Long>

@Component
@Transactional
class TestDataRunner(private val users: Users) : ApplicationListener<ApplicationReadyEvent> {
  override fun onApplicationEvent(event: ApplicationReadyEvent) {
    listOf("Max", "Bax", "Fax")
        .map { User(it) }
        .forEach { users.save(it) }
    users.findAll()
        .forEach { log.info(it) }
  }
}

@SpringBootApplication
class SpringDataJpaRepositoriesTestContainersExampleApplication

fun main(args: Array<String>) {
  runApplication<SpringDataJpaRepositoriesTestContainersExampleApplication>(*args)
}
