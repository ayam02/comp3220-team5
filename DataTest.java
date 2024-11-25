import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class DataTest {
    private Data data;
    private Entry mockEntry1;
    private Entry mockEntry2;

    /**
     * Sets up a fresh Data instance and mock entries before each test.
     */
    @Before
    public void setUp() {
        data = new Data(); // Initialize Data with default constructor
        mockEntry1 = new Entry("String", "Value1", "Field1"); // Mock Entry 1
        mockEntry2 = new Entry("Integer", 42, "Field2"); // Mock Entry 2
    }

    /**
     * Tests the default constructor to ensure the row list is initialized as empty.
     */
    @Test
    public void testDefaultConstructor() {
        assertNotNull("Row list should not be null", data.getRow());
        assertTrue("Row list should initially be empty", data.getRow().isEmpty());
    }

    /**
     * Tests the parameterized constructor to ensure it initializes the row list correctly.
     */
    @Test
    public void testParameterizedConstructor() {
        List<Entry> initialRow = new ArrayList<>();
        initialRow.add(mockEntry1);
        Data parameterizedData = new Data(initialRow);

        assertNotNull("Row list should not be null", parameterizedData.getRow());
        assertEquals("Row list should contain 1 entry", 1, parameterizedData.getRow().size());
        assertSame("First entry in the row should be mockEntry1", mockEntry1, parameterizedData.getRow().get(0));
    }

    /**
     * Tests the getRow() method.
     */
    @Test
    public void testGetRow() {
        assertNotNull("Row list should not be null", data.getRow());
        assertTrue("Row list should initially be empty", data.getRow().isEmpty());
    }

    /**
     * Tests the setRow() method.
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
    }

    /**
     * Tests the addToRow() method.
     */
    @Test
    public void testAddToRow() {
        data.addToRow(mockEntry1);
        data.addToRow(mockEntry2);

        List<Entry> row = data.getRow();
        assertEquals("Row list should contain 2 entries", 2, row.size());
        assertSame("First entry should be mockEntry1", mockEntry1, row.get(0));
        assertSame("Second entry should be mockEntry2", mockEntry2, row.get(1));
    }
}
