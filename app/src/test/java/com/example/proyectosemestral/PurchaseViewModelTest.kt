package com.example.proyectosemestral

import com.example.proyectosemestral.ui.data.Purchase
import com.example.proyectosemestral.ui.views.PurchaseViewModel
import org.junit.Assert.*
import org.junit.Test

class PurchaseViewModelTest {

    // Test 1: Verificar que se agrega una compra correctamente
    @Test
    fun `addPurchase agrega un elemento a la lista`() {
        // 1. Dado un ViewModel vacío
        val viewModel = PurchaseViewModel()
        val compraPrueba = Purchase("Juego Test", 1000, "123", userEmail = "test@test.com")

        // 2. Cuando agregamos una compra
        viewModel.addPurchase(compraPrueba)

        // 3. Entonces la lista debe tener tamaño 1 y contener el elemento
        assertEquals(1, viewModel.purchases.size)
        assertEquals("Juego Test", viewModel.purchases[0].gameName)
    }

    // Test 2: Verificar que se elimina una compra
    @Test
    fun `removePurchase elimina un elemento de la lista`() {
        // 1. Dado un ViewModel con una compra
        val viewModel = PurchaseViewModel()
        val compraPrueba = Purchase("Juego Test", 1000, "123", userEmail = "test@test.com")
        viewModel.addPurchase(compraPrueba)

        // 2. Cuando eliminamos esa compra
        viewModel.removePurchase(compraPrueba)

        // 3. Entonces la lista debe estar vacía
        assertTrue(viewModel.purchases.isEmpty())
    }
}