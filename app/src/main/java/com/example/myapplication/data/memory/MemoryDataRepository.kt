package com.example.myapplication.data.memory

import com.example.myapplication.domain.model.Report
import com.example.myapplication.domain.repository.ReportRepository

class MemoryDataRepository : ReportRepository {

    // The list is in companion so all screens see the same reports
    companion object {
        private val memoryList = mutableListOf<Report>()
        private var nextId = 1
    }

    override fun getReports(): List<Report> {
        return memoryList
    }

    override fun getReportById(id: Int): Report? {
        return memoryList.find { it.id == id }
    }

    override fun saveReport(report: Report) {
        val newReport = report.copy(id = nextId)
        nextId++
        memoryList.add(newReport)
    }

    override fun updateReport(report: Report) {
        val index = memoryList.indexOfFirst { it.id == report.id }
        if (index != -1) {
            memoryList[index] = report
        }
    }

    override fun deleteReport(id: Int) {
        memoryList.removeAll { it.id == id }
    }
}
