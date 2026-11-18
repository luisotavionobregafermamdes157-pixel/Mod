# AutoTool — Fabric 1.21.11

Client-side mod: when a new block-mining click begins, compares destroy speed for the nine hotbar slots and selects the fastest item.

## Requirements
- Minecraft Java 1.21.11
- Java 21
- Fabric Loader 0.19.5 or newer
- Fabric API 0.141.6+1.21.11

## Build
Run `gradle build`. The remapped mod JAR is generated in `build/libs/`.

Only the hotbar is scanned; items are not moved from the main inventory.
