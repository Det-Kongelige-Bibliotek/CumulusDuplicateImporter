package dk.kb.cumulus.cdi;

import dk.kb.cumulus.CumulusRecord;
import org.apache.commons.cli.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Main class for handling the arguments, perform the operation, etc.
 */
public class CumulusDuplicateImporter {

    private static class IdAndName {
        long groupId;
        String fileName;

        void setGroupId(long groupId) {
            this.groupId = groupId;
        }

        void setFileName(String fileName) {
            this.fileName = fileName;
        }

        long getGroupId() {
            return groupId;
        }

        String getFileName() {
            return fileName;
        }
    }
    private static ArrayList<IdAndName> idAndNameList = new ArrayList<>();


    /**
     * Main method for instantiating this commandline client.
     * @param args The arguments.
     */
    public static void main(String ... args) {
        // Use the following commandline argument:
        // -s Cumulus Server URL
        // -u Cumulus user name
        // -p Cumulus user password
        // -c Cumulus catalog
        // -f Excel file with duplicates
        // -v Verbose (optional - does not take argument)
        try {

            HandleOptions handleOptions = new HandleOptions(args).invoke();
            CommandLine cmd = handleOptions.getCommand();
            String server = handleOptions.getServer();
            String username = handleOptions.getUsername();
            String password = handleOptions.getPassword();
            String catalog = handleOptions.getCatalog();
            String excelFile = handleOptions.getFile();

            // Create relation master->subAsset if Group ID is equal
            try (CumulusClient cumulusClient = new CumulusClient(server, username, password, catalog)) {

                XSSFWorkbook workbook = null;
                try (FileInputStream file = new FileInputStream((new File(excelFile)))) {
                    workbook = new XSSFWorkbook(file);
                }
                XSSFSheet firstSheet = workbook.getSheetAt(0);

                //      Get Group ID and Filename from Excel sheet and save it
                int groupIdColumnIndex = 0;  // Group ID is first column in Excel sheet
                int filenameColumnIndex = 1; // Filename is second column in Excel sheet
                for (Row row : firstSheet) {
                    if (0 != row.getRowNum()) { // Skip header row
                        Cell groupIdCells = row.getCell(groupIdColumnIndex);
                        Cell filenameCells = row.getCell(filenameColumnIndex);
                        IdAndName idAndName = new IdAndName();
                        if (groupIdCells != null) {
                            if (CellType.NUMERIC == groupIdCells.getCellType()) {
                                idAndName.setGroupId((long) groupIdCells.getNumericCellValue());
                            }
                        }
                        if (filenameCells != null) {
                            if (CellType.STRING == filenameCells.getCellType()) {
                                idAndName.setFileName(filenameCells.getStringCellValue());
                            }
                        }
                        idAndNameList.add(idAndName); // store corresponding Group ID and Filename
                    }
                }
                workbook.close();

                long masterId = -1L;

                for (IdAndName entry : idAndNameList) {
                    CumulusRecord record = cumulusClient.getRecord(entry.getFileName());
                    if (record == null) {
                        System.err.println("Cannot find record: " + "; " + entry.getFileName());
                        continue;
                    }
                    if (entry.getGroupId() == masterId) {
                        if (!cmd.hasOption("v")) {
                            System.out.println("SubAsset: " + record + "; " + entry.getFileName());
                        }
                    } else {
                        if (!cmd.hasOption("v")) {
                            System.out.println("Master: " + record + "; " + entry.getFileName());
                        }
                        masterId = entry.groupId;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed!!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static class HandleOptions {
        private String[] args;
        private CommandLine cmd;
        private String serv;
        private String un;
        private String pw;
        private String cat;
        private String fil;

        HandleOptions(String... args) {
            this.args = args;
        }

        private static void PrintHelp(Options options) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("CumulusDuplicateImporter", options);
    //        final PrintWriter printWriter = new PrintWriter(System.out);
    //        helpFormatter.printUsage(printWriter,80,"CumulusDuplicateImporter", options);
    //        printWriter.flush();
            System.exit(-1);
        }

        CommandLine getCommand() {
            return cmd;
        }

        public String getServer() {
            return serv;
        }

        public String getUsername() {
            return un;
        }

        public String getPassword() {
            return pw;
        }

        public String getCatalog() {
            return cat;
        }

        public String getFile() {
            return fil;
        }

        HandleOptions invoke() throws ParseException {
            Options options = new Options();
            Option server = Option.builder()
                    .longOpt("s")
                    .argName("server")
                    .hasArg()
                    .desc("Cumulus Server URL")
                    .build();
            Option username = Option.builder()
                    .longOpt("u")
                    .argName("user name")
                    .hasArg()
                    .desc("Cumulus user name")
                    .build();
            Option password = Option.builder()
                    .longOpt("p")
                    .argName("password")
                    .hasArg()
                    .desc("Cumulus user password")
                    .build();
            Option catalog = Option.builder()
                    .longOpt("c")
                    .argName("catalog")
                    .hasArg()
                    .desc("Cumulus catalog")
                    .build();
            Option excelFile = Option.builder()
                    .longOpt("f")
                    .argName("excelFile")
                    .hasArg()
                    .desc("Excel file containing duplicates")
                    .build();

            options.addOption(server);
            options.addOption(username);
            options.addOption(password);
            options.addOption(catalog);
            options.addOption(excelFile);
            options.addOption("v", false, "Hide output");

            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
            if (cmd.hasOption("h")) {
                PrintHelp(options);
            }
            serv = null;
            if (cmd.hasOption("s")){
                serv = cmd.getOptionValue("s");
            } else {
                System.out.println("Cumulus server URL must be entered");
                PrintHelp(options);
            }
            un = null;
            if (cmd.hasOption("u")){
                un = cmd.getOptionValue("u");
            } else {
                System.out.println("Cumulus user name must be entered");
                PrintHelp(options);
            }
            pw = null;
            if (cmd.hasOption("p")){
                pw = cmd.getOptionValue("p");
            } else {
                System.out.println("Cumulus password must be entered");
                PrintHelp(options);
            }
            cat = null;
            if (cmd.hasOption("c")){
                cat = cmd.getOptionValue("c");
            } else {
                System.out.println("Cumulus catalog must be entered");
                PrintHelp(options);
            }
            fil = null;
            if (cmd.hasOption("f")){
                fil = cmd.getOptionValue("f");
            } else {
                System.out.println("Excel file with duplicates must be entered");
                PrintHelp(options);
            }
            return this;
        }
    }
}

