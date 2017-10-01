package com.github.berthy.utils.math.intervals;

import com.github.berthy.utils.math.Arithmetic;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Bertrand COTE
 */
public class GenralizedInterval implements Arithmetic {
    
    // =========================================================================
    // ========== static constants =============================================
    // =========================================================================
    
    /**
     * Neutral element for add and subtract operator.
     */
    public static final GenralizedInterval ZERO = new GenralizedInterval( Interval.ZERO );
    /**
     * Neutral element for mult and div operator.
     */
    public static final GenralizedInterval ONE = new GenralizedInterval( Interval.ONE );
    
    // =========================================================================
    // ========== class variables ==============================================
    // =========================================================================
    
    private List<Interval> intervals;
    
    // =========================================================================
    // ========== Constructors =================================================
    // =========================================================================
    
    public GenralizedInterval() {
        this.intervals = new ArrayList<>();
    }
    
    public GenralizedInterval( Interval interval ) {
        this();
        this.intervals.add( interval );
    }
    
    public GenralizedInterval( double minBound, double maxBound ) {
        this( new Interval( minBound, maxBound ) );
    }
    
    /**
     * WARNING: this "intervals" argument list must be sorted (see sort() method)
     * 
     * @param intervals (must be sorted)
     */
    public GenralizedInterval( List<Interval> intervals ) {
        this();
        this.intervals.addAll( intervals );
    }
    
    // =========================================================================
    // ========== put and remove intervals =====================================
    // =========================================================================
    
    /**
     * Put a new interval into the the generalized interval.
     * If the new interval intersects others intervals, it is united to those ones.
     * 
     * @param interval 
     * @return  
     */
    public GenralizedInterval put( Interval interval ) {
        
        if( interval == null )
            return this;
        
        if( this.intervals.isEmpty() )
            return new GenralizedInterval( interval );
        
        List<Interval> result = new ArrayList<>();
        int i = 0;
        
        for( ; i<this.intervals.size(); i++ ) {
            if(    interval.getMaxBound() < this.intervals.get( i ).getMinBound()
                || interval.intersects( this.intervals.get( i ) ) 
                ) {
                break;
            }
        }
        
        result.addAll( this.intervals.subList( 0, i ) );
        
        for( ; i<this.intervals.size(); i++ ) {
            if( interval.intersects( this.intervals.get( i ) ) ) {
                interval = interval.union( this.intervals.get( i ) );
            } else {
                break;
            }
        }
        
        result.add( interval );
        
        result.addAll( this.intervals.subList( i, this.intervals.size() ) );
        
        return new GenralizedInterval( result );
    }
    
    /**
     * Remove an interval form tthe generalized interval.
     * 
     * @param interval
     * @return 
     */
    public GenralizedInterval remove( Interval interval ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // =========================================================================
    // ========== union and intersection =======================================
    // =========================================================================
    
    public GenralizedInterval union( GenralizedInterval other ) {
        
        if( other == null )
            return this;
        
        GenralizedInterval result = this;
        
        for( Interval interval : other.intervals )
            result = result.put( interval );
        
        return result;
    }
    
    public GenralizedInterval intersection( GenralizedInterval other ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // =========================================================================
    
    /**
     * Sorts all intervals (based on the minBound value)
     * 
     */
    private void sort() {
        this.intervals.sort( 
                (Object o1, Object o2) -> Double.compare( ((Interval)o1).getMinBound(), ((Interval)o2).getMinBound() )
        );
    }

    // =========================================================================
    // ========== Arithmetic ===================================================
    // =========================================================================
    
    // ---------- add ----------
    
    @Override
    public Arithmetic add( Arithmetic other ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Arithmetic add( double d ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // ---------- subtract ----------

    @Override
    public Arithmetic subtract( Arithmetic other ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Arithmetic subtract( double d ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // ---------- mult ----------

    @Override
    public Arithmetic mult( Arithmetic other ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Arithmetic mult( double d ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // ---------- divide ----------

    @Override
    public Arithmetic divide( Arithmetic other ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Arithmetic divide( double d ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // ---------- zero ----------

    @Override
    public Arithmetic zero() {
        return GenralizedInterval.ZERO;
    }

    @Override
    public boolean isZero() {
        return this.equals( GenralizedInterval.ZERO );
    }
    
    // ---------- one ----------

    @Override
    public Arithmetic one() {
        return GenralizedInterval.ONE;
    }

    @Override
    public boolean isOne() {
        return this.equals( GenralizedInterval.ONE );
    }
    
    // ---------- pow ----------

    @Override
    public Arithmetic pow( int n ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // =========================================================================
    // ========== hashCode and equals ==========================================
    // =========================================================================

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.intervals);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        
        final GenralizedInterval other = (GenralizedInterval) obj;
        if( this.intervals.size() != other.intervals.size() )
            return false;
        for( int i=0; i<this.intervals.size(); i++ ) {
            if( !this.intervals.get(i).equals( other.intervals.get(i) ) )
                return false;
        }
        
        return true;
    }
    
    // =========================================================================
    // ========== Uitls ========================================================
    // =========================================================================
    
    /**
     * String representation of the interval.
     * 
     * @return the genralizedInterval's string representation.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append( "{ " );
        for( int i=0; i<this.intervals.size(); i++ ) {
            sb.append( this.intervals.get(i) );
            if( i<this.intervals.size()-1 )
                sb.append( ", " );
        }
        sb.append( " }" );
        
        return sb.toString();
    }
    
}
