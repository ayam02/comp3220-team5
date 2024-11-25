
import org.junit.*;


import static org.junit.Assert.*;

public class EntryTest {
    private Entry entry;

    /**
     * Sets up a fresh Entry instance before each test.
     */
    @Before
    public void setUp() {
        entry = new Entry(); // Default constructor
    }

    /**
     * Tests the default constructor to ensure all fields are null.
     */
    @Test
    public void testDefaultConstructor() {
        assertNull("Field should be null", entry.getField());
        assertNull("Type should be null", entry.getType());
        assertNull("Value should be null", entry.getValue());

          // Print statements for verification
          System.out.println("Default Constructor Test:");
          System.out.println("Field: " + entry.getField());
          System.out.println("Type: " + entry.getType());
          System.out.println("Value: " + entry.getValue());
    }

    /**
     * Tests the parameterized constructor to ensure fields are correctly set.
     */
    @Test
    public void testParameterizedConstructor() {
        Entry parameterizedEntry = new Entry("STRING", "Hello", "greeting");
        assertEquals("Type should be STRING", "STRING", parameterizedEntry.getType());
        assertEquals("Value should be Hello", "Hello", parameterizedEntry.getValue());
        assertEquals("Field should be greeting", "greeting", parameterizedEntry.getField());

         // Print statements for verification
         System.out.println("Parameterized Constructor Test:");
         System.out.println("Field: " + parameterizedEntry.getField());
         System.out.println("Type: " + parameterizedEntry.getType());
         System.out.println("Value: " + parameterizedEntry.getValue());
    }

    /**
     * Tests the setField() and getField() methods.
     */
    @Test
    public void testSetAndGetField() {
        entry.setField("name");
        assertEquals("Field should be name", "name", entry.getField());

        // Print statements for verification
        System.out.println("Set and Get Field Test:");
        System.out.println("Field: " + entry.getField());
    }
    /**
     * Tests the setType() and getType() methods.
     */
    @Test
    public void testSetAndGetType() {
        entry.setType("INTEGER");
        assertEquals("Type should be INTEGER", "INTEGER", entry.getType());
        
        // Print statements for verification
        System.out.println("Set and Get Type Test:");
        System.out.println("Type: " + entry.getType());
    }

    /**
     * Tests the setValue() and getValue() methods with different data types.
     */
    @Test
    public void testSetAndGetValue(){
        // Test with a String
        entry.setValue("TestString");
        assertEquals("Value should be TestString", "TestString", entry.getValue());
        System.out.println("Set and Get Value Test with String:");
        System.out.println("Value: " + entry.getValue());

        // Test with an Integer
        entry.setValue(42);
        assertEquals("Value should be 42", 42, entry.getValue());
        System.out.println("Set and Get Value Test with Integer:");
        System.out.println("Value: " + entry.getValue());

        // Test with a Float
        entry.setValue(3.14f);
        assertEquals("Value should be 3.14", 3.14f, entry.getValue());
        System.out.println("Set and Get Value Test with Float:");
        System.out.println("Value: " + entry.getValue());
    }

    /**
     * Tests the toString() method to ensure it returns the expected format.
     */
    @Test
    public void testToString() {
        entry.setField("age");
        entry.setType("INTEGER");
        entry.setValue(25);
        String expected = "age = 25 is of type: INTEGER";
        assertEquals("toString() output should match expected format", expected, entry.toString());
    
        // Print statements for verification
        System.out.println("ToString Test:");
        System.out.println("toString: " + entry.toString());
    }

    /**
     * Tests the null values
     */
    @Test
    public void testSetNullValues() {
        entry.setField(null);
        assertNull(entry.getField());
        System.out.println("Set Null Field Test:");
        System.out.println("Field: " + entry.getField());

        entry.setType(null);
        assertNull(entry.getType());
        System.out.println("Set Null Type Test:");
        System.out.println("Type: " + entry.getType());

        entry.setValue(null);
        assertNull(entry.getValue());
        System.out.println("Set Null Value Test:");
        System.out.println("Value: " + entry.getValue());
    }

   @Test
    public void testEmptyStrings(){
        entry.setField("");
        assertEquals("", entry.getField());
        System.out.println("Set Empty Field Test:");
        System.out.println("Field: " + entry.getField());

        entry.setType("");
        assertEquals("", entry.getType());
        System.out.println("Set Empty Type Test:");
        System.out.println("Type: " + entry.getType());
    }

}
