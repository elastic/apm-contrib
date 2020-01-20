package grails.testapp

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class ElasticApmInterceptorSpec extends Specification implements InterceptorUnitTest<ElasticApmInterceptor> {

    def setup() {
    }

    def cleanup() {

    }

    void "Test elasticApm interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"elasticApm")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
