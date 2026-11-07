package com.example.arablecoreapi.userinterface

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.arablecoreapi.data.local.Tipo
import com.example.arablecoreapi.viewmodel.MainViewModel
import kotlinx.serialization.Serializable

// Rotas type-safe. Só o Detalhe leva um argumento: a key da espécie.
@Serializable data object Especies
@Serializable data object Especialistas
@Serializable data object Consultas
@Serializable data object Favoritos
@Serializable data object Perfil
@Serializable data class Detalhe(val key: Int)

private class Aba(val rota: Any, val nome: String, val icone: ImageVector)

@Composable
fun AppNavigation(vm: MainViewModel) {
    // O app só abre depois do login.
    if (vm.userId == null) {
        Box(Modifier.safeDrawingPadding()) { LoginScreen(vm) }
    } else {
        AppLogado(vm)
    }
}

@Composable
private fun AppLogado(vm: MainViewModel) {
    val navController = rememberNavController()
    val destino = navController.currentBackStackEntryAsState().value?.destination

    // Só o fazendeiro tem a aba "Especialistas" (agrônomos e botânicos não consultam a si mesmos).
    val souEspecialista = vm.usuario?.tipo.let { it == Tipo.AGRONOMO || it == Tipo.BOTANICO }

    val abas = buildList {
        add(Aba(Especies, "Espécies", Icons.Filled.Eco))
        if (!souEspecialista) add(Aba(Especialistas, "Especialistas", Icons.Filled.Groups))
        add(Aba(Consultas, "Consultas", Icons.Filled.QuestionAnswer))
        add(Aba(Favoritos, "Favoritos", Icons.Filled.Favorite))
        add(Aba(Perfil, "Perfil", Icons.Filled.Person))
    }

    Scaffold(
        bottomBar = {
            // A barra só aparece nas abas, não na tela de detalhe.
            if (abas.any { destino?.hasRoute(it.rota::class) == true }) {
                NavigationBar {
                    abas.forEach { aba ->
                        NavigationBarItem(
                            selected = destino?.hasRoute(aba.rota::class) == true,
                            onClick = {
                                navController.navigate(aba.rota) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(aba.icone, contentDescription = null) },
                            label = { Text(aba.nome) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(navController, startDestination = Especies, modifier = Modifier.padding(padding)) {
            composable<Especies> {
                PainelPrincipal(vm, onAbrirDetalhe = { navController.navigate(Detalhe(it)) })
            }
            composable<Especialistas> { EspecialistasScreen(vm) }
            composable<Consultas> { ConsultasScreen(vm) }
            composable<Favoritos> {
                FavoritosScreen(vm, onAbrirDetalhe = { navController.navigate(Detalhe(it)) })
            }
            composable<Perfil> { PerfilScreen(vm) }
            composable<Detalhe> { entrada ->
                val rota = entrada.toRoute<Detalhe>()
                DetalheScreen(vm, key = rota.key, onVoltar = { navController.popBackStack() })
            }
        }
    }
}
