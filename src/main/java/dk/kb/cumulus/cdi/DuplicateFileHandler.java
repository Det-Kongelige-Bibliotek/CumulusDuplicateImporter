package dk.kb.cumulus.cdi;

import dk.kb.cumulus.CumulusRecord;
import dk.kb.cumulus.utils.ArgumentCheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Class for handling the duplicate file.
 */
public class DuplicateFileHandler {

    /** The constant for separating the columns of the lines of a csv file.*/
    public static final String CSV_COLUMN_SEPARATOR = ";";


    /** The Cumulus client.*/
    protected final CumulusClient cumulusClient;

    /**
     * Constructor.
     * @param cumulusClient The Cumulus client.
     */
    public DuplicateFileHandler(CumulusClient cumulusClient) {
        ArgumentCheck.checkNotNull(cumulusClient, "CumulusClient cumulusClient");
        this.cumulusClient = cumulusClient;
    }

    /**
     * Handles the duplicate input file.
     * @param inputFile The duplicate input file.
     * @throws IOException If it fails to read/write data.
     */
    public void handleFile(File inputFile) throws IOException {
        ArgumentCheck.checkExistsNormalFile(inputFile, "File inputFile");

        OutputHandler output = OutputHandler.getSingleton();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),
                StandardCharsets.UTF_8))) {
            String line;
            CumulusRecord groupMasterRecord = null;
            Long groupID = -1L;
            while((line = reader.readLine()) != null) {
                DuplicateFileLine dfLine = new DuplicateFileLine(line);
                if(!dfLine.valid) {
                    output.debug("Continue after invalid line: " + line);
                    continue;
                }

                CumulusRecord record = cumulusClient.getRecord(dfLine.fileName);

                if(groupID.longValue() == dfLine.groupID.longValue()) {
                    cumulusClient.setDuplicateRelationship(groupMasterRecord, record);
                } else {
                    groupMasterRecord = record;
                    groupID = dfLine.groupID;
                }
            }
        }
    }

    /**
     * Class for handling the format for the duplicate input file.
     */
    protected static class DuplicateFileLine {

        /** The group id for the line.*/
        Long groupID;
        /** The name of the file, which will be used for retrieving the Cumulus record.*/
        String fileName;
        /** The path of the file. Probably not needed.*/
        String path;
        /** The size of the file. Probably not needed.*/
        Long size;
        /** The dimensions of the image. Probably not needed.*/
        String dimensions;
        /** The match percentage for the group. Probably not needed.*/
        Integer match;

        /** Whether or not it was a valid line.*/
        boolean valid;

        /**
         * Constructor.
         * Parses the line into its parts.
         * If it fails to parse any column, then the line is set to invalid.
         * @param line The line.
         */
        protected DuplicateFileLine(String line) {
            String[] linePart = line.split(CSV_COLUMN_SEPARATOR);
            try {
                groupID = Long.parseLong(linePart[0]);
                fileName = linePart[1];
                path = linePart[2];
                size = Long.parseLong(linePart[3]);
                dimensions = linePart[4];
                path = linePart[5];
                valid = true;
            } catch (Exception e) {
                groupID = null;
                fileName = null;
                path = null;
                size = null;
                dimensions = null;
                path = null;
                valid = false;
            }
        }
    }
}
