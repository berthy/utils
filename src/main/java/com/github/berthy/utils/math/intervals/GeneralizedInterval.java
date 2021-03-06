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
     * 
     * @param intervals
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
        
        List<Interval> resultIntervals = new ArrayList<>();
        
        resultIntervals.addAll( this.intervals );
        resultIntervals.add( interval );
        
        return new GeneralizedInterval( resultIntervals );
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
        
        List<Interval> resultIntervals = new ArrayList<>();
        
        resultIntervals.addAll( this.intervals );
        resultIntervals.addAll( other.intervals );
        
        return new GeneralizedInterval( resultIntervals );
    }
    
    public GeneralizedInterval intersection( GeneralizedInterval other ) {
        
        if( other == null )
            return null;
        
        List<Interval> resultIntervals = new ArrayList<>();
        
        this.intervals.stream().forEach((Interval intervalThis) -> {
            other.intervals.stream().forEach((Interval intervalOther) -> {
                Interval inter = intervalThis.intersection( intervalOther );
                if( inter != null) {
                    resultIntervals.add(inter);
                }
            });
        });
        
        return new GeneralizedInterval( resultIntervals );
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
            
            this.intervals.stream().forEach((Interval intervalThis) -> {
                otherGeneralized.intervals.stream().forEach((Interval intervalOther) -> {
                    resultIntervals.add( intervalThis.add( intervalOther ) );
                });
            });
            
        } else if( other instanceof Interval ) {
            
            GeneralizedInterval otherGeneralizedInterval = new GeneralizedInterval( (Interval)other );
            return this.add( otherGeneralizedInterval );
            
        } else {
            
            throw new IllegalArgumentException( "other is not instance of Interval or GeneralizedInterval" );
        }
        
        return new GeneralizedInterval( resultIntervals );
    }
    
    public Arithmetic add( double d ) {
        
        if( d == 0. )
            return this;
        
        List<Interval> resultIntervals = new ArrayList<>();
        this.intervals.stream().forEach((Interval interval) -> {
            resultIntervals.add( interval.add( d ) );
        });
        
        return new GeneralizedInterval( resultIntervals );
    }
    
    // ---------- subtract ----------

    @Override
    public Arithmetic subtract( Arithmetic other ) {
        
        if( other.isZero() )
            return this;
        
        List<Interval> resultIntervals = new ArrayList<>();
        
        if( other instanceof GeneralizedInterval ) {
            
            GeneralizedInterval otherGeneralized = (GeneralizedInterval)other;
            
            this.intervals.stream().forEach((Interval intervalThis) -> {
                otherGeneralized.intervals.stream().forEach((Interval intervalOther) -> {
                    resultIntervals.add( intervalThis.subtract( intervalOther ) );
                });
            });
            
        } else if( other instanceof Interval ) {
            
            GeneralizedInterval otherGeneralizedInterval = new GeneralizedInterval( (Interval)other );
            return this.subtract( otherGeneralizedInterval );
            
        } else {
            
            throw new IllegalArgumentException( "other is not instance of Interval or GeneralizedInterval" );
        }
        
        return new GeneralizedInterval( resultIntervals );
    }
    
    public Arithmetic subtract( double d ) {
        
        if( d == 0. )
            return this;
        
        List<Interval> resultIntervals = new ArrayList<>();
        this.intervals.stream().forEach((Interval interval) -> {
            resultIntervals.add( interval.subtract( d ) );
        });
        
        return new GeneralizedInterval( resultIntervals );
    }
    
    // ---------- mult ----------

    @Override
    public Arithmetic mult( Arithmetic other ) {
        
        if( other.isOne() )
            return this;
        
        List<Interval> resultIntervals = new ArrayList<>();
        
        if( other instanceof GeneralizedInterval ) {
            
            GeneralizedInterval otherGeneralized = (GeneralizedInterval)other;
            
            this.intervals.stream().forEach((Interval intervalThis) -> {
                otherGeneralized.intervals.stream().forEach((Interval intervalOther) -> {
                    resultIntervals.add( intervalThis.mult( intervalOther ) );
                });
            });
            
        } else if( other instanceof Interval ) {
            
            GeneralizedInterval otherGeneralizedInterval = new GeneralizedInterval( (Interval)other );
            return this.mult( otherGeneralizedInterval );
            
        } else {
            
            throw new IllegalArgumentException( "other is not instance of Interval or GeneralizedInterval" );
        }
        
        return new GeneralizedInterval( resultIntervals );
    }
    
    public Arithmetic mult( double d ) {
        
        if( d == 1. )
            return this;
        
        List<Interval> resultIntervals = new ArrayList<>();
        this.intervals.stream().forEach((Interval interval) -> {
            resultIntervals.add( interval.mult( d ) );
        });
        
        return new GeneralizedInterval( resultIntervals );
    }
    
    // ---------- divide ----------

    @Override
    public Arithmetic divide( Arithmetic other ) {
        
        if( other.isZero() )
            throw new ArithmeticException( "Divide by 0 exception" );
        
        if( other.isOne() )
            return this;
        
        List<Interval> resultIntervals = new ArrayList<>();
        
        if( other instanceof GeneralizedInterval ) {
            
            GeneralizedInterval otherGeneralized = (GeneralizedInterval)other;
            
            for(Interval intervalThis : this.intervals ) {
                for(Interval intervalOther : otherGeneralized.intervals ) {
                    
                    if( intervalOther.zeroElementOfThis() ) {
                        
                        // [a, b] ÷ [c, d] = [min(a ÷ c, a ÷ d, b ÷ c, b ÷ d), max(a ÷ c, a ÷ d, b ÷ c, b ÷ d)]
                        // when 0 is not in [c, d].
                        //
                        // If 0 is in [c, d]:
                        //
                        //      If 0 not in [a, b]:
                        //
                        //      [a, b] ÷ [c, d] = [a, b] ÷ { [c, -0], [0+, d] }
                        //
                        //      [a, b] ÷ [c, 0-] = [min(a ÷ c, a ÷ -0, b ÷ c, b ÷ -0), max(a ÷ c, a ÷ -0, b ÷ c, b ÷ -0)]
                        //
                        //              a<0, b<0 ==> [min(a ÷ c, +Inf, b ÷ c, +Inf), max(a ÷ c, +Inf, b ÷ c, +Inf)]
                        //                       ==> [min(a ÷ c, b ÷ c), +Inf]
                        //
                        //              a>0, b>0 ==> [min(a ÷ c, -Inf, b ÷ c, -Inf), max(a ÷ c, -Inf, b ÷ c, -Inf)]
                        //                       ==> [-Inf, max(a ÷ c, b ÷ c)]
                        //
                        //otherGeneralized.intervals
                        //      [a, b] ÷ [0+, d] = [min(a ÷ 0+, a ÷ d, b ÷ 0+, b ÷ d), max(a ÷ 0+, a ÷ d, b ÷ 0+, b ÷ d)]
                        //
                        //              a<0, b<0 ==> [min(-Inf, a ÷ d, -Inf, b ÷ d), max(-Inf, a ÷ d, -Inf, b ÷ d)]
                        //                       ==> [-Inf, max(a ÷ d, b ÷ d)]
                        //
                        //              a>0, b>0 ==> [min(+Inf, a ÷ d, +Inf, b ÷ d), max(+Inf, a ÷ d, +Inf, b ÷ d)]
                        //                       ==> [min(a ÷ d, b ÷ d), +Inf]
                        //
                        //      If 0 in [a, b]:
                        //
                        //      [a, b] ÷ [c, d] = { [a, -0], [0+, b] } ÷ { [c, -0], [0+, d] }
                        //
                        //          [a, -0] ÷ [c, -0] = [min(a ÷ c, +Inf,  0, 1), max(a ÷ c, +Inf,  0, 1)] = [0, +Inf]
                        //          [a, -0] ÷ [0+, d] = [min(-Inf, a ÷ d, -1, 0), max(-Inf, a ÷ d, -1, 0)] = [-Inf, 0]
                        //          [0+, b] ÷ [c, -0] = [min(0, -1, b ÷ c, -Inf), max(0, -1, b ÷ c, -Inf)] = [-Inf, 0]
                        //          [0+, b] ÷ [0+, d] = [min(1,  0, +Inf, b ÷ d), max(1,  0, +Inf, b ÷ d)] = [0, +Inf]
                        //
                        //
                        // finally if 0 is in [c, d]:
                        //
                        //      a<0, b<0 ==> [a, b] ÷ [c, d] = { [-Inf, max (a ÷ d, b ÷ d)], [min (a ÷ c, b ÷ c), +Inf] }
                        //
                        //      a>0, b>0 ==> [a, b] ÷ [c, d] = { [-Inf, max (a ÷ c, b ÷ c)], [min (a ÷ d, b ÷ d), +Inf] }
                        //
                        //      a<0, b>0 ==> [a, b] ÷ [c, d] = { [-Inf, +Inf] }
                        //
                        
                        if( intervalThis.getMinBound() < 0 && intervalThis.getMaxBound() < 0 ) {
                            
                            resultIntervals.add( new Interval( Double.NEGATIVE_INFINITY, 
                                                               Math.max( intervalThis.getMinBound()/intervalOther.getMaxBound(), 
                                                                         intervalThis.getMaxBound()/intervalOther.getMaxBound()
                                                                       ) 
                                                             ) 
                                                );
                            resultIntervals.add( new Interval( Math.min( intervalThis.getMinBound()/intervalOther.getMinBound(), 
                                                                         intervalThis.getMaxBound()/intervalOther.getMinBound()
                                                                       ),
                                                               Double.POSITIVE_INFINITY
                                                             ) 
                                                );
                             
                        } else if( intervalThis.getMinBound() > 0 && intervalThis.getMaxBound() > 0 ) {
                            
                            resultIntervals.add( new Interval( Double.NEGATIVE_INFINITY, 
                                                               Math.max( intervalThis.getMinBound()/intervalOther.getMinBound(), 
                                                                         intervalThis.getMaxBound()/intervalOther.getMinBound()
                                                                       ) 
                                                             ) 
                                                );
                            resultIntervals.add( new Interval( Math.min( intervalThis.getMinBound()/intervalOther.getMaxBound(), 
                                                                         intervalThis.getMaxBound()/intervalOther.getMaxBound()
                                                                       ),
                                                               Double.POSITIVE_INFINITY
                                                             ) 
                                                );
                             
                        }  else { // here intervalThis.getMinBound() < 0 && intervalThis.getMaxBound() > 0
                            
                            return new GeneralizedInterval( new Interval( Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY ) );
                        }
                        
                    } else {
                        
                        resultIntervals.add( intervalThis.divide( intervalOther ) );
                    }
                    
                }
            }
            
        } else if( other instanceof Interval ) {
            
            GeneralizedInterval otherGeneralizedInterval = new GeneralizedInterval( (Interval)other );
            return this.mult( otherGeneralizedInterval );
            
        } else {
            
            throw new IllegalArgumentException( "other is not instance of Interval or GeneralizedInterval" );
        }
        
        return new GeneralizedInterval( resultIntervals );
    }
    
    public Arithmetic divide( double d ) {
        
        if( d == 0. )
            throw new ArithmeticException( "Divide by 0 exception" );
        
        if( d == 1. )
            return this;
        
        List<Interval> resultIntervals = new ArrayList<>();
        this.intervals.stream().forEach((Interval interval) -> {
            resultIntervals.add( interval.divide( d ) );
        });
        
        return new GeneralizedInterval( resultIntervals );
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
