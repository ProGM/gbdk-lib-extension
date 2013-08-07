gbdk-lib-extension
==================

A small set of sources and tools for the Gameboy Development Kit by Michael Hope

http://gbdk.sourceforge.net/


It includes a Java application to export images to sprites and background in gbdk and some C sources, adding some useful functions to the gbdk library.


==================
FEATURES
==================

It's divided in 2 components:

TILE EDITOR:
-Export png\bmp\jpeg\tiff images to efficient assembly and C code for GBDK.

-Create maps and tileset starting from an image.

-Compress tiles in an RLE-like format


C LIBRARY:
-Various functions to setup metasprites. Metasprites are sets of sprites that can be moved all together to simulate the existance a bigger sprite.

-An improved set_bkg_data to load RLE compressed tilesets.

