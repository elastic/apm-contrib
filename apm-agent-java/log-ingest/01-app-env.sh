# defaults taken from https://github.com/spring-projects/spring-boot/blob/v3.0.1/spring-boot-project/spring-boot/src/main/resources/org/springframework/boot/logging/logback/defaults.xml
# default format for console prefixed with correlation IDs + some colors for easy visualization
export CONSOLE_LOG_PATTERN="%d{yyyy-MM-dd'T'HH:mm:ss.SSSXXX} %5p \${PID:- } --- [%15.15t] (%-40.40logger{39}: %m --- %clr(%X{trace.id}){yellow},%clr(%X{transaction.id}){magenta},%clr(%X{error.id}){red}%n%wEx"
# default format in file prefixed with correlation IDs
export FILE_LOG_PATTERN="%d{yyyy-MM-dd''T''HH:mm:ss.SSSXXX} %5p \${PID:- } --- [%t] (%-40.40logger{39}: %m --- %X{trace.id},%X{transaction.id},%X{error.id}%n%wEx"

# make the app also write logs to file
export LOGGING_FILE_NAME=/var/log/app/app.log
