# AutoTool — Fabric 1.21.11

Mod client-side para Minecraft Java Edition 1.21.11.

## Funcionalidades
- Ao apontar a mira para um bloco, compara a velocidade de quebra dos itens da hotbar e seleciona a ferramenta mais eficiente automaticamente.
- HUD com porcentagem e barra de durabilidade de cada item danificável na hotbar.
- Verde: acima de 30%; laranja: 11–30%; vermelho: 10% ou menos.
- Não move itens nem reorganiza o inventário.

## Requisitos para jogar
- Minecraft Java Edition 1.21.11
- Java 21
- Fabric Loader 0.19.5 ou superior
- Fabric API 0.141.6+1.21.11

## Compilar
Use Java 25 para executar Gradle/Fabric Loom 1.18.4 e Gradle 9.7.1.
Execute `gradle clean build`. O JAR fica em `build/libs/autotool-fabric-1.0.0.jar`.
O código Java é compilado com release 21 para compatibilidade com o runtime do Minecraft.
