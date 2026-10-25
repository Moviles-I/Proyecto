package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.memory.MemoryDataRepository
import com.example.myapplication.domain.model.Category
import com.example.myapplication.domain.model.Report
import com.example.myapplication.domain.model.Status
import com.example.myapplication.domain.repository.ReportRepository

// Pantalla 1: lista principal con busqueda y filtros
class ReportListViewModel : ViewModel() {

    private val repository: ReportRepository = MemoryDataRepository()

    private val _reports = MutableLiveData<List<Report>>()
    val reports: LiveData<List<Report>> = _reports

    private var searchText = ""
    private var categoryFilter: Category? = null
    private var statusFilter: Status? = null

    fun loadReports() {
        applyFilters()
    }

    fun setSearch(text: String) {
        searchText = text
        applyFilters()
    }

    // null significa "Todas"
    fun setCategory(category: Category?) {
        categoryFilter = category
        applyFilters()
    }

    // null significa "Todos"
    fun setStatus(status: Status?) {
        statusFilter = status
        applyFilters()
    }

    private fun applyFilters() {
        var list = repository.getReports().toList()

        if (searchText.isNotEmpty()) {
            list = list.filter { it.title.contains(searchText, ignoreCase = true) }
        }
        if (categoryFilter != null) {
            list = list.filter { it.category == categoryFilter }
        }
        if (statusFilter != null) {
            list = list.filter { it.status == statusFilter }
        }

        _reports.value = list
    }
}