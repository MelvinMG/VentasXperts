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
}
