package hk.edu.polyu.comp.comp2021.clevis.controller;

import hk.edu.polyu.comp.comp2021.clevis.model.CLI;
import hk.edu.polyu.comp.comp2021.clevis.model.shapes.*;
import hk.edu.polyu.comp.comp2021.clevis.view.*;

import java.util.*;

/**
 * This file carries the job of main function.
 */
public class Application {
    private List<String> commandHistory = new ArrayList<>();
    private List<String> execommandHistory = new ArrayList<>();
    private List<String> undoHistory = new ArrayList<>();
    private GUI gui;
    public List<String> getExecommandHistory() { return execommandHistory; }
    public List<String> getUndoHistory() { return undoHistory; }
    public Application() {

    }

    public static void main(String[] args) {
        Application app = new Application();
        app.startGUI();
    }

    public void startGUI() {
        GUI.startGUI(this);
    }

    // 供 GUI 调用的命令处理方法
    public boolean processCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false;
        }

        String trimmedCommand = command.trim();
        String[] com_list = trimmedCommand.split(" ", 2);
        String word = com_list[0].toLowerCase();
        boolean valid = false;

        // 记录命令历史
        commandHistory.add(trimmedCommand);

        try {
            switch (word) {
                case "rectangle":
                    valid = Rectangle.createRectangle(trimmedCommand);
                    break;
                case "line":
                    valid = Line.createLine(trimmedCommand);
                    break;
                case "circle":
                    valid = Circle.createCircle(trimmedCommand);
                    break;
                case "square":
                    valid = Square.createSquare(trimmedCommand);
                    break;
                case "delete":
                    valid = CLI.deleteShape(trimmedCommand);
                    break;
                case "move":
                    valid = CLI.move(trimmedCommand);
                    break;
                case "shapeat":
                    CLI.shapeAt(trimmedCommand);
                    break;
                case "undo":
                    List<String> history = getExecommandHistory();
                    if (history == null || history.isEmpty()) {
                        System.out.println("No operation to undo");
                        break;
                    }
                    if(command.equalsIgnoreCase("undo")) {
                        int l= getExecommandHistory().size() - 1;
                        while(l>=0&&(getExecommandHistory().get(l).equals("undo")||getExecommandHistory().get(l).equals("redo"))){l--;}
                        if (l>=0) {
                            String c = getExecommandHistory().get(l);
                            CLI.undo(c);
                            getExecommandHistory().remove(l);
                            getUndoHistory().add(c);
                            valid=true;
                            break;
                        }
                    }

                    break;
                case "redo":
                    if(command.equalsIgnoreCase("redo")&&getExecommandHistory()!=null) {
                        int l= getExecommandHistory().size() - 1;
                        if (l>=0) {
                            String c = getExecommandHistory().get(l);
                            if (!Objects.equals(c, "undo")&&!Objects.equals(c,"redo")) {
                                System.out.println("No operation to redo");
                                break;
                            }

                            if (getUndoHistory() != null) {
                                CLI.redo(getUndoHistory().getLast());
                                getExecommandHistory().remove(l);
                                valid=true;
                                getExecommandHistory().add(getUndoHistory().getLast());
                                getUndoHistory().removeLast();
                            }
                        }
                        if (l<0) {
                            System.out.println("No operation to redo");
                        }
                    }
                    break;
                case "quit":
                case "exit":
                    System.out.println("Quit successfully");
                    if (gui != null) {
                        gui.dispose();
                    }
                    System.exit(0);
                    break;
                case "help":
                    showHelp();
                    break;
                default:
                    System.out.println("Unknown command: " + word);
            }

            if (valid) {
                execommandHistory.add(trimmedCommand);
                refreshDisplay();
            }

        } catch (Exception e) {
            System.out.println("Error executing command: " + e.getMessage());
        }
        return valid;
    }


    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("  rectangle <name> <x> <y> <width> <height>");
        System.out.println("  line <name> <x1> <y1> <x2> <y2>");
        System.out.println("  circle <name> <x> <y> <radius>");
        System.out.println("  square <name> <x> <y> <side>");
        System.out.println("  move <shapeName> <dx> <dy>");
        System.out.println("  shapeat <x> <y>");
        System.out.println("  undo");
        System.out.println("  redo");
        System.out.println("  help");
        System.out.println("  quit");
    }
    public void refreshDisplay() {
        if (gui != null) {
            gui.refreshGraphics();
        }
    }
    public void runCLI(){
        System.out.println("-------Clevis-------");
        Scanner scanner = new Scanner(System.in);
        System.out.println("CLEVIS TOOL IS READY!");
        System.out.println("Please enter command");
        String command, word;
        boolean valid;
        while (true) {
            System.out.print("clevis>");
            valid=false;
            if (scanner.hasNextLine()) {
                command = scanner.nextLine().trim();
                String[] com_list = command.split(" ", 2);
                word = com_list[0];
                switch (word.toLowerCase()) {
                    case "rectangle":
                        valid=Rectangle.createRectangle(command);
                        break;
                    case "line":
                        valid=Line.createLine(command);
                        break;
                    case "circle":
                        valid=Circle.createCircle(command);
                        break;
                    case "square":
                        valid=Square.createSquare(command);
                        break;
                    case "delete":
                        valid= CLI.deleteShape(command);
                        break;
                    case "move":
                        valid=CLI.move(command);
                        break;
                    case "shapeat":
                        CLI.shapeAt(command);
                        break;
                    case "undo":
                        List<String> history = getExecommandHistory();
                        if (history == null || history.isEmpty()) {
                            System.out.println("No operation to undo");
                            break;
                        }
                        if(command.equalsIgnoreCase("undo")) {
                            int l= getExecommandHistory().size() - 1;
                            while(l>=0&&(getExecommandHistory().get(l).equals("undo")||getExecommandHistory().get(l).equals("redo"))){l--;}
                            if (l>=0) {
                                String c = getExecommandHistory().get(l);
                                    CLI.undo(c);
                                    getExecommandHistory().remove(l);
                                    getUndoHistory().add(c);
                                    getExecommandHistory().add("undo");
                                    break;
                            }
                        }
                        break;
                    case "redo":
                        if(command.equalsIgnoreCase("redo")&&getExecommandHistory()!=null) {
                            int l= getExecommandHistory().size() - 1;
                            if (l>=0) {
                                String c = getExecommandHistory().get(l);
                                if (!Objects.equals(c, "undo")&&!Objects.equals(c,"redo")) {
                                    System.out.println("No operation to redo");
                                    break;
                                }
                                if (getUndoHistory() != null) {
                                    CLI.redo(getUndoHistory().getLast());
                                    getExecommandHistory().remove(l);
                                    getExecommandHistory().add(getUndoHistory().getLast());
                                    getUndoHistory().removeLast();
                                    valid=true;
                                }
                            }
                            if (l<0) {
                                System.out.println("No operation to redo");
                            }
                        }
                        break;
                    case "help":
                        showHelp();
                        break;
                    case "quit":
                        scanner.close();
                        System.out.println("Quit successfully");
                        return;
                    default:
                        System.out.println("Unknown command!");
                }
            } else {
                scanner.close();
                return;
            }
            if(!command.isEmpty()){
                commandHistory.add(command);
            }
            if(valid){
                execommandHistory.add(command);
            }
        }
    }


    public void setGUI(GUI gui) {
        this.gui = gui;
    }
}