# Simple integration between Grails and Elastic APM

This Grails 3 application integrates with Elastic APM through the usage of a [Grails Interceptor](http://docs.grails.org/3.3.11/guide/theWebLayer.html#interceptors) and [Elastic APM Agent API](https://www.elastic.co/guide/en/apm/agent/java/current/public-api.html).

## How to build & run

Replace path to elastic agent jar in `build.gradle`, and provide suitable Elastic APM server URL.
```
bootRun {
    jvmArgs = [
            "-javaagent:/path/to/elastic/agent/elastic-apm-agent-1.12.0.jar",
            "-Dspring.output.ansi.enabled=always",
            "-Delastic.apm.server_url=http://localhost:8200",
            "-Delastic.apm.service_name=grails-app"
    ]
    // ...
}
```

Start the application
`./gradlew bootRun`

## How to do the same on another application

1. Add dependency to java agent API (in `build.gradle`)

    ```
    dependencies {
        compile "co.elastic.apm:apm-agent-api:1.12.0"
    }
    ```

1. Add interceptor to intercept incoming requests

    See `ApmInterceptor.groovy` for details.

1. Deploy your application with Elastic APM Agent

    Refer to Elastic agent APM for details.
