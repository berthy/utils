package com.github.berthy.utils.math.intervals;

import com.github.berthy.utils.math.Arithmetic;
import com.github.berthy.utils.util.Arrays;

/**
 * Interval arithmetic.
 * 
 * A method developed by mathematicians since the 1950s and 1960s as an approach 
 * to putting bounds on rounding errors and measurement errors in mathematical 
 * computation and thus developing numerical methods that yield reliable results.
 * 
 * https://en.wikipedia.org/wiki/Interval_arithmetic
 * 
 * Here Intervals are unmutable objects.
 * 
 * @author Bertrand COTE
 */

public class Interval implements Arithmetic {
    
    /**
     * Margin of error on isZero and isOne methods
     */
    private static double epsilon;
    private static boolean epsilonIsSet;
    static {
        Interval.resetEpsilon();
    }
    
    // =========================================================================
    // ========== static constants =============================================
    // =========================================================================
    
    /**
     * Neutral element for add and subtract operator.
     */
    public static final Interval ZERO = new Interval( 0., 0. );
    /**
     * Neutral element for mult and div operator.
     */
    public static final Interval ONE = new Interval( 1., 1. );
    
    // =========================================================================
    // ========== class variables ==============================================
    // =========================================================================
    
    /**
     * Interval's minimum and maximum bounds.
     * The property this.min <= this.max is always true.
     */
    private final double minBound, maxBound;
    
    // =========================================================================
    // ========== Constructors =================================================
    // =========================================================================
    
    /**
     * Constructor for the [ 0., 0. ] interval.
     */
    public Interval() {
        this( 0., 0. );
    }
    
    /**
     * Constructor for the [val, val] interval.
     * 
     * @param val min and max values
     */
    public Interval( double val ) {
        this( val, val );
    }
    
    /**
     * Main constructor.
     * 
     * @param minBound one bound of the interval
     * @param maxBound the other bound of the interval
     */
    public Interval( double minBound, double maxBound ) {
        // the condition this.min <= this.max must be true.
        if( maxBound < minBound ) {
            this.minBound = maxBound;
            this.maxBound = minBound;
        } else {
            this.minBound = minBound;
            this.maxBound = maxBound;
        }
    }
    
    // =========================================================================
    // ========== Getters ======================================================
    // =========================================================================
    
    /**
     * @return the interval's minimum bound
     */
    public double getMinBound() {
        return minBound;
    }

    /**
     * @return the interval's maximum bound
     */
    public double getMaxBound() {
        return maxBound;
    }
    
    /**
     * Check if zero is not element of the interval.
     * 
     * @return true if zero in the interval
     */
    public boolean zeroElementOfThis() {
        return this.minBound<=0. && this.maxBound>=0.;
    }
    
    // =========================================================================
    // ========== Arithmetic interval plus operations with doubles =============
    // =========================================================================
    
    // ---------- zero ----------
    
    @Override
    public Interval zero() {
        return ZERO;
    }
    
    @Override
    public boolean isZero() { // Both min and max must be zero.
        if( Interval.epsilonIsSet ) {
            return     (this.minBound>=-Interval.epsilon && this.minBound<=Interval.epsilon) 
                    && (this.maxBound>=-Interval.epsilon && this.maxBound<=Interval.epsilon);
        } else {
            return (this.minBound==0.) && (this.maxBound==0.);
        }
    }
    
    // ---------- one ----------
    
    @Override
    public Interval one() {
        return ONE;
    }
    
    @Override
    public boolean isOne() { // Both min and max must be one.
        if( Interval.epsilonIsSet ) {
            return     (this.minBound>=1.-Interval.epsilon && this.minBound<=1.+Interval.epsilon) 
                    && (this.maxBound>=1.-Interval.epsilon && this.maxBound<=1.+Interval.epsilon);
        } else {
            return (this.minBound==1.) && (this.maxBound==1.);
        }
    }
    
    // ---------- width, rad, mid, mig ----------
    
    /**
     * Computes the intervals's width. (distance between the two bounds)
     * 
     * @return the width of the interval
     */
    public double width() {
        return this.maxBound - this.minBound;
    }
    
    /**
     * Computes the intervals's radius. (distance between de middle and bounds)
     * 
     * @return the radius of the interval
     */
    public double rad() {
        return this.width()/2.;
    }
    
