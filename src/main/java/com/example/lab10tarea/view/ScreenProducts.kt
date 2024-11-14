package com.tecsup.lab10tarea.view

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tecsup.lab10tarea.data.ProductApiService
import com.tecsup.lab10tarea.data.ProductModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProductListScreen(navController: NavHostController, service: ProductApiService) {
    val productList = remember { mutableStateListOf<ProductModel>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            val list = service.selectProducts()
            productList.addAll(list)
        } catch (e: Exception) {
            Log.e("ProductListScreen", "Error fetching products: ${e.message}")
        }
    }

    LazyColumn {
        items(productList) { product ->
            ProductRow(product, navController)
        }
    }
}

@Composable
fun ProductRow(product: ProductModel, navController: NavHostController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = product.name, fontSize = 20.sp, modifier = Modifier.weight(1f))
        IconButton(onClick = { navController.navigate("productEdit/${product.id}") }) {
            Icon(imageVector = Icons.Outlined.Edit, contentDescription = "Edit")
        }
        IconButton(onClick = { navController.navigate("productDelete/${product.id}") }) {
            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete")
        }
    }
}

@Composable
fun ProductEditScreen(navController: NavHostController, service: ProductApiService, productId: Int = 0) {
    var id by remember { mutableStateOf(productId) }
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var save by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (id != 0) {
        LaunchedEffect(Unit) {
            try {
                val product = service.selectProduct(id.toString()).body()
                delay(100)
                product?.let {
                    name = it.name
                    price = it.price.toString()
                    description = it.description
                    category = it.category
                }
            } catch (e: Exception) {
                Log.e("ProductEditScreen", "Error fetching product: ${e.message}")
            }
        }
    }

    Column(Modifier.padding(16.dp)) {
        TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        TextField(value = price, onValueChange = { price = it }, label = { Text("Price") })
        TextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
        TextField(value = category, onValueChange = { category = it }, label = { Text("Category") })
        Button(onClick = { save = true }) { Text("Save") }
    }

    if (save) {
        val product = ProductModel(id, name, price.toDouble(), description, category)
        coroutineScope.launch {
            try {
                if (id == 0) service.insertProduct(product)
                else service.updateProduct(id.toString(), product)
            } catch (e: Exception) {
                Log.e("ProductEditScreen", "Error saving product: ${e.message}")
            }
            save = false
            navController.navigate("products")
        }
    }
}

@Composable
fun ProductDeleteScreen(navController: NavHostController, service: ProductApiService, id: Int) {
    var showDialog by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirm Delete") },
            text = { Text("Are you sure you want to delete this product?") },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    coroutineScope.launch {
                        try {
                            service.deleteProduct(id.toString())
                            navController.navigate("products")
                        } catch (e: Exception) {
                            Log.e("ProductDeleteScreen", "Error deleting product: ${e.message}")
                        }
                    }
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = { Button(onClick = { navController.navigate("products") }) { Text("Cancel") } }
        )
    }
}
