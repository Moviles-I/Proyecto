package com.example.myapplication

import com.example.myapplication.domain.model.Report

interface OnItemClickListener {
    fun onItemClick(report: Report)
}