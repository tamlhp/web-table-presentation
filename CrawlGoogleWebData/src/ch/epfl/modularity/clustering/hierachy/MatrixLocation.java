/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.epfl.modularity.clustering.hierachy;

/**
 *
 * @author kubin
 */
public class MatrixLocation {
    private static MatrixLocation instance = new MatrixLocation();
    
    public int row;
    public int col;
    
    public MatrixLocation(){
        this(0,0);
    }
    
    public MatrixLocation(int row, int col){
        this.row = row;
        this.col = col;
    }
    
    public MatrixLocation set(int row, int col){
        this.row = row;
        this.col = col;
        return this;
    }
    
    @Override
    public int hashCode(){
//        long bits = java.lang.Double.doubleToLongBits(col);
//        bits ^= java.lang.Double.doubleToLongBits(row) * 31;
//        return (((int) bits) ^ ((int) (bits >> 32)));
        
        return row * 10000 + col;
    }
    
    public static MatrixLocation getInstance(){
        return instance;
    }
    
    public static MatrixLocation getInstance(int row, int col){
        return instance.set(row, col);
    }
    
    public String toString(){
        return "[" + row + "," + col + "]" + hashCode();
    }
    
    public static void main(String []args){
        int a = getInstance(2, 36).hashCode();
        int b = getInstance(30, 32).hashCode();
        int c = getInstance(36, 2).hashCode();
        int d = getInstance(36, 30).hashCode();
        
        System.out.println(a+ " " + b + " " + c + " " + d);
        System.out.println((new Integer(a)).hashCode() + "  " + (new Integer(b)).hashCode() + "  " );
    }
}
