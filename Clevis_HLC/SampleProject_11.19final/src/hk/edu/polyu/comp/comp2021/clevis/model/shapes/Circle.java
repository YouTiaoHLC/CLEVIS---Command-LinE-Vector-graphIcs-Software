package hk.edu.polyu.comp.comp2021.clevis.model.shapes;

import hk.edu.polyu.comp.comp2021.clevis.model.CLI;


/**
 * A class to store shape type circle.
 */
public class Circle extends Shape {
    public static final double DOUBLE = 0.05;
    private String name;
    private double x;
    private double y;
    private double radius;

    public Circle(String name, double x, double y, double r){
        this.setName(name);
        this.setX(x);
        this.setY(y);
        setRadius(r);
    }
    public static boolean createCircle(String command) {
        String[] com = command.split(" ");
        if (com.length!=5) {
            System.out.println("Command length error!Please check 'help' for correct commands");
            return false;
        }
        String name=com[1];
        double[] parameters=new double[3];
        //检查指令能否被转换成double
        try {
            for (int i = 2; i < 5; i++) {
                parameters[i - 2] = Double.parseDouble(com[i]);
            }
        } catch (NumberFormatException e) {
            System.out.println("Command error in Parameters!");
            return false;
        }

        if(parameters[2]<=0){
            System.out.println("Command error in Parameters!The radius cannot be less than 0!");
            return false;
        }

        if (CLI.getShapes_map().containsKey(name)) {
            System.out.println("Command error, name exists!");
            return false;
        }

        Circle circle = new Circle(name, parameters[0],parameters[1],parameters[2]);
        CLI.addShape(name,circle);
        return true;
    }
    @Override
    public String getName(){return this.name;}
    public double getX(){return this.x;}
    public double getY(){return this.y;}
    public double getRadius(){return this.radius;}
    @Override
    public void move(double x, double y){
        this.setX(this.getX() + x);
        this.setY(this.getY() + y);
    }
    @Override
    public boolean shapeAt(double x,double y) {
        double distance=Math.sqrt(Math.pow(this.getX()-x,2) + Math.pow(this.getY()-y,2));
        distance=Math.abs(distance-this.getRadius());
        return distance< DOUBLE;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
