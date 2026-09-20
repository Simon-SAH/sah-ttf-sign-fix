# SAH TTF Sign Fix

A small client-side Fabric mod that fixes TTF font rendering on signs when using Iris shaders.

## What it fixes

When a resource pack replaces Minecraft's default font with a TTF font, sign text may fail to render correctly with Iris shaders enabled.

Depending on the Minecraft/Iris version, the text may:

* disappear completely,
* render as solid rectangular glyph quads,
* or produce other incorrect grayscale-font artifacts.

SAH TTF Sign Fix restores the missing rendering behaviour required for grayscale TTF glyph atlases.

## Supported versions

* Minecraft 26.2
* Minecraft 26.3
* Fabric Loader 0.19.5 or newer
* Iris 1.11.2 or newer
* Java 25

The same mod JAR is used for both Minecraft 26.2 and 26.3.

Minecraft 26.1.2 was tested separately and does not require this fix.

## Tested shaderpacks

The mod has been tested successfully with:

* Complementary Unbound
* BSL Shaders

It should not depend on a specific shaderpack, because the problem occurs in the Iris text rendering path rather than in an individual shaderpack.

## Installation

1. Install Fabric Loader.
2. Install Iris and its required Sodium version.
3. Place `SAH TTF Sign Fix` in the Minecraft `mods` folder.
4. Use a resource pack containing a TTF font.
5. Enable your shaderpack normally.

Fabric API is not required.

## TTF resource packs

This mod does not include or install a font.

A resource pack can define a TTF font using Minecraft's font provider system, for example:

```json
{
  "providers": [
    {
      "type": "ttf",
      "file": "minecraft:montserrat.ttf",
      "shift": [0.5, 0.5],
      "size": 9.5,
      "oversample": 6.0
    }
  ]
}
```

The font itself must be supplied by the resource pack and used according to its own licence.

## Technical details

TTF glyphs are stored in a single-channel grayscale texture atlas.

In Minecraft 26.2, Iris does not assign a shader program to the grayscale polygon-offset text pipeline used by normal sign text.

SAH TTF Sign Fix restores that mapping.

Minecraft 26.3 introduced additional rendering backend changes. The mod also restores the intensity texture swizzle required for grayscale glyph data to be interpreted correctly by Iris shader programs.

The mod does not convert TTF fonts to bitmap fonts and does not modify the glyph data itself.

## Compatibility

This is a client-side rendering fix.

It does not need to be installed on a server.

The supported Minecraft range is:

```text
>=26.2 <26.4
```

Older versions are not currently supported. Minecraft 26.1.2 was tested and renders TTF sign text correctly without this mod.

## Credits

Technical investigation was informed by:

* Iris source code
* SignFontFix by mattlawliet

No SignFontFix source code is included in this project.

## License

SAH TTF Sign Fix is licensed under the MIT License.

Copyright © 2026 Simon / SAH Project
