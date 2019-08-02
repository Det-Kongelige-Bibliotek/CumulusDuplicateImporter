package dk.kb.cumulus.cdi;

import org.jaccept.structure.ExtendedTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ConstantsTest extends ExtendedTestCase {

    @Test
    public void testInstantiation() {
        Constants c = new Constants();
        Assert.assertTrue(c instanceof Constants);
    }
}
