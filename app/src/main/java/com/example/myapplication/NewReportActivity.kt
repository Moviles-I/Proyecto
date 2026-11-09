package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.domain.model.Category
import com.example.myapplication.util.Utils
import com.example.myapplication.viewmodel.ReportFormViewModel

class NewReportActivity : AppCompatActivity() {

    private lateinit var tvFormTitle: TextView
    private lateinit var txtTitle: EditText
    private lateinit var txtDescription: EditText
    private lateinit var btnDelete: Button
    private var isEditMode: Boolean = false
    private lateinit var reportFormViewModel: ReportFormViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_report)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        reportFormViewModel = ReportFormViewModel()

        tvFormTitle = findViewById<TextView>(R.id.tvFormTitle)
        txtTitle = findViewById<EditText>(R.id.etProblemTitle)
        txtDescription = findViewById<EditText>(R.id.etDescription)
        btnDelete = findViewById<Button>(R.id.btnDelete)

        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        val reportId = intent.getIntExtra(Utils.EXTRA_REPORT_ID, -1)
        if (reportId != -1) {
            searchReport(reportId)
        }

        btnSave.setOnClickListener {
            saveReport()
        }

        btnDelete.setOnClickListener {
            deleteReport()
        }

        btnCancel.setOnClickListener {
            cleanScreen()
            finish()
        }
    }

    private fun searchReport(id: Int) {
        try {
            reportFormViewModel.loadReport(id)
            val report = reportFormViewModel.editingReport
            if (report != null) {
                isEditMode = true
                tvFormTitle.text = getString(R.string.btn_edit)
                txtTitle.setText(report.title)
                txtDescription.setText(report.description)
                btnDelete.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, getString(R.string.msg_data_not_found), Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            cleanScreen()
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun cleanScreen() {
        isEditMode = false
        txtTitle.setText("")
        txtDescription.setText("")
        btnDelete.visibility = View.GONE
    }

    fun isValidationData(): Boolean {
        return txtTitle.text.toString().trim().isNotBlank() &&
                txtDescription.text.toString().trim().isNotBlank()
    }

    fun saveReport() {
        try {
            if (isValidationData()) {
                if (isEditMode) {
                    showUpdateDialog()
                } else {
                    executeSave()
                }
            } else {
                Toast.makeText(this, getString(R.string.msg_incomplete_data), Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun executeSave() {
        val title = txtTitle.text.toString().trim()
        val description = txtDescription.text.toString().trim()
        val category = Category.VIALIDAD

        reportFormViewModel.photoUri = "photo_uri"
        reportFormViewModel.latitude = Utils.DEFAULT_LATITUDE
        reportFormViewModel.longitude = Utils.DEFAULT_LONGITUDE

        reportFormViewModel.saveReport(title, description, category)
        cleanScreen()
        Toast.makeText(this, getString(R.string.msg_saved_success), Toast.LENGTH_LONG).show()
        finish()
    }

    private fun showUpdateDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title_update))
            .setMessage(getString(R.string.dialog_msg_update))
            .setPositiveButton(getString(R.string.btn_yes)) { _, _ ->
                executeSave()
            }
            .setNegativeButton(getString(R.string.btn_no), null)
            .show()
    }

    fun deleteReport() {
        try {
            val report = reportFormViewModel.editingReport
            if (report != null) {
                showDeleteDialog(report.id)
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun showDeleteDialog(id: Int) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_title_delete))
            .setMessage(getString(R.string.dialog_msg_delete))
            .setPositiveButton(getString(R.string.btn_yes)) { _, _ ->
                reportFormViewModel.deleteReport(id)
                cleanScreen()
                Toast.makeText(this, getString(R.string.msg_deleted_success), Toast.LENGTH_LONG).show()
                finish()
            }
            .setNegativeButton(getString(R.string.btn_no), null)
            .show()
    }
}
