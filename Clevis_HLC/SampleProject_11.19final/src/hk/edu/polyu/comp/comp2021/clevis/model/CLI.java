package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import hk.edu.polyu.comp.comp2021.clevis.model.shapes.*;

/**
 * This class stores methods of shape operations like move, add shape...
 * This class also contains the shapes_map which stores all the current shape.It can be used for further search.
 */
public class CLI {
    private static Map<String, Shape> shapes_map = new HashMap<>();
    private static List<Shape> shapes_list = new ArrayList<>();
    /**
     * REQ2-5
     * @param s the shape to be added
     * @param name name of shape
     */
    public static void addShape(String name, Shape s) {
        getShapes_map().put(name, s);
        getShapes_list().add(s);
        System.out.println("Add successfully!");
    }
    /**
     * REQ8
     * @param command the command user entered
     * @return valid or not
     */
    public static boolean deleteShape(String command) {
        String[] parts = command.split(" ");
        if (parts.length != 2) {
            System.out.println("Command error!");
            return false;
        }
        String deletedShapeName = parts[1];
        if (!getShapes_map().containsKey(deletedShapeName)) {
            System.out.println("Command error in parameters! Shape '" + deletedShapeName + "' doesn't exist.");
            return false;
        }
        Shape deletedShape = getShapes_map().get(deletedShapeName);
        Boolean removeMap = getShapes_map().remove(deletedShapeName, deletedShape);
        Boolean removeList = getShapes_list().remove(deletedShape);
        if (removeMap && removeList) {
            System.out.println("Shape " + deletedShapeName + " is deleted successfully.");
            return true;
        }
        System.out.println("Shape " + deletedShapeName + " fails to be deleted.");
        return false;
    }
    /**
     * REQ10
     * @param command the command user entered
     * @return valid or not
     */
    public static boolean move(String command) {
        double[] parameters = new double[2];
        String[] com_list = command.split(" ");
        if (com_list.length != 4) {
            System.out.println("Command error!Please check 'help' for correct commands");
            return false;
        }
        String name = com_list[1];
        if (!getShapes_map().containsKey(name)) {
            System.out.println("Command error, name doesn't exist!");
            return false;
        }
        try {
            for (int i = 2; i < 4; i++) {
                parameters[i - 2] = Double.parseDouble(com_list[i]);
            }
        } catch (NumberFormatException e) {
            System.out.println("Command error in Parameters!");
            return false;
        }
        getShapes_map().get(name).move(parameters[0], parameters[1]);
        System.out.println("Move successfully!");
        return true;
    }

    /**
     * REQ11
     * @param command the command user entered
     */
    public static void shapeAt(String command) {
        double[] parameters = new double[2];
        String[] com_list = command.split(" ");
        if (com_list.length != 3) {
            System.out.println("Command error!Please check 'help' for correct commands");
            return;
        }
        try {
            for (int i = 1; i < 3; i++) {
                parameters[i - 1] = Double.parseDouble(com_list[i]);
            }
        } catch (NumberFormatException e) {
            System.out.println("Command error in Parameters!");
        }
        double x = parameters[0];
        double y = parameters[1];
        for (int i = getShapes_list().size() - 1; i >= 0; i--) {
            Shape shape2 = getShapes_list().get(i);
            if (shape2.shapeAt(x, y)) {
                String name = shape2.getName();
                String positionX = String.format("%.2f", x);
                String positionY = String.format("%.2f", y);
                System.out.println("Shape at (" + positionX + "," + positionY + ") is " + name + ".");
                return;
            }
        }
        System.out.println("Not found.");
    }

    public static double calculatePtoL(double x, double y, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double segmentLengthSquared = dx * dx + dy * dy;
        double distanceToStart = Math.sqrt(Math.pow(x1 - x, 2) + Math.pow(y1 - y, 2));
        if (segmentLengthSquared == 0) {
            return distanceToStart;
        }
        double t = ((x - x1) * dx + (y - y1) * dy) / segmentLengthSquared;

        if (t < 0) {
            return distanceToStart;
        } else if (t > 1) {
            return Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
        } else {
            double projX = x1 + t * dx;
            double projY = y1 + t * dy;

            return Math.sqrt(Math.pow(projX - x, 2) + Math.pow(projY - y, 2));
        }
    }
     /** BON2
     * @param command the command to be undone
     */
    public static void undo(String command) {
        String[] com_list = command.split(" ", 8);
        String word = com_list[0];
        String newCommand;
        switch (word.toLowerCase()) {
            case "rectangle":
            case "line":
            case "circle":
            case "square":
                newCommand = "delete " + com_list[1];
                CLI.deleteShape(newCommand);
                break;
            case "move":
                double[] parameters = new double[2];
                for (int i = 2; i < 4; i++) {
                    parameters[i - 2] = Double.parseDouble(com_list[i]);
                }
                    newCommand = word + " " + com_list[1] + " " + (-parameters[0]) + " " + (-parameters[1]);
                    CLI.move(newCommand);

        }
    }
    /**
     * BON2
     * @param command the command to be redone
     */
    public static void redo(String command) {
        String[] com_list = command.split(" ", 8);
        String word = com_list[0];
        switch (word.toLowerCase()) {
            case "rectangle":
                Rectangle.createRectangle(command);
                break;
            case "line":
                Line.createLine(command);
                break;
            case "circle":
                Circle.createCircle(command);
                break;
            case "square":
                Square.createSquare(command);
                break;
            case "delete":
                CLI.deleteShape(command);
                break;
            case "move":
                CLI.move(command);
                break;
        }
    }

    public static Map<String, Shape> getShapes_map() {
        return shapes_map;
    }

    public static List<Shape> getShapes_list() {
        return shapes_list;
    }
}
