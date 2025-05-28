package com.app.ventasxpertsmobile.ui.inventario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.ventasxpertsmobile.data.model.CategoriaDTO
import com.app.ventasxpertsmobile.data.model.ProductoDTO
import com.app.ventasxpertsmobile.data.network.RetrofitClient
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

class ProductoViewModel : ViewModel() {

    val categorias = mutableStateListOf<CategoriaDTO>()
    val productos = mutableStateListOf<ProductoDTO>()  // Lista para productos

    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    // Cargar categorías
    fun cargarCategorias() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = RetrofitClient.api.obtenerCategorias()
                if (response.isSuccessful) {
                    categorias.clear()
                    response.body()?.results?.let {
                        categorias.addAll(it)
                    }
                    errorMessage.value = null
                } else {
                    errorMessage.value = "Error al cargar categorías: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }

    // Nueva función para cargar productos
    fun cargarProductos() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = RetrofitClient.api.obtenerProductos()
                if (response.isSuccessful) {
                    productos.clear()
                    response.body()?.results?.let {
                        productos.addAll(it)
                    }
                    errorMessage.value = null
                } else {
                    errorMessage.value = "Error al cargar productos: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }


    // Agregar producto
    fun agregarProducto(
        codigo: String,
        nombre: String,
        categoriaId: Int,
        stockInventario: Int,
        stockMinimo: Int,
        precioProveedor: Double,
        precioCliente: Double,
        gananciaPorcentaje: Double?,
        gananciaDinero: Double?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val producto = ProductoDTO(
                    codigo = codigo,
                    nombre = nombre,
                    categoria = categoriaId,
                    stockInventario = stockInventario,
                    stockMinimo = stockMinimo,
                    precioProveedor = precioProveedor,
                    precioTienda = precioCliente,
                    gananciaPorcentaje = gananciaPorcentaje,
                    gananciaPesos = gananciaDinero
                )
                val response = RetrofitClient.api.agregarProducto(producto)
                if (response.isSuccessful) {
                    onSuccess()
                    errorMessage.value = null
                } else {
                    onError("Error al guardar: ${response.code()} ${response.message()}")
                    errorMessage.value = "Error al guardar: ${response.code()}"
                }
            } catch (e: Exception) {
                onError("Excepción: ${e.localizedMessage}")
                errorMessage.value = "Excepción: ${e.localizedMessage}"
            } finally {
                isLoading.value = false
            }
        }
    }
}
