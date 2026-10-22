package com.example.myapplication.domain.repository

interface ReportRepository {
    fun getReports(): List<String>
    fun saveReport(report: String)
}