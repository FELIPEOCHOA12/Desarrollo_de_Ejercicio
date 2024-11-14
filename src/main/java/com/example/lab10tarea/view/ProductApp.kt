package com.tecsup.lab10tarea.view
// Felipe Ochoa Patiño
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.lab10tarea.data.ProductApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun ProductApp() {
    val baseUrl = "http://10.0.2.2:8000/" // o tu IP si usas un dispositivo externo
    val retrofit = Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val service = retrofit.create(ProductApiService::class.java)
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.padding(top = 40.dp),
        topBar = { TopBar() },
        bottomBar = { BottomBar(navController) },
        floatingActionButton = { FloatingAddButton(navController) },
        content = { paddingValues -> Content(paddingValues, navController, service) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "PRODUCTS APP",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun BottomBar(navController: NavHostController) {
    NavigationBar(
        containerColor = Color.LightGray
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = navController.currentDestination?.route == "inicio",
            onClick = { navController.navigate("inicio") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Favorite, contentDescription = "Products") },
            label = { Text("Products") },
            selected = navController.currentDestination?.route == "products",
            onClick = { navController.navigate("products") }
        )
    }
}

@Composable
fun FloatingAddButton(navController: NavHostController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    if (currentRoute == "products") {
        FloatingActionButton(
            containerColor = Color.Magenta,
            contentColor = Color.White,
            onClick = { navController.navigate("productNew") }
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Product")
        }
    }
}

@Composable
fun Content(
    paddingValues: PaddingValues,
    navController: NavHostController,
    service: ProductApiService
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        NavHost(
            navController = navController,
            startDestination = "inicio"
        ) {
            composable("inicio") { HomeScreen() }
            composable("products") { ProductListScreen(navController, service) }
            composable("productNew") {
                ProductEditScreen(navController, service, 0)
            }
            composable("productEdit/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) {
                ProductEditScreen(navController, service, it.arguments!!.getInt("id"))
            }
            composable("productDelete/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) {
                ProductDeleteScreen(navController, service, it.arguments!!.getInt("id"))
            }
        }
    }
}
