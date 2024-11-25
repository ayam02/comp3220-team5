import org.junit.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ViewHandlerTest {
    private ViewHandler viewHandler;
    private DataController dummyController1;
    private DataController dummyController2;

    /**
     * Sets up a fresh ViewHandler instance and dummy controllers before each test.
     */
    @Before
    public void setUp() {
        viewHandler = new ViewHandler(); // Initialize ViewHandler

        // Create dummy DataController objects
        dummyController1 = new DataController("dummyPath1", "dummyConfig1");
        dummyController2 = new DataController("dummyPath2", "dummyConfig2");
    }

    /**
     * Tests the default constructor to ensure the controllers list is initialized as empty.
     */
    @Test
    public void testDefaultConstructor() {
        assertNotNull("Controllers list should not be null", viewHandler.getControllersList());
        assertTrue("Controllers list should initially be empty", viewHandler.getControllersList().isEmpty());
    }

    /**
     * Tests adding a single controller to the controllers list.
     */
    @Test
    public void testAddController() {
        viewHandler.addController(dummyController1);
        List<DataController> controllers = viewHandler.getControllersList();

        assertEquals("Controllers list should contain 1 item", 1, controllers.size());
        assertSame("First item in list should be dummyController1", dummyController1, controllers.get(0));
    }

    /**
     * Tests adding multiple controllers to the controllers list.
     */
    @Test
    public void testAddMultipleControllers() {
        viewHandler.addController(dummyController1);
        viewHandler.addController(dummyController2);
        List<DataController> controllers = viewHandler.getControllersList();

        assertEquals("Controllers list should contain 2 items", 2, controllers.size());
        assertSame("First item should be dummyController1", dummyController1, controllers.get(0));
        assertSame("Second item should be dummyController2", dummyController2, controllers.get(1));
    }

    /**
     * Tests setting a new controllers list.
     */
    @Test
    public void testSetControllersList() {
        List<DataController> newList = new ArrayList<>();
        newList.add(dummyController1);
        newList.add(dummyController2);

        viewHandler.setControllersList(newList);
        List<DataController> controllers = viewHandler.getControllersList();

        assertEquals("Controllers list should contain 2 items", 2, controllers.size());
        assertSame("First item should be dummyController1", dummyController1, controllers.get(0));
        assertSame("Second item should be dummyController2", dummyController2, controllers.get(1));
    }

    /**
     * Tests replacing the controllers list with an empty list.
     */
    @Test
    public void testSetEmptyControllersList() {
        List<DataController> emptyList = new ArrayList<>();

        viewHandler.setControllersList(emptyList);
        List<DataController> controllers = viewHandler.getControllersList();

        assertNotNull("Controllers list should not be null after replacement", controllers);
        assertTrue("Controllers list should be empty", controllers.isEmpty());
    }
}
