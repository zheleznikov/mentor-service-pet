plugins {
		java
		id("org.springframework.boot") version "3.2.3"
		id("io.spring.dependency-management") version "1.1.4"
		id("org.openapi.generator") version "7.4.0"
}

group = "org.zheleznikov"
version = "0.0.1-SNAPSHOT"

java {
		sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
		mavenCentral()
}

springBoot {
		mainClass.set("org.zheleznikov.authservice.AuthServiceApplication")
}

dependencies {
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		runtimeOnly("org.postgresql:postgresql")
		implementation("org.springframework.boot:spring-boot-starter-data-redis")
		implementation("org.springframework.boot:spring-boot-starter-security")
		implementation("org.springframework.boot:spring-boot-starter-web")
		implementation("org.liquibase:liquibase-core")

		compileOnly("org.projectlombok:lombok:1.18.30")
		annotationProcessor("org.projectlombok:lombok:1.18.30")

		testImplementation("org.springframework.boot:spring-boot-starter-test")

		testImplementation("org.springframework.security:spring-security-test")

		testCompileOnly("org.projectlombok:lombok:1.18.30")
		testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

		implementation("org.springframework.boot:spring-boot-starter-mail")

		// https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt
		implementation("io.jsonwebtoken:jjwt:0.2")


		/*
		Dependencies for OPEN API
		 */

		implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
		implementation("org.openapitools:jackson-databind-nullable:0.2.6")

}

sourceSets.getByName("main").java.srcDir("$buildDir/generated/server/src/main/java")
sourceSets.getByName("test").java.srcDir("$buildDir/generated/server/src/test/java")

//sourceSets.getByName("main").java.srcDirs(File(buildDir, "generated/server/src/main/java"))
//sourceSets.getByName("test").java.srcDirs(File(buildDir, "generated/server/src/test/java"))


tasks.withType<Test> {
		useJUnitPlatform()
}


openApiGenerate {
		generatorName.set("spring")
		inputSpec.set("$projectDir/src/main/resources/static/openapi/server/openapi.yaml")
		templateDir.set("$projectDir/src/main/resources/static/openapi/server/templates")
		outputDir.set("${buildDir}/generated/server")
		apiPackage.set("org.zheleznikov.generated.api")
		modelPackage.set("org.zheleznikov.generated.model")

		configOptions.putAll(
						mapOf(
										Pair("library", "spring-boot"),
										Pair("useSpringBoot3", "true"),
										Pair("useSwaggerUI", "true"),
										Pair("datesLibrary", "java8"),
										Pair("java8", "true"),
										Pair("delegatePattern", "true"),
										Pair("useBeanValidation", "true"),
										Pair("useTags", "true"),
										Pair("additionalModelTypeAnnotations", "@lombok.experimental.Accessors(chain = true)")
						)

		)
		typeMappings.putAll(
						mapOf(
										Pair("local-date-time", "java.time.LocalDateTime"),
										Pair("legacy-date", "java.util.Date"),
										Pair("java-period", "java.time.Period")
						)
		)
		generateApiTests.set(false)
}

tasks {
		compileJava {
				dependsOn(openApiGenerate)
		}
}






