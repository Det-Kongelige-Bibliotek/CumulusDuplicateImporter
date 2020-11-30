# CumulusDuplicateImporter
Commandline tool for importing an Excel-file with duplicate Cumulus Records and setting the relationship in Cumulus.

This project requires [Cumulus JAVA SDK](https://sbprojects.statsbiblioteket.dk/display/AIM/Cumulus+Java+SDK).
Follow the installation instructions provided in the link to install.
 
Build: mvn package

After build has been completed, there should be a tar-archive intended for deployment in the `target`-folder.
 In order to use the application, the archive must be unpacked:
```
tar -xf target/cumulus-export-0.1-SNAPSHOT-distribution.tar.gz -C target/
```
After building & deploying, a Cumulus update with duplicates can be activated with
```
target/CumulusDuplicateImporter-1.0-SNAPSHOT/bin/cumulus-duplicate-importer.sh -u CUMULUS_USER -p CUMULUS_PASSWORD -s CUMULUS_SERVER -c CUMULUS_COLLECTION -f EXCEL_FILE
```