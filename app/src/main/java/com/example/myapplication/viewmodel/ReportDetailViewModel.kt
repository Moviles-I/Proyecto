package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.memory.MemoryDataRepository
import com.example.myapplication.domain.model.Report
import com.example.myapplication.domain.repository.ReportRepository

// Screen 4: report detail
class ReportDetailViewModel : ViewModel() {

    private val repository: ReportRepository = MemoryDataRepository()

    private val _report = MutableLiveData<Report?>()
    val report: LiveData<Report?> = _report

    fun loadReport(id: Int) {
        _report.value = repository.getReportById(id)
    }

    fun deleteReport() {
        val current = _report.value
        if (current != null) {
            repository.deleteReport(current.id)
        }
    }
}
