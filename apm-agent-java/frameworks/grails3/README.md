# Simple integration between Grails and Elastic APM

## Add dependency to apm-agent-api

Replace `1.12.0` with the current/required agent version.
```
dependencies {
    compile "co.elastic.apm:apm-agent-api:1.12.0"
}
```

## Add Interceptor to application

Create an interceptor in `grails-app/controllers`, for example `ApmInterceptor.groovy`.

```
import co.elastic.apm.api.ElasticApm

class ApmInterceptor {

    ApmInterceptor() {
        matchAll()
    }

    boolean before() {
        ElasticApm.currentTransaction().setName(String.format("%s/%s", controllerName, actionName))
        true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
```

## Compile, package and run the application with agent

For example, when running application with `./gradlew bootRun`, in `build.gradle`:
Replace elastic agent jar path in `build.gradle`, and provide suitable Elastic APM server URL.
```
bootRun {
    jvmArgs = [
            "-javaagent:/path/to/elastic/agent/elastic-apm-agent-1.12.0.jar",
            "-Delastic.apm.server_url=http://localhost:8200",
            "-Delastic.apm.service_name=grails-app"
    ]
}
```

For other deployment options, refert to Elastic APM documentation.
