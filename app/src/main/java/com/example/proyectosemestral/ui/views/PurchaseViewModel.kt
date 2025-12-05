package com.example.proyectosemestral.ui.views

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.proyectosemestral.ui.data.Purchase

class PurchaseViewModel : ViewModel() {

    val purchases = mutableStateListOf<Purchase>()

    fun addPurchase(purchase: Purchase) {
        purchases.add(purchase)
    }

    fun removePurchase(purchase: Purchase){
        purchases.remove(purchase)
    }
}