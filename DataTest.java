import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the {@link Data} class.
 * This class uses JUnit to test the functionality of the {@link Data} class,
 * including its constructors, getters, and methods for manipulating the row list.
 */
public class DataTest {
    private Data data;
    private Entry mockEntry1;
    private Entry mockEntry2;

    /**
     * Sets up a fresh {@link Data} instance and mock {@link Entry} objects
     * before each test is executed.
     */
    @Before
    public void setUp() {
        data = new Data(); // Initialize Data with default constructor
        mockEntry1 = new Entry("String", "Value1", "Field1"); // Mock Entry 1
        mockEntry2 = new Entry("Integer", 42, "Field2"); // Mock Entry 2
    }

    /**
     * Tests the default constructor to ensure that the row list is
     * initialized as an empty {@link List}.
     */
    @Test
    public void testDefaultConstructor() {
        assertNotNull("Row list should not be null", data.getRow());
        assertTrue("Row list should initially be empty", data.getRow().isEmpty());

        // Print statements for verification
        System.out.println("Default Constructor Test:");
        System.out.println("Row: " + data.getRow());
    }

    /**
     * Tests the parameterized constructor to ensure that it initializes
     * the row list correctly with a given {@link List} of {@link Entry} objects.
     */
    @Test
    public void testParameterizedConstructor() {
        List<Entry> initialRow = new ArrayList<>();
        initialRow.add(mockEntry1);
        Data parameterizedData = new Data(initialRow);

        assertNotNull("Row list should not be null", parameterizedData.getRow());
        assertEquals("Row list should contain 1 entry", 1, parameterizedData.getRow().size());
        assertSame("First entry in the row should be mockEntry1", mockEntry1, parameterizedData.getRow().get(0));

        // Print statements for verification
        System.out.println("Parameterized Constructor Test:");
        System.out.println("Row: " + parameterizedData.getRow());
    }

    /**
     * Tests the {@link Data#getRow()} method to ensure it returns the correct row list.
     */
    @Test
    public void testGetRow() {
        assertNotNull("Row list should not be null", data.getRow());
        assertTrue("Row list should initially be empty", data.getRow().isEmpty());

        // Print statements for verification
        System.out.println("Get Row Test:");
        System.out.println("Row: " + data.getRow());
    }

    /**
     * Tests the {@link Data#setRow(List)} method to ensure it correctly
     * updates the row list.
     */
    @Test
    public void testSetRow() {
        List<Entry> newRow = new ArrayList<>();
        newRow.add(mockEntry1);
        newRow.add(mockEntry2);

        data.setRow(newRow);
        assertEquals("Row list should contain 2 entries", 2, data.getRow().size());
        assertSame("First entry should be mockEntry1", mockEntry1, data.getRow().get(0));
        assertSame("Second entry should be mockEntry2", mockEntry2, data.getRow().get(1));

        // Print statements for verification
        System.out.println("Set Row Test:");
        System.out.println("Row: " + data.getRow());
    }

    /**
     * Tests the {@link Data#addToRow(Entry)} method to ensure it correctly
     * appends entries to the row list.
     */
    @Test
    public void testAddToRow() {
        data.addToRow(mockEntry1);
        data.addToRow(mockEntry2);

        List<Entry> row = data.getRow();
        assertEquals("Row list should contain 2 entries", 2, row.size());
        assertSame("First entry should be mockEntry1", mockEntry1, row.get(0));
        assertSame("Second entry should be mockEntry2", mockEntry2, row.get(1));

        // Print statements for verification
        System.out.println("Add To Row Test:");
        System.out.println("Row: " + data.getRow());
    }
}
