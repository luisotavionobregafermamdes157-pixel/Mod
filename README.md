# AutoTool — Fabric 1.21.11

Client-side mod: when a new block-mining click begins, compares destroy speed
for the nine hotbar slots and selects the fastest item already in the hotbar.

## Runtime requirements
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

## Behavior and limits
- Only the nine hotbar slots are checked.
- Items are not moved from the main inventory.
- The game continues to handle block breaking normally.
- The selected tool is chosen using the item's block destroy-speed value.
