appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%date{HH:mm:ss.SSS} %-5level %logger %method - %msg%n"
    }
}

appender("FILE", FileAppender) {
    file = "log/ontoqa.log"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "%date{HH:mm:ss.SSS} %-5level %logger - %msg%n"
    }
}

root(INFO, ["CONSOLE"])

logger("com.acmutv.ontoqa", DEBUG)

logger("ch.qos.logback", OFF)
logger("javax", WARN)
logger("org.apache.catalina", WARN)
logger("org.apache.coyote", WARN)
logger("org.apache.jena", WARN)
logger("org.apache.tomcat", WARN)
logger("org.hibernate", WARN)
logger("org.springframework", WARN)
logger("org.eclipse.rdf4j", WARN)
logger("sun.rmi", OFF)