package com.github.berthy.utils.util;

/**
 *
 * @author Bertrand COTE
 */
public class Arrays {
    
    /**
     * 
     * @param a
     * @return the minimum value in the array a
     */
    public static double min( double[] a ) {
        
        if( a == null || a.length == 0 )
            throw new IllegalArgumentException( "null or empty argument");
        
        double min = a[0];
        for( int i=1; i<a.length; i++ )
            if( min > a[i] )
                min = a[i];
        
        return min;
    }
    
    /**
     * 
     * @param a
     * @return the minimum value in the array a
     */
    public static double max( double[] a ) {
        
        if( a == null || a.length == 0 )
            throw new IllegalArgumentException( "null or empty argument");
        
        double max = a[0];
        for( int i=1; i<a.length; i++ )
            if( max < a[i] )
                max = a[i];
        
        return max;
    }
    
}
