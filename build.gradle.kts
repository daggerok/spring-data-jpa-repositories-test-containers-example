plugins {
  idea
  kotlin("jvm") version "1.3.71"
  kotlin("plugin.jpa") version "1.3.71"
  kotlin("plugin.spring") version "1.3.71"
  kotlin("plugin.allopen") version "1.3.71"
  id("org.springframework.boot") version "2.2.6.RELEASE"
  id("io.spring.dependency-management") version "1.0.9.RELEASE"
  id("com.github.ben-manes.versions") version "0.28.0"
  id("org.ajoberstar.reckon") version "0.12.0"
}

val junit4Version = "4.13"
val assertjVersion = "3.15.0"
val coroutinesVersion = "1.3.5"
val springCloudVersion = "2.2.2.RELEASE"
val junitJupiterVersion = "5.6.1"
val gradleWrapperVersion = "6.3"
val testContainersVersion = "1.13.0"
val javaVersion = JavaVersion.VERSION_1_8

group = "daggerok"
java.sourceCompatibility = javaVersion

sourceSets {
  main {
    java.srcDir("src/main/kotlin")
  }
  test {
    java.srcDir("src/test/kotlin")
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(enforcedPlatform("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"))
  // implementation(kotlin("stdlib"))
  // implementation(kotlin("reflect"))
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-data-rest")
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.springframework.data:spring-data-rest-hal-browser")

  runtimeOnly("org.postgresql:postgresql")

  testImplementation("org.springframework.boot:spring-boot-starter-test")/* {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
  }*/
  // testImplementation("org.springframework.cloud:spring-cloud-starter:$springCloudVersion")
  testImplementation(enforcedPlatform("org.testcontainers:testcontainers-bom:$testContainersVersion"))
  testImplementation("org.testcontainers:testcontainers")
  testImplementation("org.testcontainers:junit-jupiter")
  testImplementation("org.testcontainers:postgresql")

  testImplementation("org.assertj:assertj-core:$assertjVersion")
  testImplementation(enforcedPlatform("org.junit:junit-bom:$junitJupiterVersion"))
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-params")
  testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation("junit:junit:$junit4Version")
}

allOpen {
  annotation("javax.persistence.Entity")
  annotation("javax.persistence.Embeddable")
  annotation("javax.persistence.MappedSuperclass")
}

tasks {
  withType<Test> {
    useJUnitPlatform()
  }

  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=strict")
      jvmTarget = "$javaVersion"
    }
  }

  withType<Test> {
    // useJUnitPlatform {
    //   includeEngines("junit-jupiter", "junit-vintage")
    // }
    useJUnit()
    useJUnitPlatform()
    // systemProperties["sleep"] = System.getProperty("sleep") ?: "10"
    testLogging {
      showCauses = true
      showExceptions = true
      showStackTraces = true
      showStandardStreams = true
      events(
          org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
          org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
          org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
      )
    }
  }
  withType(Wrapper::class.java) {
    gradleVersion = gradleWrapperVersion
    distributionType = Wrapper.DistributionType.BIN
  }
}

idea {
  module.iml {
    beforeMerged(Action<org.gradle.plugins.ide.idea.model.Module> {
      dependencies.clear()
    })
  }
}

defaultTasks("clean", "build")

reckon {
  scopeFromProp()
  // stageFromProp()
  snapshotFromProp()
}

tasks {
  register("version") {
    println(project.version.toString())
  }
  // register("status") {
  //   doLast {
  //     val status = grgit.status()
  //     status?.let { s ->
  //       println("workspace is clean: ${s.isClean}")
  //       if (!s.isClean) {
  //         if (s.unstaged.allChanges.isNotEmpty()) {
  //           println("""all unstaged changes: ${s.unstaged.allChanges.joinToString(separator = "") { i -> "\n - $i" }}""")
  //         }
  //       }
  //     }
  //   }
  // }
}
