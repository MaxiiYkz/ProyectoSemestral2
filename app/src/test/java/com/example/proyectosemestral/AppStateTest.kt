package com.example.proyectosemestral

import com.example.proyectosemestral.ui.data.AppState
import com.example.proyectosemestral.ui.data.DataStoreManager
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class AppStateTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `registrarUsuario agrega un usuario a la lista`() = runTest {
        // 1. Creamos un DataStore falso que no haga nada real
        val dataStoreFalso = mockk<DataStoreManager>(relaxed = true)

        // Simulamos que getUsers devuelve una lista vacía al principio
        coEvery { dataStoreFalso.getUsers() } returns flowOf(emptyList())
        coEvery { dataStoreFalso.profileImageUri } returns flowOf(null)

        val appState = AppState(dataStoreFalso)

        // 2. Registramos un usuario
        val resultado = appState.registrarUsuario("nuevo@test.com", "1234")

        // 3. Verificación
        assertTrue(resultado) // Debe devolver true
        assertEquals(1, appState.usuarios.size) // La lista debe tener 1 usuario
        assertEquals("nuevo@test.com", appState.usuarios[0].email)
    }

    @Test
    fun `login exitoso con credenciales correctas`() = runTest {

        val dataStoreFalso = mockk<DataStoreManager>(relaxed = true)
        coEvery { dataStoreFalso.getUsers() } returns flowOf(emptyList())
        coEvery { dataStoreFalso.profileImageUri } returns flowOf(null)

        val appState = AppState(dataStoreFalso)

        // Agregamos un usuario manualmente
        appState.registrarUsuario("pepe@test.com", "1234")

        // 2. Intentamos loguear
        val resultado = appState.login("pepe@test.com", "1234")

        // 3. Verificación
        assertTrue(resultado) // Debe ser true
        assertNotNull(appState.usuarioActual) // Debe haber un usuario logueado
        assertEquals("pepe@test.com", appState.usuarioActual?.email)
    }

    @Test
    fun `login fallido con credenciales incorrectas`() = runTest {

        val dataStoreFalso = mockk<DataStoreManager>(relaxed = true)
        coEvery { dataStoreFalso.getUsers() } returns flowOf(emptyList())
        coEvery { dataStoreFalso.profileImageUri } returns flowOf(null)

        val appState = AppState(dataStoreFalso)
        appState.registrarUsuario("pepe@test.com", "1234")

        // 2. Contraseña incorrecta
        val resultado = appState.login("pepe@test.com", "MAL_PASSWORD")

        // 3. Verificación
        assertFalse(resultado) // Debe ser false
        assertNull(appState.usuarioActual) // Nadie debe estar logueado
    }

    @Test
    fun `logout limpia el usuario actual`() = runTest {

        val dataStoreFalso = mockk<DataStoreManager>(relaxed = true)
        coEvery { dataStoreFalso.getUsers() } returns flowOf(emptyList())
        coEvery { dataStoreFalso.profileImageUri } returns flowOf(null)

        val appState = AppState(dataStoreFalso)

        // Nos logueamos primero
        appState.registrarUsuario("pepe@test.com", "123")
        appState.login("pepe@test.com", "123")

        // 2. Logout
        appState.logout()

        // 3. Verificación
        assertNull(appState.usuarioActual) // Debe ser null
        assertNull(appState.profileImageUri) // La foto debe limpiarse
    }
}