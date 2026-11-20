package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.model.Report
import com.example.myapplication.domain.repository.ReportRepository

class ReportDetailViewModel(
    private val repository: ReportRepository
): ViewModel() {
    fun getReportById(id: Int): Report?{
        return repository.getReportById(id)
    }

    fun saveReport(report: Report){
        repository.saveReport(report)
    }

    fun updateReport(report: Report){
        repository.updateReport(report)
    }

    fun removeReport(id: Int){
        repository.deleteReport(id)
    }
}