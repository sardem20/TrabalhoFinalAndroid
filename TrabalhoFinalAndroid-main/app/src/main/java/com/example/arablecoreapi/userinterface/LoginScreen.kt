package com.example.arablecoreapi.userinterface

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.arablecoreapi.data.local.Tipo
import com.example.arablecoreapi.viewmodel.MainViewModel

// Uma tela só para entrar e para criar conta: o botão de baixo alterna entre os dois modos.
@Composable
fun LoginScreen(vm: MainViewModel) {
    var cadastrando by rememberSaveable { mutableStateOf(false) }
    var nome by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var tipo by rememberSaveable { mutableStateOf(Tipo.FAZENDEIRO) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            if (cadastrando) "Criar conta" else "Entrar",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (cadastrando) {
            OutlinedTextField(
                value = nome, onValueChange = { nome = it },
                label = { Text("Nome") }, singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("E-mail") }, singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        OutlinedTextField(
            value = senha, onValueChange = { senha = it },
            label = { Text("Senha") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        if (cadastrando) {
            Text("Eu sou:", modifier = Modifier.padding(top = 12.dp))
            listOf(Tipo.FAZENDEIRO, Tipo.AGRONOMO, Tipo.BOTANICO).forEach { t ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = tipo == t, onClick = { tipo = t })
                    Text(Tipo.nome(t))
                }
            }
        }

        vm.erroLogin?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Button(
            onClick = {
                if (cadastrando) vm.cadastrar(nome, email, senha, tipo) else vm.entrar(email, senha)
            },
            enabled = !vm.entrando,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) { Text(if (cadastrando) "Cadastrar" else "Entrar") }

        TextButton(onClick = {
            cadastrando = !cadastrando
            vm.limparErro()
        }) { Text(if (cadastrando) "Já tenho conta" else "Não tenho conta") }
    }
}
