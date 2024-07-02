package ru.ushakov.otushub.health.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.lang.management.ManagementFactory
import java.lang.management.OperatingSystemMXBean
import java.net.HttpURLConnection
import java.net.URL

@Component
class SimpleHealthCheckService(
    private val jdbcTemplate: JdbcTemplate,
    private val environment: Environment
) : HealthCheckService {

    @Value("\${healthcheck.cpuThreshold}")
    private lateinit var cpuThreshold: String

    @Value("\${healthcheck.memoryThreshold}")
    private lateinit var memoryThreshold: String

    override fun isServiceAvailable(): Boolean {
        return try {
            val url = environment.getProperty("service.ping.url") ?: return false
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            val responseCode = connection.responseCode
            connection.disconnect()
            responseCode == 200
        } catch (e: Exception) {
            false
        }
    }

    override fun isDatabaseAvailable(): Boolean {
        return try {
            jdbcTemplate.queryForObject("SELECT 1", Int::class.java) != null
        } catch (e: Exception) {
            false
        }
    }

    override fun hasSufficientResources(): Boolean {
        val osBean = ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean
        val cpuLoad = osBean.systemLoadAverage
        val freeMemory = Runtime.getRuntime().freeMemory()
        val totalMemory = Runtime.getRuntime().totalMemory()
        val memoryUsage = (totalMemory - freeMemory).toDouble() / totalMemory

        return cpuLoad < cpuThreshold.toDouble() && memoryUsage < memoryThreshold.toDouble()
    }
}