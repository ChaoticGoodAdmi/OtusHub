package ru.ushakov.otushub.health.service

interface HealthCheckService {
    fun isServiceAvailable(): Boolean
    fun isDatabaseAvailable(): Boolean
    fun hasSufficientResources(): Boolean
}