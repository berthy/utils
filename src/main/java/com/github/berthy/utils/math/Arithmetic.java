package com.github.berthy.utils.math;

/**
 * Arithmetic operations.
 * 
 * @author Bertrand COTE
 */
public interface Arithmetic {

    /**
     * Addition.
     * @param other value
     * @return this + other
     */
    Arithmetic add( Arithmetic other );
    
    /**
     * Subtraction.
     * @param other value
     * @return this - other
     */
    Arithmetic subtract( Arithmetic other );
    
    /**
     * Multiplication.
     * @param other value
     * @return this * other
     */
    Arithmetic mult( Arithmetic other );
    
    /**
     * Division.
     * @param other value
     * @return this / other
     */
    Arithmetic divide( Arithmetic other );
    
    /**
     * Zero: neutral element for additions.
     * @return zero element
     */
    Arithmetic zero();

    /**
     * Test if this is zero.
     * @return true if this is zero
     */
    boolean isZero();
    
    /**
     * One: neutral element for multiplications.
     * @return one element
     */
    Arithmetic one();
    
    /**
     * Test if this is one.
     * @return true if this is one
     */
    boolean isOne();
    
    /**
     * Power.
     * @param n power
     * @return this ^ n
     */
    Arithmetic pow( int n );
    
}