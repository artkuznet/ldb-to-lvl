# Max Payne 1 LDB to MaxED LVL converter

### [Download JAR](https://github.com/artkuznet/ldb-to-lvl/raw/refs/heads/dev/docs/ldb-to-lvl.jar)

Java 8 or more recent is required

### How to run:
```
java -Xmx1024m -jar ldb-to-lvl.jar BasicRoom.ldb
```

### Try using options to prevent some errors:
* `--skip-join-polygons` - do not join coplanar polygons
* `--skip-dynamic-fsm` - do not export dynamic mesh fsm scripts

```
java -Xmx1024m -jar ldb-to-lvl.jar --skip-join-polygons Part1_Level2.ldb
```

See also [Max Payne 1 and Max Payne 2 LDB Importer plugin for Maya](https://github.com/m0nstr0/max_payne_ldb_importer) by [m0nstr0](https://github.com/m0nstr0)

![](docs/preview1.jpg)
![](docs/preview2.jpg)
