package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.data.memory.MemoryDataRepository
import com.example.myapplication.domain.model.Category
import com.example.myapplication.domain.model.Report
import com.example.myapplication.util.Utils
import com.example.myapplication.viewmodel.ReportDetailViewModel

class ReportActivity : AppCompatActivity() {
    private lateinit var txtId: EditText
    private lateinit var txtTitle: EditText
    private lateinit var txtDescription: EditText
    private lateinit var txtCategory: EditText
    private lateinit var txtLatitude: EditText
    private lateinit var txtLongitude: EditText
    private lateinit var reportViewModel: ReportDetailViewModel

    private var currentReport: Report? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        reportViewModel = ReportDetailViewModel(MemoryDataRepository())

        txtId = findViewById<EditText>(R.id.txtId_Report)
        txtTitle = findViewById<EditText>(R.id.txtTitle_Report)
        txtDescription = findViewById<EditText>(R.id.txtDescription_Report)
        txtCategory = findViewById<EditText>(R.id.txtCategory_Report)
        txtLatitude = findViewById<EditText>(R.id.txtLatitude_Report)
        txtLongitude = findViewById<EditText>(R.id.txtLongitude_Report)

        val btnSearch = findViewById<ImageButton>(R.id.btnSearch_Report)
        val btnSave = findViewById<Button>(R.id.btnSave_Report)
        val btnRemove = findViewById<Button>(R.id.btnRemove_Report)
        val btnCancel = findViewById<Button>(R.id.btnCancel_Report)

        btnSearch.setOnClickListener(View.OnClickListener { view ->
            searchReport()
        })

        btnSave.setOnClickListener(View.OnClickListener { view ->
            saveReport()
        })

        btnRemove.setOnClickListener(View.OnClickListener { view ->
            removeReport()
        })

        btnCancel.setOnClickListener(View.OnClickListener { view ->
            cleanScreen()
        })
    }
    private fun searchReport() {
        if (txtId.text.toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.msg_id_required), Toast.LENGTH_LONG).show()
            return
        }

        val id = txtId.text.toString().toInt()
        val report = reportViewModel.getReportById(id)

        if (report == null) {
            currentReport = null
            Toast.makeText(this, getString(R.string.msg_data_not_found), Toast.LENGTH_LONG).show()
        } else {
            currentReport = report
            txtTitle.setText(report.title)
            txtDescription.setText(report.description)
            txtCategory.setText(report.category.name)
            txtLatitude.setText(report.latitude.toString())
            txtLongitude.setText(report.longitude.toString())
        }
    }

    private fun saveReport() {
        if (txtTitle.text.toString().isEmpty() || txtDescription.text.toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.msg_incomplete_data), Toast.LENGTH_LONG).show()
            return
        }

        val category = when (txtCategory.text.toString().uppercase()) {
            "VIALIDAD" -> Category.VIALIDAD
            "ILUMINACION" -> Category.ILUMINACION
            "LIMPIEZA" -> Category.LIMPIEZA
            else -> null
        }
        if (category == null) {
            Toast.makeText(this, getString(R.string.msg_invalid_category), Toast.LENGTH_LONG).show()
            return
        }

        val latitude = txtLatitude.text.toString().toDoubleOrNull()
        val longitude = txtLongitude.text.toString().toDoubleOrNull()
        if (latitude == null || longitude == null) {
            Toast.makeText(this, getString(R.string.msg_incomplete_data), Toast.LENGTH_LONG).show()
            return
        }

        val oldReport = currentReport

        if (oldReport == null) {
            // create
            val newReport = Report(
                title = txtTitle.text.toString(),
                description = txtDescription.text.toString(),
                category = category,
                photoUri = "",
                latitude = latitude,
                longitude = longitude,
                date = Utils.getCurrentDate()
            )
            reportViewModel.saveReport(newReport)
            Toast.makeText(this, getString(R.string.msg_saved_success), Toast.LENGTH_LONG).show()
            cleanScreen()
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.title_dialog_update))
            builder.setMessage(getString(R.string.msg_confirm_update))
            builder.setPositiveButton(getString(R.string.btn_yes)) { dialog, which ->
                val updatedReport = oldReport.copy(
                    title = txtTitle.text.toString(),
                    description = txtDescription.text.toString(),
                    category = category,
                    latitude = latitude,
                    longitude = longitude
                )
                reportViewModel.updateReport(updatedReport)
                Toast.makeText(this, getString(R.string.msg_updated_success), Toast.LENGTH_LONG).show()
                cleanScreen()
            }
            builder.setNegativeButton(getString(R.string.btn_no), null)
            builder.show()
        }
    }
    private fun removeReport() {
        val report = currentReport

        if (report == null) {
            Toast.makeText(this, getString(R.string.msg_data_not_found), Toast.LENGTH_LONG).show()
            return
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.title_dialog_delete))
        builder.setMessage(getString(R.string.msg_confirm_delete))
        builder.setPositiveButton(getString(R.string.btn_yes)) { dialog, which ->
            reportViewModel.removeReport(report.id)
            Toast.makeText(this, getString(R.string.msg_deleted_success), Toast.LENGTH_LONG).show()
            cleanScreen()
        }
        builder.setNegativeButton(getString(R.string.btn_no), null)
        builder.show()
    }

    private fun cleanScreen() {
        currentReport = null
        txtId.setText("")
        txtTitle.setText("")
        txtDescription.setText("")
        txtCategory.setText("")
        txtLatitude.setText("")
        txtLongitude.setText("")
    }
}