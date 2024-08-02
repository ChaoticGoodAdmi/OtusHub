package ru.ushakov.otushub.health.aspect

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.ushakov.otushub.health.service.HealthCheckService
import javax.naming.ServiceUnavailableException

private const val SERVICE_AVAILABILITY_CHECK = "service"
private const val DATABASE_AVAILABILITY_CHECK = "database"
private const val RESOURCES_INSUFFICIENCY_CHECK = "resources"

@Aspect
@Component
class HealthCheckAspect(
    private val healthCheckService: HealthCheckService
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @Pointcut("@within(HealthCheck) || @annotation(HealthCheck)")
    fun healthCheckMethods() {
        //do nothing
    }

    @Before("healthCheckMethods()")
    fun checkHealth(joinPoint: JoinPoint) {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method

        val healthCheck = method.getAnnotation(HealthCheck::class.java)
            ?: joinPoint.target.javaClass.getAnnotation(HealthCheck::class.java)

        if (healthCheck != null) {
            performChecks(healthCheck)
        }
    }

    @Before("healthCheckPointcut(healthCheck)")
    fun performChecks(healthCheck: HealthCheck) {
        val checks = healthCheck.checks
        if (SERVICE_AVAILABILITY_CHECK in checks && !healthCheckService.isServiceAvailable()) {
            val message = "Service is unavailable."
            log.error(message)
            throw ServiceUnavailableException(message)
        }
        if (DATABASE_AVAILABILITY_CHECK in checks && !healthCheckService.isDatabaseAvailable()) {
            val message = "Database is unavailable."
            log.error(message)
            throw ServiceUnavailableException(message)
        }
        if (RESOURCES_INSUFFICIENCY_CHECK in checks && !healthCheckService.hasSufficientResources()) {
            val message = "Insufficient resources."
            log.error(message)
            throw ServiceUnavailableException(message)
        }
    }
}