    /**
     * Computes the intervals's middle.
     * 
     * @return the middle of the interval.
     */
    public double mid() {
        return (this.minBound+this.maxBound)/2.;
    }
    
    /**
     * Computes the intervals's mignitude.
     * (The smallest absolute value of the elements of the interval. There is no 
     * typo, this is the correct spelling: M-I-G-N-I-T-U-D-E)
     * 
     * @return the smallest absolute value of the elements of the interval
     */
    public double mig() {
        if( !this.zeroElementOfThis() ) {
            return Math.min(Math.abs(this.minBound), Math.abs(this.maxBound) );
        }
        return 0.;
        
    }
    
    // ---------- add ----------
    
    /**
     * Addition.
     * 
     * [a, b] + [c, d] = [a + c, b + d]
     * The addition and multiplication operations are commutative, 
     * associative and sub-distributive: the set X ( Y + Z ) is a subset 
     * of XY + XZ.
     * 
     * @param other addition
     * @return added interval
     */
    @Override
    public Interval add( Arithmetic other ) {
        Interval otherInterval = (Interval)other;
        return new Interval( this.getMinBound()+otherInterval.getMinBound(), this.getMaxBound()+otherInterval.getMaxBound() );
    }
    
    /**
     * Addition with simple real.
     * 
     * @param d addition
     * @return added interval
     */
    public Interval add( double d ) {
        return new Interval( this.getMinBound()+d, this.getMaxBound()+d );
    }
    
    // ---------- subtract ----------
    
    /**
     * Subtraction.
     * 
     * [a, b] − [c, d] = [a − d, b − c]
     * 
     * @param other subtract
     * @return subtracted interval
     */
    @Override
    public Interval subtract( Arithmetic other ) {
        Interval otherInterval = (Interval)other;
        return new Interval( this.getMinBound()-otherInterval.getMaxBound(), this.getMaxBound()-otherInterval.getMinBound() );
    }
    
    /**
     * Subtraction with simple real.
     * 
     * @param d subtract
     * @return subtracted interval
     */
    public Interval subtract( double d ) {
        return new Interval( this.getMinBound()-d, this.getMaxBound()-d );
    }
    
    // ---------- mult ----------
    
    /**
     * Multiplication.
     * 
     * [a, b] × [c, d] = [min (a × c, a × d, b × c, b × d), max (a × c, a × d, b × c, b × d)]
     * The addition and multiplication operations are commutative, 
     * associative and sub-distributive: the set X ( Y + Z ) is a subset 
     * of XY + XZ.
     * 
     * @param other multiplier
     * @return multiplied interval
     */
    @Override
    public Interval mult( Arithmetic other ) {
        Interval otherInterval = (Interval)other;
        double[] bounds = new double[]{ this.getMinBound()*otherInterval.getMinBound(), 
                                        this.getMinBound()*otherInterval.getMaxBound(), 
                                        this.getMaxBound()*otherInterval.getMinBound(), 
                                        this.getMaxBound()*otherInterval.getMaxBound() };
        return new Interval( Arrays.min( bounds ), Arrays.max( bounds ) );
    }
    
    /**
     * Multiplication by simple real.
     * 
     * @param d multiplier
     * @return multiplied interval
     */
    public Interval mult( double d ) {
        return new Interval( this.getMinBound()*d, this.getMaxBound()*d );
    }
    
    // ---------- divide ----------
    
    /**
     * Division.
     * 
     * [a, b] ÷ [c, d] = [min (a ÷ c, a ÷ d, b ÷ c, b ÷ d), max (a ÷ c, a ÷ d, b ÷ c, b ÷ d)] 
     * when 0 is not in [c, d].
     * 
     * @param other division
     * @return divided interval
     */
    @Override
    public Interval divide( Arithmetic other ) {
        Interval otherInterval = (Interval)other;
        if( !otherInterval.zeroElementOfThis() ) {
            double[] bounds = new double[]{ this.getMinBound()/otherInterval.getMinBound(), 
                                            this.getMinBound()/otherInterval.getMaxBound(), 
                                            this.getMaxBound()/otherInterval.getMinBound(), 
                                            this.getMaxBound()/otherInterval.getMaxBound() };
            return new Interval( Arrays.min( bounds ), Arrays.max( bounds ) );
        } else {
            throw new ArithmeticException( "Divide by zero exception.(zero is included in the interval)");
        }
    }
    
