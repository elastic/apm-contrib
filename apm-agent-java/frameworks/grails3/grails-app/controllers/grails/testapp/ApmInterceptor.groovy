package grails.testapp

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