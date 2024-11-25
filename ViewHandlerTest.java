import org.junit.*;
import static org.junit.Assert.*;

public class ViewHandlerTest {
    private ViewHandler viewHandler;

    @Before
    public void setUp() {
        // Initialize ViewHandler
        viewHandler = new ViewHandler();
        System.out.println("ViewHandler initialized successfully.");
    }

    /**
     * Test default constructor initializes an empty controllers list.
     */
    @Test
    public void testDefaultConstructor() {
        System.out.println("Testing default constructor...");
        assertNotNull("Controllers list should not be null", viewHandler.getControllersList());
        assertTrue("Controllers list should initially be empty", viewHandler.getControllersList().isEmpty());
        System.out.println("Default constructor test passed. Controllers list is empty.");
    }

    /**
     * Test getFileNames() for a known button.
     */
    @Test
    public void testGetFileNames() {
        System.out.println("Testing getFileNames for 'Funding Insights'...");
        String[] files = viewHandler.getFileNames("Funding Insights");

        assertNotNull("File names array should not be null", files);
        assertEquals("Dataset path should match expected value", "./data/csv/Funding.csv", files[0]);
        assertEquals("Config path should match expected value", "./configFiles/FundingConfig.json", files[1]);

        System.out.println("File names for 'Funding Insights':");
        System.out.println("Dataset Path: " + files[0]);
        System.out.println("Config Path: " + files[1]);
    }

    /**
     * Test getFileNames() for a valid button with expected output.
     */
    @Test
    public void testGetFileNamesForValidButton() {
        System.out.println("Testing getFileNames for a valid button...");
        String[] files = viewHandler.getFileNames("Funding Insights");

        assertNotNull("File names array should not be null", files);
        assertEquals("First file should match expected path", "./data/csv/Funding.csv", files[0]);
        assertEquals("Second file should match expected path", "./configFiles/FundingConfig.json", files[1]);

        System.out.println("Valid button file names:");
        System.out.println("Dataset Path: " + files[0]);
        System.out.println("Config Path: " + files[1]);
    }

    /**
     * Test getFileNames() for an invalid button with default empty values.
     */
    @Test
    public void testGetFileNamesForInvalidButton() {
        System.out.println("Testing getFileNames for an invalid button...");
        String[] files = viewHandler.getFileNames("Invalid Button");

        assertNotNull("File names array should not be null", files);
        assertEquals("First file should be an empty string", "", files[0]);
        assertEquals("Second file should be an empty string", "", files[1]);

        System.out.println("Invalid button file names:");
        System.out.println("Dataset Path: " + files[0]);
        System.out.println("Config Path: " + files[1]);
    }
}
