package dk.kb.cumulus.cdi;

import com.canto.cumulus.GUID;
import dk.kb.cumulus.Constants;
import dk.kb.cumulus.CumulusRecord;
import dk.kb.cumulus.CumulusServer;
import dk.kb.cumulus.config.CumulusConfiguration;

import java.util.Arrays;

/**
 * This is a wrapped version of the CumulusServer from the KB-Cumulus-API, where the necessary methods for this project
 * are implemented.
 */
public class CumulusClient implements AutoCloseable {
    /** Constants for allowing this client to write back to Cumulus.*/
    protected static final boolean CUMULUS_WRITE_ACCESS = false;

    /** The Cumulus configuration.*/
    protected final CumulusConfiguration cumulusConfiguration;
    /** The access to the Cumulus server.*/
    protected final CumulusServer cumulusServer;
    /** The catalog.*/
    protected final String catalog;

    /**
     * Constructor.
     * @param serverUrl    The URL for the Cumulus server.
     * @param userName     The username.
     * @param userPassword The user password.
     * @param catalog      The catalog.
     */
    public CumulusClient(String serverUrl, String userName, String userPassword, String catalog) {
        this.catalog = catalog;
        this.cumulusConfiguration = new CumulusConfiguration(CUMULUS_WRITE_ACCESS, serverUrl, userName, userPassword,
                Arrays.asList(catalog));
        this.cumulusServer = new CumulusServer(cumulusConfiguration);
    }

    /**
     * Retrieves the record with the given name from the default catalog.
     * @param recordName The name of the record.
     * @return The record.
     */
    public CumulusRecord getRecord(String recordName) {
        return cumulusServer.findCumulusRecordByName(catalog, recordName);
    }

    /**
     * Create the relation
     * @param master The master record.
     * @param subAsset The subAsset record.
     */
    public void setDuplicateRelationship(CumulusRecord master, CumulusRecord subAsset) {
        // TODO: is it the variation relation we want?
        master.createRelationToRecord(subAsset, Constants.FieldNames.RELATED_SUB_ASSETS,
                GUID.UID_ASSET_RELATION_IS_VARIATION);
        subAsset.createRelationToRecord(master, Constants.FieldNames.RELATED_MASTER_ASSETS,
                GUID.UID_ASSET_RELATION_IS_VARIATION);
    }

    @Override
    public void close() throws Exception {
        cumulusServer.close();
    }
}
