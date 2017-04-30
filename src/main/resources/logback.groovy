appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%date{HH:mm:ss.SSS} %-5level %logger %method - %msg%n"
    }
}

appender("FILE", FileAppender) {
    file = "log/testFile.log"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "%date{HH:mm:ss.SSS} %-5level %logger - %msg%n"
    }
}

root(ALL, ["CONSOLE"])

logger("com.acmutv.ontoqa", ALL, ["CONSOLE"],false)

logger("ch.qos.logback", OFF)
logger("org.springframework", WARN)
logger("org.apache.tomcat", WARN)
logger("org.apache.catalina", WARN)
logger("org.apache.coyote", WARN)
logger("org.apache.jena", WARN)
logger("org.hibernate", WARN)
logger("javax", WARN)
logger("org.eclipse.rdf4j", WARN)