package com.example.myapplication

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.data.memory.MemoryDataRepository
import com.example.myapplication.domain.model.Category
import com.example.myapplication.domain.model.Report
import com.example.myapplication.domain.model.Status
import com.example.myapplication.util.Utils
import com.example.myapplication.viewmodel.ReportDetailViewModel

class ReportActivity : AppCompatActivity() {

    private lateinit var txtId: EditText
    private lateinit var txtTitle: EditText
    private lateinit var txtDescription: EditText
    private lateinit var spCategory: Spinner
    private lateinit var txtLatitude: EditText
    private lateinit var txtLongitude: EditText
    private var isEditMode: Boolean = false

    // report found with the search, it keeps the status, photo and date
    private var currentReport: Report? = null

    private val reportViewModel = ReportDetailViewModel(MemoryDataRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtId = findViewById(R.id.txtId_Report)
        txtTitle = findViewById(R.id.txtTitle_Report)
        txtDescription = findViewById(R.id.txtDescription_Report)
        spCategory = findViewById(R.id.spCategory_Report)
        txtLatitude = findViewById(R.id.txtLatitude_Report)
        txtLongitude = findViewById(R.id.txtLongitude_Report)

        setupSpinner()

        val btnSearch = findViewById<ImageButton>(R.id.btnSearch_Report)

        btnSearch.setOnClickListener {
            searchReport(txtId.text.trim().toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.crud_actions, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.mnuDelete)?.isVisible = isEditMode
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnuSave -> {
                if (isEditMode) {
                    Utils.showDialogCondition(
                        this,
                        getString(R.string.msg_confirm_update)
                    ) { saveReport() }
                } else {
                    saveReport()
                }
                true
            }
            R.id.mnuDelete -> {
                Utils.showDialogCondition(
                    this,
                    getString(R.string.msg_confirm_delete)
                ) { deleteReport() }
                true
            }
            R.id.mnuCancel -> {
                cleanScreen()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupSpinner() {
        val categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Category.entries.toTypedArray()
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategory.adapter = categoryAdapter
    }

    // search a report by id and turn on the edit mode
    private fun searchReport(id: String) {
        if (id.isBlank()) {
            Toast.makeText(
                this,
                getString(R.string.msg_id_required),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        try {
            val report = reportViewModel.getReportById(id.toInt())
            if (report != null) {
                currentReport = report
                isEditMode = true
                txtId.isEnabled = false
                txtTitle.setText(report.title)
                txtDescription.setText(report.description)
                spCategory.setSelection(report.category.ordinal)
                txtLatitude.setText(report.latitude.toString())
                txtLongitude.setText(report.longitude.toString())
                invalidateOptionsMenu()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.msg_data_not_found),
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            cleanScreen()
            Toast.makeText(
                this,
                e.message.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun cleanScreen() {
        isEditMode = false
        currentReport = null
        txtId.isEnabled = true
        txtId.setText("")
        txtTitle.setText("")
        txtDescription.setText("")
        spCategory.setSelection(0)
        txtLatitude.setText("")
        txtLongitude.setText("")
        invalidateOptionsMenu()
    }

    private fun isValidationData(): Boolean {
        return txtTitle.text.toString().trim().isNotBlank()
                && txtDescription.text.toString().trim().isNotBlank()
                && spCategory.selectedItem != null
                && txtLatitude.text.toString().trim().isNotBlank()
                && txtLongitude.text.toString().trim().isNotBlank()
    }

    // create a new report or update the one that was found
    private fun saveReport() {
        if (!isValidationData()) {
            Toast.makeText(
                this,
                getString(R.string.msg_incomplete_data),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        try {
            val selectedCategory = spCategory.selectedItem as Category

            if (!isEditMode) {
                val report = Report(
                    0,
                    txtTitle.text.toString().trim(),
                    txtDescription.text.toString().trim(),
                    selectedCategory,
                    Status.PENDIENTE,
                    "",
                    txtLatitude.text.toString().trim().toDouble(),
                    txtLongitude.text.toString().trim().toDouble(),
                    Utils.getCurrentDate()
                )
                reportViewModel.saveReport(report)
                Toast.makeText(
                    this,
                    getString(R.string.msg_saved_success),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val oldReport = currentReport!!
                val report = Report(
                    oldReport.id,
                    txtTitle.text.toString().trim(),
                    txtDescription.text.toString().trim(),
                    selectedCategory,
                    oldReport.status,
                    oldReport.photoUri,
                    txtLatitude.text.toString().trim().toDouble(),
                    txtLongitude.text.toString().trim().toDouble(),
                    oldReport.date
                )
                reportViewModel.updateReport(report)
                Toast.makeText(
                    this,
                    getString(R.string.msg_updated_success),
                    Toast.LENGTH_LONG
                ).show()
            }
            cleanScreen()
        } catch (e: Exception) {
            Toast.makeText(
                this,
                e.message.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun deleteReport() {
        val id = txtId.text.toString().trim()
        if (id.isBlank()) return

        reportViewModel.removeReport(id.toInt())
        cleanScreen()
        Toast.makeText(
            this,
            getString(R.string.msg_deleted_success),
            Toast.LENGTH_LONG
        ).show()
    }
}