package com.example.myapplication

import android.widget.ImageButton
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.data.memory.MemoryDataRepository
import com.example.myapplication.domain.model.Status
import com.example.myapplication.util.Utils
import com.example.myapplication.viewmodel.ReportDetailViewModel

class ReportDetailActivity : AppCompatActivity() {

    private lateinit var tvReportTitle: TextView
    private lateinit var tvCategoryDate: TextView
    private lateinit var btnStatus: Button
    private lateinit var tvDescription: TextView
    private lateinit var tvLatValue: TextView
    private lateinit var tvLonValue: TextView
    private var reportId: Int = -1

    private val reportDetailViewModel = ReportDetailViewModel(MemoryDataRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvReportTitle = findViewById(R.id.tvReportTitle_ReportDetail)
        tvCategoryDate = findViewById(R.id.tvCategoryDate_ReportDetail)
        btnStatus = findViewById(R.id.btnStatus_ReportDetail)
        tvDescription = findViewById(R.id.tvDescription_ReportDetail)
        tvLatValue = findViewById(R.id.tvLatValue_ReportDetail)
        tvLonValue = findViewById(R.id.tvLonValue_ReportDetail)

        val btnEdit = findViewById<Button>(R.id.btnEdit_ReportDetail)
        val btnDelete = findViewById<Button>(R.id.btnDelete_ReportDetail)
        val btnBack = findViewById<ImageButton>(R.id.btnBack_ReportDetail)
        val btnMenu = findViewById<ImageButton>(R.id.btnMenu_ReportDetail)

        btnBack.setOnClickListener {
            finish()
        }

        btnMenu.setOnClickListener { view ->
            Utils.showMainMenu(this, view)
        }
        reportId = intent.getIntExtra(Utils.EXTRA_REPORT_ID, -1)

        // open the form in edit mode with the id of this report
        btnEdit.setOnClickListener {
            val intent = Intent(this, NewReportActivity::class.java).apply {
                putExtra(Utils.EXTRA_REPORT_ID, reportId)
            }
            startActivity(intent)
        }

        btnDelete.setOnClickListener {
            Utils.showDialogCondition(
                this,
                getString(R.string.msg_confirm_delete)
            ) { deleteReport() }
        }
    }


    override fun onResume() {
        super.onResume()
        showReport()
    }

    private fun showReport() {
        val report = reportDetailViewModel.getReportById(reportId)

        if (report == null) {
            Toast.makeText(
                this,
                getString(R.string.msg_data_not_found),
                Toast.LENGTH_LONG
            ).show()
            finish()
            return
        }

        tvReportTitle.text = report.title
        tvCategoryDate.text = getString(R.string.label_category) + ": " + report.category.name + " " + report.date
        if (report.status == Status.PENDIENTE) {
            btnStatus.text = getString(R.string.status_pending)
        } else {
            btnStatus.text = getString(R.string.status_processed)
        }
        tvDescription.text = report.description
        tvLatValue.text = report.latitude.toString()
        tvLonValue.text = report.longitude.toString()
    }

    private fun deleteReport() {
        reportDetailViewModel.removeReport(reportId)
        Toast.makeText(
            this,
            getString(R.string.msg_deleted_success),
            Toast.LENGTH_LONG
        ).show()
        finish()
    }
}