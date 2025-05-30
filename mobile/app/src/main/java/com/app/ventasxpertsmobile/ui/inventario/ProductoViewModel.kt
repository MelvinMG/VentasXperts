package com.app.ventasxpertsmobile.ui.inventario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.ventasxpertsmobile.data.model.CategoriaDTO
import com.app.ventasxpertsmobile.data.model.ProductoDTO
import com.app.ventasxpertsmobile.data.network.RetrofitClient
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.app.ventasxpertsmobile.data.model.StockUpdateRequest

class ProductoViewModel : ViewModel() {

    val categorias = mutableStateListOf<CategoriaDTO>()
    val productos = mutableStateListOf<ProductoDTO>()

    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

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

    fun actualizarStockProducto(
        idProducto: Int,
        cantidadAgregar: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val productoActual = productos.find { it.id == idProducto }
                if (productoActual == null) {
                    onError("Producto no encontrado")
                    isLoading.value = false
                    return@launch
                }
                val nuevoStock = productoActual.stockInventario + cantidadAgregar
                val body = StockUpdateRequest(stockInventario = nuevoStock)
                val response = RetrofitClient.api.modificarProducto(idProducto, body)
                if (response.isSuccessful) {
                    onSuccess()
                    cargarProductos()
                } else {
                    onError("Error al actualizar stock: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Excepción: ${e.localizedMessage}")
            } finally {
                isLoading.value = false
            }
        }
    }

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
                    id = 0, // Id se generará en backend
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

    fun eliminarProducto(
        idProducto: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = RetrofitClient.api.eliminarProducto(idProducto)
                if (response.isSuccessful) {
                    // Refrescar productos luego de eliminar
                    cargarProductos()
                    onSuccess()
                } else {
                    cargarProductos()
                    onSuccess()
                    onError("Error al eliminar producto: ${response.code()}")
                }
            } catch (e: Exception) {
                onError("Excepción: ${e.localizedMessage}")
                cargarProductos()
                onSuccess()
            } finally {
                isLoading.value = false
            }
        }
    }



}