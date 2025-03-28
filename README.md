# Max Payne 1 LDB to MaxED LVL converter

### [Download JAR](https://github.com/artkuznet/ldb-to-lvl/raw/refs/heads/dev/docs/ldb-to-lvl.jar)

Java 8 or more recent is required

### Example:
```
java -jar ldb-to-lvl.jar BasicRoom.ldb
```

Please wait until all the meshes are calculated, it may take a few minutes.

### Try using options:
* `--skip-join-polygons` - do not join coplanar polygons

```
java -Xmx1024m -jar ldb-to-lvl.jar Part3_Level5.ldb --skip-join-polygons
```

See also [Max Payne 1 and Max Payne 2 LDB Importer plugin for Maya](https://github.com/m0nstr0/max_payne_ldb_importer) by [m0nstr0](https://github.com/m0nstr0)

![](docs/preview1.jpg)
![](docs/preview2.jpg)
