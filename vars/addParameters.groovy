#!/usr/bin/env groovy

def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    def param = config.parameters
    def logRotator = config.rotator
    def authMatrix = config.authMatrix
    def concurrent = config.concurrent
    if (concurrent == null) {
        concurrent = true
    }
    def props = []
    if (!concurrent) {
        props << disableConcurrentBuilds()
    }
    props << buildDiscarder(logRotator)
    props << parameters(param)

    if(authMatrix){
        props << authMatrix
    }
    properties(props)
}