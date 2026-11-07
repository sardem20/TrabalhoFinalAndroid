package com.example.arablecoreapi.userinterface

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.arablecoreapi.viewmodel.MainViewModel

// Atalhos com as principais culturas do agronegócio (nome na tela -> nome científico buscado).
private val CULTURAS = listOf(
    "Soja" to "Glycine max",
    "Milho" to "Zea mays",
    "Café" to "Coffea arabica",
    "Cana" to "Saccharum officinarum",
    "Trigo" to "Triticum aestivum",
    "Feijão" to "Phaseolus vulgaris",
    "Mandioca" to "Manihot esculenta",
    "Algodão" to "Gossypium hirsutum"
)

// Aba "Espécies": busca na API e lista os resultados.
@Composable
fun PainelPrincipal(vm: MainViewModel, onAbrirDetalhe: (Int) -> Unit) {
    var texto by rememberSaveable { mutableStateOf(vm.ultimaBusca) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Espécies vegetais", style = MaterialTheme.typography.headlineSmall)

        Row(Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = texto, onValueChange = { texto = it },
                label = { Text("Nome científico ou popular") }, singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = { vm.buscar(texto) }, modifier = Modifier.padding(start = 8.dp)) {
                Text("Buscar")
            }
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 8.dp)) {
            items(CULTURAS) { (nome, cientifico) ->
                OutlinedButton(onClick = {
                    texto = cientifico
                    vm.buscar(cientifico)
                }) { Text(nome) }
            }
        }

        when {
            vm.carregandoLista -> CircularProgressIndicator()
            vm.erroLista != null -> {
                Text(vm.erroLista!!, color = MaterialTheme.colorScheme.error)
                Button(onClick = { vm.buscar(texto) }) { Text("Tentar novamente") }
            }
            vm.lista.isEmpty() -> Text("Nenhuma espécie encontrada.")
            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vm.lista, key = { it.key }) { especie ->
                    val favorita = vm.favoritos.any { it.especieKey == especie.key }
                    Card(onClick = { onAbrirDetalhe(especie.key) }, modifier = Modifier.fillMaxWidth()) {
                        Row(Modifier.padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(especie.nome, fontStyle = FontStyle.Italic)
                                Text("Família: ${especie.family ?: "-"}", style = MaterialTheme.typography.bodySmall)
                            }
                            IconButton(onClick = { vm.alternarFavorito(especie) }) {
                                Icon(
                                    if (favorita) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = "Favoritar"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
