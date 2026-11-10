package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.domain.model.Report
import com.example.myapplication.util.Utils
import com.example.myapplication.viewmodel.ReportListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ReportListActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var reportListViewModel: ReportListViewModel
    private lateinit var etSearchReport: EditText
    private lateinit var rvReports: RecyclerView
    private lateinit var fabAddReport: FloatingActionButton

    private lateinit var reportAdapter: ReportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("es"))
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_report_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        reportListViewModel = ReportListViewModel()

        etSearchReport = findViewById(R.id.etSearchReport)
        rvReports = findViewById(R.id.rvReports)
        fabAddReport = findViewById(R.id.fabAddReport)

        reportAdapter = ReportAdapter(emptyList(), this)
        rvReports.layoutManager = LinearLayoutManager(this)
        rvReports.adapter = reportAdapter

        etSearchReport.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterReports(s.toString().trim())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        fabAddReport.setOnClickListener {
            val intent = Intent(this, NewReportActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        filterReports(etSearchReport.text.toString().trim())
    }

    private fun filterReports(query: String) {
        val filteredList = reportListViewModel.getReports(query)
        reportAdapter.updateData(filteredList)
    }

    override fun onItemClick(report: Report) {
        val intent = Intent(this, NewReportActivity::class.java).apply {
            putExtra(Utils.EXTRA_REPORT_ID, report.id)
        }
        startActivity(intent)
    }
}