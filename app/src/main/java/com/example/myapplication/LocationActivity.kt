package com.example.myapplication

import com.example.myapplication.util.Utils
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_location)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack = findViewById<ImageButton>(R.id.btnBack_Location)
        val btnConfirm = findViewById<Button>(R.id.btnConfirm_Location)
        btnBack.setOnClickListener {
            finish()
        }
        val btnMenu = findViewById<ImageButton>(R.id.btnMenu_Location)

        btnMenu.setOnClickListener { view ->
            Utils.showMainMenu(this, view)
        }
        btnConfirm.setOnClickListener {
            Toast.makeText(
                this,
                getString(R.string.msg_location_confirmed),
                Toast.LENGTH_LONG
            ).show()
            finish()
        }
    }
}