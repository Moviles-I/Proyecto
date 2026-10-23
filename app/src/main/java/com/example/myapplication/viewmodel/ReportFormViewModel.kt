package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.memory.MemoryDataRepository
import com.example.myapplication.domain.model.Category
import com.example.myapplication.domain.model.Report
import com.example.myapplication.domain.repository.ReportRepository
import com.example.myapplication.util.Utils

// Pantalla 2: formulario para crear o editar un reporte
class ReportFormViewModel : ViewModel() {

    private val repository: ReportRepository = MemoryDataRepository()

    var photoUri: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    // si es null estamos creando, si no estamos editando
    var editingReport: Report? = null

    fun loadReport(id: Int) {
        val report = repository.getReportById(id)
        if (report != null) {
            editingReport = report
            photoUri = report.photoUri
            latitude = report.latitude
            longitude = report.longitude
        }
    }

    fun isFormValid(title: String, description: String): Boolean {
        return title.isNotEmpty() && description.isNotEmpty() &&
                photoUri.isNotEmpty() && latitude != 0.0 && longitude != 0.0
    }

    fun saveReport(title: String, description: String, category: Category) {
        val old = editingReport

        if (old == null) {
            val newReport = Report(
                title = title,
                description = description,
                category = category,
                photoUri = photoUri,
                latitude = latitude,
                longitude = longitude,
                date = Utils.getCurrentDate()
            )
            repository.saveReport(newReport)
        } else {
            val updated = old.copy(
                title = title,
                description = description,
                category = category,
                photoUri = photoUri,
                latitude = latitude,
                longitude = longitude
            )
            repository.updateReport(updated)
        }
    }
}