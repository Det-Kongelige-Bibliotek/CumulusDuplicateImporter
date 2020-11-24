# CumulusDuplicateImporter
Commandline tool for importing an Excel-file with duplicate Cumulus Records and setting the relationship in Cumulus.

**Note:** Currently this cannot be run from commandline due to dependency to CumulusJC.jar 

Build: mvn clean package

Use: java -jar CumulusDuplicateImporter-*version*-jar-with-dependencies.jar -u *user* -p *password* -s *cumulus_server* -c *collection* -f *path_to_file/file_name* 
