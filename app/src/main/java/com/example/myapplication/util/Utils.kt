package com.example.myapplication.util

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.PopupMenu
import com.example.myapplication.NewReportActivity
import com.example.myapplication.R
import com.example.myapplication.ReportActivity
import com.example.myapplication.ReportListActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    // keys to send data between screens with Intent
    const val EXTRA_REPORT_ID = "report_id"
    const val EXTRA_LATITUDE = "latitude"
    const val EXTRA_LONGITUDE = "longitude"

    // default map location (Orotina, approximate)
    const val DEFAULT_LATITUDE = 9.9114
    const val DEFAULT_LONGITUDE = -84.5236

    // today's date with the card format, e.g. 12/08/26
    fun getCurrentDate(): String {
        val format = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        return format.format(Date())
    }

    // dialog with a question, the callback runs when the user answers yes
    fun showDialogCondition(context: Context, questionText: String, callback: () -> Unit) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage(questionText)
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.btn_yes)) { _, _ -> callback() }
            .setNegativeButton(context.getString(R.string.btn_no)) { dialog, _ -> dialog.cancel() }

        val alert = dialogBuilder.create()
        alert.setTitle(context.getString(R.string.title_dialog_question))
        alert.show()
    }

    // main menu, it opens the other screens of the app
    fun showMainMenu(context: Context, anchor: View) {
        val popupMenu = PopupMenu(context, anchor)
        popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.mnuReports -> {
                    val intent = Intent(context, ReportListActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    context.startActivity(intent)
                    true
                }
                R.id.mnuNewReport -> {
                    val intent = Intent(context, NewReportActivity::class.java)
                    context.startActivity(intent)
                    true
                }
                R.id.mnuManageReports -> {
                    val intent = Intent(context, ReportActivity::class.java)
                    context.startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}