package com.example.proyectosemestral

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectosemestral.SessionManager

class ProfileActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sessionManager = SessionManager(this)

        if (!sessionManager.isSessionActive()) {
            finish()
            return
        }

        val welcomeText: TextView = findViewById(R.id.welcomeText)
        val logoutButton: Button = findViewById(R.id.logoutButton)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewHistory)
        val noPurchasesText: TextView = findViewById(R.id.noPurchasesText)

        welcomeText.text = "Bienvenido, ${sessionManager.getActiveUser()}"

        logoutButton.setOnClickListener {
            sessionManager.logout()
            finish()
        }


        val purchases = sessionManager.getUserPurchases()
        if (purchases.isEmpty()) {
            noPurchasesText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            noPurchasesText.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerView.layoutManager = LinearLayoutManager(this)

        }
    }
}