package hk.edu.polyu.comp.comp2021.clevis.model;

import hk.edu.polyu.comp.comp2021.clevis.controller.Application;
import hk.edu.polyu.comp.comp2021.clevis.model.shapes.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class CLITest {
    private Application app;

    private ByteArrayOutputStream outputStream;
    @BeforeEach
    public void setUp() {
        // 创建一个 ByteArrayOutputStream 捕获输出
        app = new Application();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        if (CLI.getShapes_map() != null) {
            CLI.getShapes_map().clear();
            CLI.getShapes_list().clear();
        }
    }
    @AfterEach
    public void clear(){outputStream.reset();}
    @Test
    public void testAddRect01() {
        String Input = "rectangle myRectangle 1 2 3 4\n"; //
        simulateInput(Input);
        app.runCLI();
        assertTrue(CLI.getShapes_map().containsKey("myRectangle"));
        Input = "rectangle myRectangle 1 2 3 4\n";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Command error, name exists!",output);
        simulateInput("undo");
        app.runCLI();
        simulateInput("redo");
        app.runCLI();
    }
    @Test
    public void testAddRect02() {
        String Input = "rectangle myRectangle 1 2 3 \n";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Command error!",output);
    }
    @Test
    public void testAddRect03() {
        String Input = "rectangle C2 1 2 3 x\n";
        simulateInput(Input);
        app.runCLI();
        Input = "rectangle myLine 2 3 4 -1\n";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Command error in Parameters!",output);
    }
    @Test
    public void testAddLine03() {
        String Input = "line myLine 2 3 4\n";
        simulateInput(Input);
        app.runCLI();
        Input = "line C2 1 2 3 x\n";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Command error in Parameters!",output);
    }
    @Test
    public void testAddLine01() {
        String Input = "line myLine 1 2 3 4\n";
        simulateInput(Input);
        app.runCLI();
        assertTrue(CLI.getShapes_map().containsKey("myLine"));
        Input = "line myLine 1 2 3 4\n";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Command error, name exists!",output);
        simulateInput("undo");
        app.runCLI();
        simulateInput("redo");
        app.runCLI();
    }
    @Test
    public void testAddLine02() {
        String Input = "line myLine 1 2 1 2\n";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Command error in Parameters!",output);
    }
    @Test
    public void testAddCircle01() {
        String Input = "circle C 1 2 3\n";
        simulateInput(Input);
        app.runCLI();
        assertTrue(CLI.getShapes_map().containsKey("C"));
        Input = "circle C 1 2 3\n";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Command error, name exists!",output);
        simulateInput("undo");
        app.runCLI();
        simulateInput("undo");
        app.runCLI();
        simulateInput("redo");
        app.runCLI();
    }
    @Test
    public void testAddCircle02() {
        String Input = "circle C 1 2 -1\n";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Command error in Parameters!",output);
    }
    @Test
    public void testAddCircle03() {
        String Input = "circle C2 1 2 --1\n";
        simulateInput(Input);
        app.runCLI();
        Input = "circle C2 1 2 3 4\n";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Command error!",output);
    }
    @Test
    public void testAddSquare01() {
        String Input = "square s 1 2 3\n";
        simulateInput(Input);
        app.runCLI();
        assertTrue(CLI.getShapes_map().containsKey("s"));
        Input = "square s 1 2 3\n";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Command error, name exists!",output);
        simulateInput("undo");
        app.runCLI();
        simulateInput("redo");
        app.runCLI();
    }
    @Test
    public void testAddSquare02() {
        String Input = "square s 1 2 -1\n";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Command error in Parameters!",output);
    }
    @Test
    public void testAddSquare03() {
        String Input = "square C2 1 2 x\n";
        simulateInput(Input);
        app.runCLI();
        Input = "square s1 1 2 3 4\n";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Command error!",output);
    }
    @Test
    public void testuc() {
        String Input = "lall";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Unknown command!",output);
    }
    @Test
    public void testListall() {
        String Input = "listall";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>No shapes currently exist.",output);
        Input = "square s 1 2 3\n";
        simulateInput(Input);
        app.runCLI();
        Input = "circle C 1 2 3\n";
        simulateInput(Input);
        app.runCLI();
        Input = "line myLine 1 2 3 4\n";
        simulateInput(Input);
        app.runCLI();
        simulateInput("rectangle r 0 0 2 1");
        app.runCLI();
        Input = "listall";
        simulateInput(Input);
        app.runCLI();
        output = getLastOutputLine();
        assertEquals("Square s: Top-left  corner is at location (1.00,2.00), side length = 3.00.",output);
        Input = "group r s myLine";
        simulateInput(Input);
        app.runCLI();
        Input = "group g1 s myLine";
        simulateInput(Input);
        app.runCLI();
        Input = "group g2 g1 myLine";
        simulateInput(Input);
        app.runCLI();
        Input = "group g2 g1 r";
        simulateInput(Input);
        app.runCLI();
        Input = "list g2";
        simulateInput(Input);
        app.runCLI();
        Input = "listall";
        simulateInput(Input);
        app.runCLI();
        output = getLastOutputLine();
        assertEquals("Circle C: Center is at location (1.00,2.00), radius = 3.00.",output);
        Input = "ungroup g2";
        simulateInput(Input);
        app.runCLI();
        Input = "ungroup g1";
        simulateInput(Input);
        app.runCLI();
    }
    @Test
    public void testList() {
        String Input = "list n";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Command error in parameters! Shape n doesn't exist.",output);
        Input = "square s 1 2 3\n";
        simulateInput(Input);
        app.runCLI();
        Input = "circle C 1 2 3\n";
        simulateInput(Input);
        app.runCLI();
        Input = "line myLine 1 2 3 4\n";
        simulateInput(Input);
        app.runCLI();
        Input = "list s";
        simulateInput(Input);
        app.runCLI();
        output = getLastOutputLine();
        assertEquals("clevis>Square s: Top-left  corner is at location (1.00,2.00), side length = 3.00.",output);
        Input = "list myLine";
        simulateInput(Input);
        app.runCLI();
        output = getLastOutputLine();
        assertEquals("clevis>Line myLine: From location (1.00,2.00) to location (3.00,4.00).",output);
        simulateInput("list C");
        app.runCLI();
        simulateInput("rectangle r 0 0 2 1");
        app.runCLI();
        simulateInput("list r");
        app.runCLI();
        Input = "group k";
        simulateInput(Input);
        app.runCLI();
        Input = "group g s myLine C";
        simulateInput(Input);
        app.runCLI();
        Input = "list g";
        simulateInput(Input);
        app.runCLI();
        output = getLastOutputLine();
        assertEquals("    Circle C: Center is at location (1.00,2.00), radius = 3.00.",output);
    }
    @Test
    public void testbb() {
        String Input = "boundingbox n";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Shape not found!",output);
        Input = "square s 1 2 3\n";
        simulateInput(Input);
        app.runCLI();
        Input = "circle C 1 2 3\n";
        simulateInput(Input);
        app.runCLI();
        Input = "boundingbox C";
        simulateInput(Input);
        app.runCLI();
        Input = "line myLine 1 2 3 4\n";
        simulateInput(Input);
        app.runCLI();
        Input = "boundingbox myLine";
        simulateInput(Input);
        app.runCLI();
        simulateInput("rectangle r 0 0 2 1");
        app.runCLI();
        Input = "boundingbox r";
        simulateInput(Input);
        app.runCLI();
        Input = "boundingbox ss";
        simulateInput(Input);
        app.runCLI();
        Input = "boundingbox s";
        simulateInput(Input);
        app.runCLI();
        output = getLastOutputLine();
        assertEquals("clevis>1.00 2.00 3.00 3.00",output);
        Input = "group g s myLine C r";
        simulateInput(Input);
        app.runCLI();
        Input = "boundingbox g";
        simulateInput(Input);
        app.runCLI();
        output = getLastOutputLine();
        assertEquals("clevis>-2.00 5.00 6.00 6.00",output);
   }
    @Test
    public void testmove() {
        String Input = "move n";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Command error!",output);
        Input = "move s 1 1";
        simulateInput(Input);
        app.runCLI();
        Input = "move s 1 x";
        simulateInput(Input);
        app.runCLI();
        Input = "square s 1 2 3\n";
        simulateInput(Input);
        app.runCLI();
        Input = "circle C 1 2 3\n";
        simulateInput(Input);
        app.runCLI();
        simulateInput("rectangle r 0 0 2 1");
        app.runCLI();
        simulateInput("line l 0 0 2 1");
        app.runCLI();
        Input = "move s 1 1";
        simulateInput(Input);
        app.runCLI();
        Input = "move l 1 1";
        simulateInput(Input);
        app.runCLI();
        Input = "move C 1 1";
        simulateInput(Input);
        app.runCLI();
        Input = "move r 1 1";
        simulateInput(Input);
        app.runCLI();
        output = getLastOutputLine();
        assertEquals("clevis>Move successfully!",output);
        simulateInput("undo");
        app.runCLI();
        simulateInput("redo");
        app.runCLI();
        simulateInput("undo");
        app.runCLI();
        Input = "group g s l C";
        simulateInput(Input);
        app.runCLI();
        Input = "move g 1 1";
        simulateInput(Input);
        app.runCLI();
        Input = "group g2 g r";
        simulateInput(Input);
        app.runCLI();
        Input = "move g2 1 1";
        simulateInput(Input);
        app.runCLI();
    }
    @Test
    public void testsa() {
        String Input = "shapeat n";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Command error!",output);
        Input = "square s 0 2 2\n";
        simulateInput(Input);
        app.runCLI();
        Input = "circle C -1 1 1\n";
        simulateInput(Input);
        app.runCLI();
        Input = "shapeat 2 2";
        simulateInput(Input);
        app.runCLI();
        Input = "shapeat -1 0";
        simulateInput(Input);
        app.runCLI();
        Input = "rectangle r 0 0 2 1";
        simulateInput(Input);
        app.runCLI();
        Input = "shapeat 0 0";
        simulateInput(Input);
        app.runCLI();
        Input = "shapeat 0.25 -1";
        simulateInput(Input);
        app.runCLI();
        Input = "line myLine 0 0 2 1";
        simulateInput(Input);
        app.runCLI();
        Input = "shapeat 0 0";
        simulateInput(Input);
        app.runCLI();
        Input = "group g s myLine C";
        simulateInput(Input);
        app.runCLI();
        Input = "shapeat 0 2";
        simulateInput(Input);
        app.runCLI();
        Input = "group g2 g r";
        simulateInput(Input);
        app.runCLI();
        Input = "shapeat 0 2";
        simulateInput(Input);
        app.runCLI();
        Input = "shapeat 0 x";
        simulateInput(Input);
        app.runCLI();
    }
    @Test
    public void testin() {
        String Input = "intersect n";
        simulateInput(Input);
        app.runCLI();
        Input = "square s 1 2 3\n";
        simulateInput(Input);
        app.runCLI();
        Input = "circle C 1 2 3\n";
        simulateInput(Input);
        app.runCLI();
        Input = "line myLine 1 2 3 4\n";
        simulateInput(Input);
        app.runCLI();
        simulateInput("rectangle r 0 0 2 1");
        app.runCLI();
        Input = "intersect s m";
        simulateInput(Input);
        app.runCLI();
        Input = "intersect s myLine";
        simulateInput(Input);
        app.runCLI();
        String output = getLastOutputLine();
        assertEquals("clevis>Shapes s and myLine do not intersect",output);
        Input = "group g s myLine C";
        simulateInput(Input);
        app.runCLI();
        Input = "intersect g r";
        simulateInput(Input);
        app.runCLI();
        output = getLastOutputLine();
        assertEquals("clevis>Shapes g and r intersect",output);
    }
    private String getLastOutputLine() {
        String output = outputStream.toString().trim(); // 获取所有输出
        String[] lines = output.split("\n"); // 按行分割
        return lines[lines.length - 2].replace("\n", "").replace("\r", "");
    }

    public void simulateInput(String a){
        InputStream in = new ByteArrayInputStream(a.getBytes());
        System.setIn(in);
    }
}