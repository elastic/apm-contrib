
## Tomcat

### All-In-One copy & paste configuration

Agent `capture_jmx_metrics` value to use:
```
object_name[Catalina:type=ThreadPool,name=*] attribute[connectionCount] attribute[currentThreadCount] attribute[currentThreadsBusy] attribute[maxConnections] attribute[maxThreads] attribute[poolSize], object_name[Catalina:type=Executor,name=*] attribute[activeCount] attribute[maxThreads] attribute[poolSize], object_name[Catalina:type=GlobalRequestProcessor,name=*] attribute[bytesSent] attribute[bytesReceived] attribute[errorCount] attribute[maxTime] attribute[requestCount] object_name[Catalina:type=Manager,context=*,host=*] attribute[activeSessions] attribute[sessionCounter] attribute[expiredSessions]
```

### Thread pools

`Executor` thread pools are only available when explicitly configured, they aren't enabled by default.

Attributes availability depends on Tomcat version and configuration.

```
Catalina:type=ThreadPool,name=*
- connectionCount
- currentThreadCount
- currentThreadsBusy
- maxConnections
- maxThreads
- poolSize

Catalina:type=Executor,name=*
- activeCount
- maxThreads
- poolSize
```

Agent `capture_jmx_metrics` value to use:
```
object_name[Catalina:type=ThreadPool,name=*] attribute[connectionCount] attribute[currentThreadCount] attribute[currentThreadsBusy] attribute[maxConnections] attribute[maxThreads] attribute[poolSize], object_name[Catalina:type=Executor,name=*] attribute[activeCount] attribute[maxThreads] attribute[poolSize]
```

### Request throughput

```
Catalina:type=GlobalRequestProcessor,name=*
- bytesSent
- bytesReceived
- errorCount
- maxTime
- requestCount
```

Agent `capture_jmx_metrics` value to use:
```
object_name[Catalina:type=GlobalRequestProcessor,name=*] attribute[bytesSent] attribute[bytesReceived] attribute[errorCount] attribute[maxTime] attribute[requestCount]
```

### Sessions

```
Catalina:type=Manager,context=*,host=*
- activeSessions
- sessionCounter
- expiredSessions
```

Agent `capture_jmx_metrics` value to use:
```
object_name[Catalina:type=Manager,context=*,host=*] attribute[activeSessions] attribute[sessionCounter] attribute[expiredSessions]
```

## IBM Websphere

TODO

## Oracle Weblogic

TODO

## Database connection pools
### HikariCP

Agent `capture_jmx_metrics` value to use:
```
object_name[com.zaxxer.hikari:type=Pool (poolName)] attribute[IdleConnections] attribute[ActiveConnections] attribute[TotalConnections] attribute[ThreadsAwaitingConnection]
```
where `poolName` is a name you can set on the HikariConfig using setPoolName("foo"), or the auto-generated name (e.g. HikariPool-1).
