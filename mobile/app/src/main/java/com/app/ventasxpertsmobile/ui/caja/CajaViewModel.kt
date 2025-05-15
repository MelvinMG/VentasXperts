package com.app.ventasxpertsmobile.ui.caja

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.ventasxpertsmobile.data.api.ApiClient
import com.app.ventasxpertsmobile.data.api.ApiService
import com.app.ventasxpertsmobile.data.model.Ticket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.app.ventasxpertsmobile.data.model.CarritoProducto
import com.app.ventasxpertsmobile.data.model.Producto

class CajaViewModel : ViewModel() {

    private val apiService: ApiService = ApiClient.retrofit.create(ApiService::class.java)

    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    val tickets: StateFlow<List<Ticket>> = _tickets

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarHistorialTickets() {
        _isLoading.value = true
        apiService.getHistorialTickets().enqueue(object : Callback<List<Ticket>> {
            override fun onResponse(call: Call<List<Ticket>>, response: Response<List<Ticket>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _tickets.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error ${response.code()}"
                }
            }

            override fun onFailure(call: Call<List<Ticket>>, t: Throwable) {
                _isLoading.value = false
                _error.value = t.message
            }
        })
    }

    private val _productos = MutableStateFlow<List<CarritoProducto>>(emptyList())
    val productos: StateFlow<List<CarritoProducto>> = _productos

    fun cargarProductos() {
        viewModelScope.launch {
            try {
                val response = apiService.getCarritoProductos()
                _productos.value = response.results
            } catch (e: Exception) {
                // Puedes registrar el error si necesitas debuggear
                _error.value = e.message
            }
        }
    }

    private val _productosCatalogo = MutableStateFlow<List<Producto>>(emptyList())
    val productosCatalogo: StateFlow<List<Producto>> = _productosCatalogo
    fun cargarProductosCatalogo() {
        viewModelScope.launch {
            try {
                val response = apiService.getProductos()
                _productosCatalogo.value = response.results
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }


    fun agregarUnidad(id: Int) {
        viewModelScope.launch {
            try {
                apiService.agregarUnidad(id)
                cargarProductos()  // recarga lista actualizada
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun restarUnidad(id: Int) {
        viewModelScope.launch {
            try {
                apiService.restarUnidad(id)
                cargarProductos()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun quitarProducto(id: Int) {
        viewModelScope.launch {
            try {
                apiService.quitarProducto(id)
                cargarProductos()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun vaciarCarrito() {
        viewModelScope.launch {
            try {
                apiService.vaciarCarrito()
                cargarProductos()  // refrescar la lista, que debería quedar vacía
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun procesarVenta(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                apiService.procesarVenta()
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Error desconocido")
            }
        }
    }

    fun finalizarVenta(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                apiService.generarTicket()
                apiService.procesarVenta()
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Error desconocido")
            }
        }
    }


}
