package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.domain.model.Report

class ReportAdapter(
    private var reportsList: List<Report>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    class ReportViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvReportTitle)
        val tvDescription: TextView = view.findViewById(R.id.tvReportDescription)
        val tvCategory: TextView = view.findViewById(R.id.tvReportCategory)
        val tvStatus: TextView = view.findViewById(R.id.tvReportStatus)

        fun bind(report: Report, listener: OnItemClickListener) {
            tvTitle.text = report.title
            tvDescription.text = report.description
            tvCategory.text = report.category.name
            tvStatus.text = report.status.name

            itemView.setOnClickListener {
                listener.onItemClick(report)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(reportsList[position], itemClickListener)
    }

    override fun getItemCount(): Int {
        return reportsList.size
    }

    fun updateData(newReports: List<Report>) {
        reportsList = newReports
        notifyDataSetChanged()
    }
}