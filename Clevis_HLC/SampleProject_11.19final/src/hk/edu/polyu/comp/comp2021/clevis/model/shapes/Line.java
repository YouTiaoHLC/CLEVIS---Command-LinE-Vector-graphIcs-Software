package hk.edu.polyu.comp.comp2021.clevis.model.shapes;

import hk.edu.polyu.comp.comp2021.clevis.model.CLI;

/**
 * A class to store shape type line.
 */
public class Line extends Shape{
    public static final double DOUBLE = 0.05;
    private String name;
    private double x1;
    private double y1;
    private double x2;
    private double y2;

    public Line(String name, double x1, double y1, double x2, double y2) {
        this.setName(name);
        this.setX1(x1);
        this.setY1(y1);
        this.setX2(x2);
        this.setY2(y2);
    }

    public static boolean createLine(String command) {
        String[] com = command.split(" ");
        if (com.length != 6) {
            System.out.println("Command error!Please check 'help' for correct commands");
            return false;
        }
        String name = com[1];
        double[] parameters = new double[4];
        //检查指令能否被转换成double
        try {
            for (int i = 2; i < 6; i++) {
                parameters[i - 2] = Double.parseDouble(com[i]);
            }
        } catch (NumberFormatException e) {
            System.out.println("Command error in Parameters!");
            return false;
        }

        if(parameters[0]==parameters[2]&&parameters[1]==parameters[3]){
            System.out.println("Command error in Parameters!The length of line cannot be less than 0!");
            return false;
        }

        if (CLI.getShapes_map().containsKey(name)) {
            System.out.println("Command error, name exists!");
            return false;
        }

        Line line = new Line(name, parameters[0], parameters[1], parameters[2], parameters[3]);
        CLI.addShape(name, line);
        return true;
    }
    @Override
    public String getName(){return this.name;}
    public double getX1(){return this.x1;}
    public double getX2(){return this.x2;}
    public double getY1(){return this.y1;}
    public double getY2(){return this.y2;}
    @Override
    public void move(double x, double y){
        this.setX1(this.getX1() + x);
        this.setX2(this.getX2() + x);
        this.setY1(this.getY1() + y);
        this.setY2(this.getY2() + y);
    }
    @Override
    public boolean shapeAt(double x,double y){
        double distance=CLI.calculatePtoL(x,y,this.getX1(),this.getY1(),this.getX2(),this.getY2());
        return Math.abs(distance)< DOUBLE;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }
}