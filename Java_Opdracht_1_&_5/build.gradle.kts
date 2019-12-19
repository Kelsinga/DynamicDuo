plugins {
    `java-library`
    jacoco
}

repositories {
    jcenter()
}

dependencies {

    compile  (group= "org.seleniumhq.selenium", name = "selenium-java", version =  "3.+" )
    compile  (group= "org.seleniumhq.selenium", name = "selenium-api", version =  "3.+" )


    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api("org.apache.commons:commons-math3:3.6.1")


    // Use JUnit Jupiter API for testing.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testImplementation ("org.junit.jupiter:junit-jupiter-params:5.5.2")

    // Use JUnit Jupiter Engine for testing.
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")

}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
    systemProperty("browser", System.getProperty("browser"));
    systemProperty("version", System.getProperty("version"));
    systemProperty("platform", System.getProperty("platform"));
}
