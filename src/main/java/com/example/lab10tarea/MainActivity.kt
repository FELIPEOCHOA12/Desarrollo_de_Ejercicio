package com.example.lab10tarea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tecsup.lab10tarea.view.ProductApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductApp() // Llama a la función ProductApp que contiene la estructura de navegación y pantallas
        }
    }
}
