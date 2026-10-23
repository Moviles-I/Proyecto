package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.Report

interface ReportRepository {
    fun getReports(): List<Report>
    fun getReportById(id: Int): Report?
    fun saveReport(report: Report)
    fun updateReport(report: Report)
    fun deleteReport(id: Int)
}