# Arable Core

App Android que reúne fazendeiros, agrônomos e botânicos: o fazendeiro pesquisa espécies vegetais, salva as que importam para ele e tira dúvidas com os especialistas.

## API utilizada
GBIF Species API (`https://api.gbif.org/v1`), sem chave. Usamos `species/search` (busca de plantas aceitas, reino Plantae), `species/{key}` (detalhe e classificação botânica) e `species/{key}/vernacularNames` (nomes populares em português e inglês).

## Como o app funciona
- **Cadastro e login** locais (Room), com senha guardada como hash SHA-256 e sessão que continua ao fechar e reabrir o app.
- **Tipos de usuário:** no cadastro a pessoa escolhe Fazendeiro, Agrônomo ou Botânico.
- **Abas:** Espécies (busca na API), Especialistas (só fazendeiro), Consultas, Favoritos e Perfil (com botão Sair).
- **Consultas:** o fazendeiro escolhe um agrônomo ou botânico e envia uma pergunta, também pelo botão *Consultar sobre esta espécie* na tela de detalhe. O especialista responde na aba Consultas.
- **Favoritos e consultas** ficam no Room e pertencem a cada usuário.

## Prints
(4 a 6 imagens: login, cadastro, lista, detalhe, favoritos, estado de erro)

## Divisão de tarefas
| Integrante | O que fez |
|------------|-----------|
| Mateus | Backend |
| Demais integrantes | Figma e o restante do projeto |

## O que aprendemos
| Integrante | Uma frase sobre a sua parte |
|------------|-----------------------------|
| | |