    /**
     * Division by simple real.
     * 
     * @param d division
     * @return divided interval
     */
    public Interval divide( double d ) {
        if( d == 0. )
            throw new ArithmeticException( "Divide by zero exception.");
        return new Interval( this.getMinBound()/d, this.getMaxBound()/d );
    }
    
    // ---------- sq, sqrt, pow ----------
    
    /**
     * Square function
     * 
     * WARNING: In intervals arithmetic: this.sq() != this.mult(this)
     * 
     * @return squared interval
     */
    public Interval sq() {
        
        if ( !zeroElementOfThis() ) {
            return new Interval( this.minBound*this.minBound, this.maxBound*this.maxBound );
        } else {
            return new Interval( 0., Math.max(this.minBound*this.minBound, this.maxBound*this.maxBound));
        }
    }
    
    /**
     * Square root function
     * 
     * @return square interval
     */
    public Interval sqrt() {
        return new Interval( Math.sqrt(this.minBound), Math.sqrt(this.maxBound) );
    }
    
    /**
     * Compute this^n with n integer.
     * 
     * @param n power
     * @return pow
     */
    @Override
    public Interval pow( int n ) {
        
        if( n<0 && this.zeroElementOfThis() ) {
            throw new ArithmeticException( "Divide by zero exception.(zero is included in the interval)");
        }
        
        if( n==0 ) {
            return ONE;
        }
        
        Interval result;
        if( ((n&1) == 0) && this.zeroElementOfThis() ) { // (n is even) and (0 !∈ this)
            result = new Interval( 0., 
                                   Math.max(Math.pow(minBound, n), 
                                             Math.pow(maxBound, n) )
                                  );
        } else {
            result = new Interval( Math.pow(minBound, n), Math.pow(maxBound, n) );
        }
        
        return result;
    }
    
    // ---------- intersection and union ----------
    
    /**
     * Check if the interval is a subset of other.
     * 
     * @param other compare interval
     * @return true if this interval is a subset
     */
    public boolean isSubsetOf( Interval other ) {
        return other.minBound<=this.minBound && other.maxBound>=this.maxBound;
    }
    
    /**
     * Checks if this interval have a common range with other.
     * 
     * @param other
     * @return true if ther is a common range.
     */
    public boolean intersects( Interval other ) {
        final double maxOfMins = Math.max( this.minBound, other.minBound );
        final double minOfMaxs = Math.min( this.maxBound, other.maxBound );
        return maxOfMins <= minOfMaxs;
    }
    
    
    
    public Interval intersection( Interval other ) {
        if( this.intersects(other) ) {
            final double min = Math.max( this.minBound, other.minBound );
            final double max = Math.min( this.maxBound, other.maxBound );
            return new Interval( min, max );
        }
        return null;
    }
    
    public Interval union( Interval other ) {
        if( this.intersects(other) ) {
            final double min = Math.min( this.minBound, other.minBound );
            final double max = Math.max( this.maxBound, other.maxBound );
            return new Interval( min, max );
        }
        return null;
    }
    
    // =========================================================================
    // ========== hashCode and equals ==========================================
    // =========================================================================

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.minBound) ^ (Double.doubleToLongBits(this.minBound) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.maxBound) ^ (Double.doubleToLongBits(this.maxBound) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Interval other = (Interval) obj;
        if (Double.doubleToLongBits(this.minBound) != Double.doubleToLongBits(other.minBound)) return false;
        if (Double.doubleToLongBits(this.maxBound) != Double.doubleToLongBits(other.maxBound)) return false;
        return true;
    }
    
    // =========================================================================
    // ========== Utils ========================================================
    // =========================================================================
    
    /**
     * String representation of the interval.
     * 
     * @return the string representation of the interval.
     */
    @Override
    public String toString() {
        return "[ " + this.minBound + ", " + this.maxBound + " ]";
    }
    
    // =========================================================================
    // ========== Static methods ===============================================
    // =========================================================================
    
    
    /**
     * Sets the margin of error on isZero and isOne methods
     * 
     * @param epsilon tolerance
     */
    public static void setEpsilon( double epsilon ) {
        if( epsilon>=0.) {
            Interval.epsilon = epsilon;
        } else {
            Interval.epsilon = -epsilon;
        }
        Interval.epsilonIsSet = true;
    }
    
    public static void resetEpsilon() {
        Interval.epsilon = 0.;
        Interval.epsilonIsSet = false;
    }
    
}