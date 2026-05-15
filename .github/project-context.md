# WhiteNoise — Project Context

## What This Is
A cross-modloader shared library used by wendall911 Minecraft mods. Provides a cross-platform API via Java's Service Provider Interface (SPI), a config manager, and a custom UI config editor. The UI config editor is based on the NeoForge config UI for Minecraft 1.21.1 and later.

This project is a fork of [illusivesoulworks/spectrelib](https://github.com/illusivesoulworks/spectrelib). See `README.md` for fork history and project background.

License: LGPL-2.1-only

## Branch Convention
Each branch targets a specific Minecraft version. All listed branches are independent release streams — there is no canonical main branch.

| Branch  | Modloaders          |
|---------|---------------------|
| 1.18.2  | Forge + Fabric      |
| 1.19.2  | Forge + Fabric      |
| 1.20.1  | Forge + Fabric      |
| 1.21.1  | NeoForge + Fabric   |
| 26.1    | NeoForge + Fabric   |

Branches `1.21`, `1.21.6`, `1.21.8`, `1.21.10` exist locally — confirm with the user before treating these as actively maintained.

The `upstream` remote tracks [illusivesoulworks/spectrelib](https://github.com/illusivesoulworks/spectrelib). Do not push to upstream.

## How Dependents Consume WhiteNoise
Mods that depend on this library declare it in `gradle.properties` as a version variable. It is included as:
- **Forge/NeoForge:** `jarJar` dependency in the modloader-specific `build.gradle`
- **Fabric:** `include` in `Fabric/build.gradle`

To discover which mods depend on WhiteNoise, check each mod's `gradle.properties` for a WhiteNoise version variable.

## Multi-Loader Project Structure
This project uses a multi-loader layout. Expect modloader-specific subdirectories (e.g. `Forge/`, `NeoForge/`, `Fabric/`) alongside a common/shared module. When making changes, consider whether the change is common-layer or modloader-specific before editing.

## Release Process
Follow the standard wendall911 mod release process. See `../docs/minecraft/MINECRAFT_DEVELOPMENT_NOTES.md` for the full release sequence. Releases must be made per branch — a release on `26.1` does not release `1.20.1`.
