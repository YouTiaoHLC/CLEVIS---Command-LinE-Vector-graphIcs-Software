package hk.edu.polyu.comp.comp2021.clevis.model.shapes;

import hk.edu.polyu.comp.comp2021.clevis.model.CLI;

/**
 * A class to store shape type rectangle.
 */
public class Rectangle extends Shape{
    public static final double DOUBLE = 0.05;
    private String name;
    private double x;
    private double y;
    private double width;
    private double height;
    public Rectangle(String name, double x, double y, double w, double h){
        this.setName(name);
        this.setX(x);
        this.setY(y);
        setWidth(w);
        setHeight(h);
    }
    public static boolean createRectangle(String command) {
        String[] com = command.split(" ");
        if (com.length != 6) {
            System.out.println("Command error!Please check 'help' for correct commands");
            return false;
        }
        String name = com[1];
        double[] parameters = new double[4];
        try {
            for (int i = 2; i < 6; i++) {
                parameters[i - 2] = Double.parseDouble(com[i]);
            }
        } catch (NumberFormatException e) {
            System.out.println("Command error in Parameters!");
            return false;
        }

        if(parameters[2]<=0||parameters[3]<=0){
            System.out.println("Command error in Parameters!The length or width cannot be less than 0!");
            return false;
        }

        if (CLI.getShapes_map().containsKey(name)) {
            System.out.println("Command error, name exists!");
            return false;
        }

        Rectangle rect = new Rectangle(name, parameters[0],parameters[1],parameters[2],parameters[3]);
        CLI.addShape(name,rect);
        return true;
    }
    @Override
    public String getName(){return this.name;}
    public double getX(){return this.x;}
    public double getY(){return this.y;}
    public double getW(){return this.getWidth();}
    public double getH(){return this.getHeight();}
    @Override
    public void move(double x, double y){
        this.setX(this.getX() + x);
        this.setY(this.getY() + y);
    }
    @Override
    public boolean shapeAt(double x,double y){
        double x1=this.getX();
        double y1=this.getY();
        double x2=x1+this.getW();
        double y3=y1-this.getH();
        if(CLI.calculatePtoL(x,y,x1,y1,x2, y1)<DOUBLE) return true;
        if(CLI.calculatePtoL(x,y,x2, y1, x2,y3)< DOUBLE) return true;
        if(CLI.calculatePtoL(x,y, x1, y3, x2,y3)< DOUBLE) return true;
        return CLI.calculatePtoL(x,y,x1,y1, x1, y3)< DOUBLE;
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

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
