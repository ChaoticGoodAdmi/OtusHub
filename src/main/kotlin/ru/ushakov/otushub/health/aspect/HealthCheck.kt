package ru.ushakov.otushub.health.aspect

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class HealthCheck(val checks: Array<String> = ["service", "database", "resources"])