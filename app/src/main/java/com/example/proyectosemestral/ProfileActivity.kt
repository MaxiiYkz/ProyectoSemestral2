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

        // Redirigir si no hay sesión iniciada
        if (!sessionManager.isSessionActive()) {
            //startActivity(Intent(this, LoginActivity::class.java))
            finish() // Cierra esta actividad para que el usuario no pueda volver con el botón "atrás"
            return
        }

        val welcomeText: TextView = findViewById(R.id.welcomeText)
        val logoutButton: Button = findViewById(R.id.logoutButton)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewHistory)
        val noPurchasesText: TextView = findViewById(R.id.noPurchasesText)

        // Mostrar nombre de usuario
        welcomeText.text = "Bienvenido, ${sessionManager.getActiveUser()}"

        // Configurar botón de cierre de sesión
        logoutButton.setOnClickListener {
            sessionManager.logout()
            //startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Mostrar historial de compras
        val purchases = sessionManager.getUserPurchases()
        if (purchases.isEmpty()) {
            noPurchasesText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            noPurchasesText.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerView.layoutManager = LinearLayoutManager(this)
            // Aquí necesitarías un `PurchaseHistoryAdapter` similar al `GameAdapter`
            // para mostrar los datos de las compras.
        }
    }
}