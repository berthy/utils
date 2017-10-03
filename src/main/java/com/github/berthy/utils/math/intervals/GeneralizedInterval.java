package com.github.berthy.utils.math.intervals;

import com.github.berthy.utils.math.Arithmetic;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Bertrand COTE
 */
public class GeneralizedInterval implements Arithmetic {
    
    // =========================================================================
    // ========== static constants =============================================
    // =========================================================================
    
    /**
     * Neutral element for add and subtract operator.
     */
    public static final GeneralizedInterval ZERO = new GeneralizedInterval( Interval.ZERO );
    /**
     * Neutral element for mult and div operator.
     */
    public static final GeneralizedInterval ONE = new GeneralizedInterval( Interval.ONE );
    
    // =========================================================================
    // ========== class variables ==============================================
    // =========================================================================
    
    private List<Interval> intervals;
    
    // =========================================================================
    // ========== Constructors =================================================
    // =========================================================================
    
    public GeneralizedInterval() {
        this.intervals = new ArrayList<>();
    }
    
    public GeneralizedInterval( Interval interval ) {
        this();
        this.intervals.add( interval );
    }
    
    public GeneralizedInterval( double minBound, double maxBound ) {
        this( new Interval( minBound, maxBound ) );
    }
    
    /**
     * WARNING: this "intervals" argument list must be sorted (see sort() method)
     * 
     * @param intervals (must be sorted)
     */
    public GeneralizedInterval( List<Interval> intervals ) {
        this();
        this.intervals.addAll( intervals );
        GeneralizedInterval.compactIntervals( this.intervals );
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
    public GeneralizedInterval put( Interval interval ) {
        
        if( interval == null )
            return this;
        
        if( this.intervals.isEmpty() )
            return new GeneralizedInterval( interval );
        
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
        
        return new GeneralizedInterval( result );
    }
    
    /**
     * Remove an interval form tthe generalized interval.
     * 
     * @param interval
     * @return 
     */
    public GeneralizedInterval remove( Interval interval ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // =========================================================================
    // ========== union and intersection =======================================
    // =========================================================================
    
    public GeneralizedInterval union( GeneralizedInterval other ) {
        
        if( other == null )
            return this;
        
        GeneralizedInterval result = this;
        
        for( Interval interval : other.intervals )
            result = result.put( interval );
        
        return result;
    }
    
    public GeneralizedInterval intersection( GeneralizedInterval other ) {
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
    
    public static void compactIntervals( List<Interval> list ) {
        
        list.sort( 
            (Object o1, Object o2) -> 
                Double.compare( ((Interval)o1).getMinBound(), ((Interval)o2).getMinBound() )
        );
        
        int i = 0;
         while( i<list.size()-1 ) {
            
            while( i<list.size()-1 && list.get( i ).intersects( list.get(i+1) ) ) {
                Interval united = list.get( i ).union( list.get(i+1) );
                list.set( i, united );
                list.remove( i+1 );
            }
            
            i++;
        }
    }

    // =========================================================================
    // ========== Arithmetic ===================================================
    // =========================================================================
    
    // ---------- add ----------
    
    @Override
    public Arithmetic add( Arithmetic other ) {
        
        if( other.isZero() )
            return this;
        
        List<Interval> resultIntervals = new ArrayList<>();
        
        if( other instanceof GeneralizedInterval ) {
            
            GeneralizedInterval otherGeneralized = (GeneralizedInterval)other;
            
            for( Interval intervalThis : this.intervals ) {
                for( Interval intervalOther : otherGeneralized.intervals ) {
                    resultIntervals.add( intervalThis.add( intervalOther ) );
                }
            }
            
        } else if( other instanceof Interval ) {
            
            Interval otherInterval = (Interval)other;
            for( Interval interval : this.intervals )
                resultIntervals.add( interval.add( otherInterval ) );
            
        } else {
            
            throw new IllegalArgumentException( "other is not instance of Interval or GeneralizedInterval" );
        }
        
        return new GeneralizedInterval( resultIntervals );
    }
    
    public Arithmetic add( double d ) {
        
        if( d == 0. )
            return this;
        
        List<Interval> resultIntervals = new ArrayList<>();
        for( Interval interval : this.intervals ) {
            resultIntervals.add( interval.add( d ) );
        }
        return new GeneralizedInterval( resultIntervals );
    }
    
    // ---------- subtract ----------

    @Override
    public Arithmetic subtract( Arithmetic other ) {
        
        if( other.isZero() )
            return this;
        
        List<Interval> resultIntervals = new ArrayList<>();
        for( Interval interval : this.intervals ) {
            resultIntervals.add( interval.subtract( other ) );
        }
        return new GeneralizedInterval( resultIntervals );
    }
    
    public Arithmetic subtract( double d ) {
        
        if( d == 0. )
            return this;
        
        List<Interval> resultIntervals = new ArrayList<>();
        for( Interval interval : this.intervals ) {
            resultIntervals.add( interval.subtract( d ) );
        }
        return new GeneralizedInterval( resultIntervals );
    }
    
    // ---------- mult ----------

    @Override
    public Arithmetic mult( Arithmetic other ) {
        
        if( other.isOne() )
            return this;
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Arithmetic mult( double d ) {
        
        if( d == 1. )
            return this;
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // ---------- divide ----------

    @Override
    public Arithmetic divide( Arithmetic other ) {
        
        if( other.isOne() )
            return this;
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Arithmetic divide( double d ) {
        
        if( d == 1. )
            return this;
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // ---------- zero ----------

    @Override
    public Arithmetic zero() {
        return GeneralizedInterval.ZERO;
    }

    @Override
    public boolean isZero() {
        return this.equals(GeneralizedInterval.ZERO );
    }
    
    // ---------- one ----------

    @Override
    public Arithmetic one() {
        return GeneralizedInterval.ONE;
    }

    @Override
    public boolean isOne() {
        return this.equals(GeneralizedInterval.ONE );
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
        
        final GeneralizedInterval other = (GeneralizedInterval) obj;
        if( this.intervals.size() != other.intervals.size() )
            return false;
        for( int i=0; i<this.intervals.size(); i++ ) {
            if( !this.intervals.get(i).equals( other.intervals.get(i) ) )
                return false;
        }
        
        return true;
    }
    
    // =========================================================================
    // ========== Utils ========================================================
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
