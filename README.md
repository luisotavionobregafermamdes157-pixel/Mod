# AutoTool — Fabric 1.21.11

Client-side mod for Minecraft Java Edition 1.21.11.

## Features
- **Automatic tool selection:** when you start mining a block, the mod compares the destroy speed of the items in your hotbar and selects the fastest one.
- **Durability HUD:** displays the remaining durability percentage and a color-coded bar for every damageable item in your hotbar. Red means 10% or less, orange means 11–30%, and green means more than 30%.
- Does not move items or rearrange the inventory.
- Vanilla handles the actual block-breaking action.

## Requirements
- Minecraft Java Edition 1.21.11
- Java 21 to run Minecraft
- Fabric Loader 0.19.5 or newer
- Fabric API 0.141.6+1.21.11

## Build requirements
- Java 25 to run Gradle with Fabric Loom 1.18.4
- Gradle 9.7.1

The Java compiler targets release 21, so the resulting mod remains compatible
with the Java runtime required by Minecraft 1.21.11.

## Build
Run `gradle clean build`. The remapped mod JAR is generated at:

`build/libs/autotool-fabric-1.0.0.jar`
