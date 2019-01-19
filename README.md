# Black Lotus - App

**Autor**: Anderson Simões Vieira.

**Data**: 16/01/2019.

**Tecnologias**: Android (Java)

Entrega do Projeto Nanodegree - Android - Turma 09/03/2019.

**Repositório**: https://github.com/andersonsv/adnd_black_lotus

### Projeto.

Requisitos sistema.

    Android Studio v3.2 ou superior.
    Java 8.
    
### Passos para rodar o projeto.
- Copiar o arquivo google-services.json (enviado separadamente) para o diretório [app] do projeto

- Criar o arquivo gradle.properties com o conteúdo 

RELEASE_STORE_FILE=src/main/res/raw/blacklotus.jks

RELEASE_STORE_PASSWORD=[password]

RELEASE_KEY_ALIAS=[alias]

RELEASE_KEY_PASSWORD=[password]

os dados serão enviados separadamente.

### Rodar testes unitários.
 ./gradlew test
 
 
 ### Rodar testes instrumentados.
Certificar que o aparalho/emulador esteja conectado à internet
 ./gradlew connectedAndroidTest