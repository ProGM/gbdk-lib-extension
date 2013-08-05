gbdk-lib-extension
==================

A small set of sources and tools for the Gameboy Development Kit by Michael Hope


It includes a Java application to export images to sprites and background in gbdk and some C sources, adding some useful functions to the gbdk library.


==================
FEATURES
==================

-Export png\bmp\jpeg\tiff images to efficient assembly and C code for GBDK.

-Create maps starting from a "tileset", exporting both a tileset and the map in assembly code.

-Various functions to setup metasprites. Metasprites are sets of sprites that can be moved all together to simulate the existance a bigger sprite.

-An improved set_bkg_data to load RLE compressed tilesets.

