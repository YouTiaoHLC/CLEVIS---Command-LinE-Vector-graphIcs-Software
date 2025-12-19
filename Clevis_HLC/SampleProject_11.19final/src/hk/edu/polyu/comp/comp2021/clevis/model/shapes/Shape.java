package hk.edu.polyu.comp.comp2021.clevis.model.shapes;

/**
 * The super class of all types of shape.
 */
public abstract class Shape {
    public abstract String getName();
    public abstract void move(double x,double y);
    public abstract boolean shapeAt(double x,double y);
    }
