package com.example.myapplication.data.memory

import com.example.myapplication.domain.repository.ReportRepository

class MemoryDataRepository : ReportRepository {

    private val memoryList = mutableListOf<String>()

    override fun getReports(): List<String> {
        return memoryList
    }

    override fun saveReport(report: String) {
        memoryList.add(report)
    }
}