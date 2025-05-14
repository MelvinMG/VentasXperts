package com.app.ventasxpertsmobile.ui.catalogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.ventasxpertsmobile.data.api.ApiClient
import com.app.ventasxpertsmobile.data.api.ApiService
import com.app.ventasxpertsmobile.data.model.Tienda
import com.app.ventasxpertsmobile.data.model.TiendaResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatalogoViewModel : ViewModel() {

    private val apiService: ApiService = ApiClient.retrofit.create(ApiService::class.java)

    private val _tiendas = MutableStateFlow<List<Tienda>>(emptyList())
    val tiendas: StateFlow<List<Tienda>> = _tiendas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        obtenerTiendas()
    }

    fun obtenerTiendas() {
        _isLoading.value = true
        _error.value = null

        apiService.getTiendas().enqueue(object : Callback<TiendaResponse> {
            override fun onResponse(
                call: Call<TiendaResponse>,
                response: Response<TiendaResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _tiendas.value = response.body()?.results ?: emptyList()
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<TiendaResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = t.message
            }
        })
    }
}
