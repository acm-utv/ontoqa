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


root(INFO, ["CONSOLE"])

logger("ch.qos.logback", OFF)