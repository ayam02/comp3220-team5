import org.junit.*;
import static org.junit.Assert.*;

/**
 * This class contains unit tests for the {@code ViewHandler} class, ensuring its methods behave as expected.
 * The tests validate the default constructor, file retrieval methods, and behavior for valid and invalid inputs.
 */
public class ViewHandlerTest {
    private ViewHandler viewHandler;

    /**
     * Initializes a new {@code ViewHandler} instance before each test is executed.
     */
    @Before
    public void setUp() {
        // Initialize ViewHandler
        viewHandler = new ViewHandler();
        System.out.println("ViewHandler initialized successfully.");
    }

    /**
     * Tests that the default constructor initializes the {@code controllersList} as an empty list.
     */
    @Test
    public void testDefaultConstructor() {
        System.out.println("Testing default constructor...");
        assertNotNull("Controllers list should not be null", viewHandler.getControllersList());
        assertTrue("Controllers list should initially be empty", viewHandler.getControllersList().isEmpty());
        System.out.println("Default constructor test passed. Controllers list is empty.");
    }

    /**
     * Tests the {@code getFileNames} method for the button labeled "Funding Insights".
     * Ensures that the method returns the correct dataset and configuration file paths.
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
     * Tests the {@code getFileNames} method for a valid button label.
     * Validates that the method returns the correct dataset and configuration paths.
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
     * Tests the {@code getFileNames} method for an invalid button label.
     * Ensures that the method returns empty strings for both dataset and configuration paths.
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
