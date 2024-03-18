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

dependencies {
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//	implementation("org.springframework.boot:spring-boot-starter-data-redis")
		implementation("org.springframework.boot:spring-boot-starter-security")
		implementation("org.springframework.boot:spring-boot-starter-web")
		implementation("org.liquibase:liquibase-core")

		compileOnly ("org.projectlombok:lombok:1.18.30")
		annotationProcessor ("org.projectlombok:lombok:1.18.30")

		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("org.springframework.security:spring-security-test")

		testCompileOnly ("org.projectlombok:lombok:1.18.30")
		testAnnotationProcessor ("org.projectlombok:lombok:1.18.30")

}

tasks.withType<Test> {
		useJUnitPlatform()
}


openApiGenerate {
		generatorName.set("spring")
		inputSpec.set("$projectDir/src/main/resources/static/openapi/server/openapi.yaml")
		templateDir.set("$projectDir/src/main/resources/static/openapi/server/templates") // непонятно, зачем. Туда вроде бы ничего не попадает
		outputDir.set("${buildDir}/generated/server") // buildDir deprecated, но {layout.buildDirectory} не работает
		apiPackage.set("org.zheleznikov.generated.api") // непонятно, какие надо указывать
		modelPackage.set("org.zheleznikov.generated.model") // непонятно, какие надо указывать org.zheleznikov.generated.model

		configOptions.putAll(
						mapOf(
										Pair("library", "spring-boot"),
										Pair("useSpringBoot3", "true"),
										Pair("datesLibrary", "java8"),
										Pair("java8", "true"),
										Pair("delegatePattern", "true"),
										Pair("useBeanValidation", "true"),
										Pair("useTags", "true"),
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
		build {
				dependsOn(openApiGenerate)
		}
}

/*

tasks.register('TASK_NAME', GenerateTask) {
		generatorName = "spring"
		inputSpec = "$projectDir/src/main/resources/static/openapi/server/YOUR_SPECIFICATION_FILE.yaml".toString()
		templateDir = "$projectDir/src/main/resources/static/openapi/server/templates"
		outputDir = "$buildDir/generated/server".toString()
		apiPackage = "com.dummy.foo.generated.any.api"
		modelPackage = "com.dummy.foo.generated.any.model"

		configOptions = [
				library          : "spring-boot",
				useSpringBoot3   : "true",
				dateLibrary      : "java8",
				java8            : "true",
				delegatePattern  : "true",
				useBeanValidation: "true",
				useTags          : "true"
		]

		typeMappings = [
						"local-date-time": "java.time.LocalDateTime",
						"legacy-date"    : "java.util.Date",
						"java-period"    : "java.time.Period",
		]

}

 */






