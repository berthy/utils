package com.github.berthy.utils.math.intervals;

import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Ignore;

/**
 * @author Bertrand COTE
 */
public class IntervalTest {
    
    final double epsilon = 1e-12;
    static double[][] tests; // tests for getters, +, -, * and /
    static double[][] zeroElementTest;
    static double[][] midWidthRadMigTest; // tests for mid, width, rad and mig
    static double[][] isSubsetTest;
    static double[][] squareTest;
    static double[][] sqrtTest;
    static double[][] powIntTest;
    static double[][] powTests;
    static double[][] intersectionUnionTests;
    
    static {
        //                         Xmin  Xmax  Ymin  Ymax +min  +max   -min   -max  *min  *max  /min     /max
        tests = new double[][]{ { -0.5,  3.0,  8.0, 10.0, 7.5, 13.0, -10.5,  -5.0, -5.0, 30.0, -0.0625, 0.375 },
                                {  8.0, 10.0, -0.5,  3.0, 7.5, 13.0,   5.0,  10.5, -5.0, 30.0, -20.0,   10./3. } };
        
        //                                  Xmin Xmax test
        zeroElementTest = new double[][]{ { -3., -1., 0. },
                                          { -3.,  1., 1. },
                                          {  1.,  3., 0. } };
        
        //                                     Xmin   Xmax  mid    width  rad   mig
        midWidthRadMigTest = new double[][]{ { -3.,   -1.,  -2.,    2.,   1.,   1. },
                                             { -2.,    0.5, -0.75,  2.5,  1.25, 0. },
                                             {  0.25,  4.25,  2.25, 4.,   2.,   0.25 } };
        
        //                                other    this  test
        isSubsetTest = new double[][]{ { -3., -1, -3, -1, 1. },
                                       { -3., -1, -2, -1, 1. }, 
                                       { -3., -1, -3, -2, 1. },
                                       { -3., -1, -4, -2, 0. },
                                       { -3., -1, -2, -0, 0. }, 
                                       { -3., -1,  2,  5, 0. }, 
                                       { -3., -1, -7, -4, 0. }, };
        
        //                                x        x²
        squareTest = new double[][]{ { -3., 2., 0.,  9. },
                                     { -5., 2., 0., 25. }, 
                                     {  2, 5.,  4., 25. }, 
                                     { -5, -2., 4., 25. } };
        
        //                                x    pow     x_pow
        powIntTest = new double[][]{ { -5, 2.,  2.,    0.,  25. }, 
                                     { -5, 2.,  3., -125.,   8. }, 
                                     { -5, 2.,  4.,    0., 625. }, 
                                     { -3, 2.,  1.,   -3.,   2. }, 
                                     { -3, 2.,  2.,    0.,   9. }, 
                                     { -3, 2.,  3.,  -27.,   8. }, 
                                     { -3, 2.,  4.,    0.,  81. }, 
                                     { -3, 2.,  5., -243.,  32. }, 
                                     { -3, 2.,  6.,    0., 729. }, 
                                     {  2, 5.,  1.,    2.,   5. }, 
                                     {  2, 5.,  2.,    4.,  25. }, 
                                     {  2, 5.,  3.,    8., 125. }, 
                                     {  2, 5.,  4.,   16., 625. }, 
                                     {  2, 5.,  5.,   32.,3125. }, 
                                     { -5, -2., 1.,   -5.,  -2. }, 
                                     { -5, -2., 2.,    4.,  25. }, 
                                     { -5, -2., 3., -125.,  -8. }, 
                                     { -5, -2., 4.,   16., 625. }, 
                                     { -5, -2., 5.,-3125., -32. } };
        
        sqrtTest = new double[][]{ {  0.,  4., 0., 2. }, 
                                   {  4.,  9., 2., 3. }, 
                                   { 16., 36., 4., 6. },
                                   { -4., -1., 0., 0. }, 
                                   { -1.,  5., 0., 0. }, 
                                   { -4.,  3., 0., 0. },
                                    };
        
        //                              x              x⁻⁴              x⁻³              x⁻²             x⁻¹         x⁰       x¹        x²         x³          x⁴
        powTests = new double[][]{ { -3.,  2.,    0.,      0.,     0.,      0.,    0.,       0.,    0,     0.,    1., 1., -3.,  2.,  0.,  9., -27.,   8.,   0.,  81. },
                                   {  2.,  3.,  1./81.,  1./16.,  1./27., 1./8.,   1./9.,  1./4.,  1./3.,  1./2., 1., 1.,  2.,  3.,  4.,  9.,   8.,  27.,  16.,  81. }, 
                                   { -3., -2.,  1./16.,  1./81., -1./8., -1./27.,  1./4.,  1./9., -1./2., -1./3., 1., 1., -3., -2.,  4.,  9., -27.,  -8.,  16.,  81. } };
        
        intersectionUnionTests = new double[][] {
            // for boolean results: 0. ==> false, 1. ==> true
            // this_min, this_max, other_min,other_max, isSubset, intersects, inter_min, inter_max, union_min, union_max
            { 1., 3., 2., 4., 0., 1., 2., 3., 1., 4. }, // intersect but not subset
            { 1., 3., 2., 2.5, 1., 1., 2., 2.5, 1., 3. }, // intersect and subset
            { 1., 3., -2., -1., 0., 0., Double.NaN, Double.NaN, Double.NaN, Double.NaN }, // no intersection
            { 1., 3., 3., 4., 0., 1., 3., 3., 1., 4. }, // a common bound

        };
    }

    /**
     * Test of getMin method, of class Interval.
     */
    @Test
    public void testGetMinBound() {
        double expResult, result;
        for( double[] test : tests ) {
            
            Interval X = new Interval( test[0], test[1] );
            Interval Y = new Interval( test[2], test[3] );
            
            result = X.getMinBound();
            expResult = test[0];
            Assert.assertEquals(expResult, result, epsilon);
            
            result = Y.getMinBound();
            expResult = test[2];
            Assert.assertEquals(expResult, result, epsilon);
        }
    }

    /**
     * Test of getMax method, of class Interval.
     */
    @Test
    public void testGetMaxBound() {
        double expResult, result;
        for( double[] test : tests ) {
            
            Interval X = new Interval( test[0], test[1] );
            Interval Y = new Interval( test[2], test[3] );
            
            result = X.getMaxBound();
            expResult = test[1];
            Assert.assertEquals(expResult, result, epsilon);
            
            result = Y.getMaxBound();
            expResult = test[3];
            Assert.assertEquals(expResult, result, epsilon);
        }
    }

    /**
     * Test of mid method, of class Interval.
     */
    @Test
    public void testMid() {
        double expResult, result;
        for( double[] test : tests ) {
            
            Interval X = new Interval( test[0], test[1] );
            Interval Y = new Interval( test[2], test[3] );
            
            result = X.mid();
            expResult = (test[0]+test[1])/2;
            Assert.assertEquals(expResult, result, epsilon);
            
            result = Y.mid();
            expResult = (test[2]+test[3])/2;
            Assert.assertEquals(expResult, result, epsilon);
        }
    }

    /**
     * Test of add method, of class Interval.
     */
    @Test
    public void testAdd_Interval() {
        Interval expResult, result;
        for( double[] test : tests ) {
            
            Interval X = new Interval( test[0], test[1] );
            Interval Y = new Interval( test[2], test[3] );
            
            result = X.add(Y);
            expResult = new Interval( test[4], test[5] );
            Assert.assertEquals(expResult.getMinBound(), result.getMinBound(), epsilon);
            Assert.assertEquals(expResult.getMaxBound(), result.getMaxBound(), epsilon);
        }
    }

    /**
     * Test of add method, of class Interval.
     */
    @Test
    public void testAdd_double() {
        Interval expResult, result;
        for( double[] test : tests ) {
            
            Interval X = new Interval( test[0], test[1] );
            Interval Y = new Interval( test[2] );
            
            result = X.add(test[2]);
            expResult = X.add(Y);
            Assert.assertEquals(expResult.getMinBound(), result.getMinBound(), epsilon);
            Assert.assertEquals(expResult.getMaxBound(), result.getMaxBound(), epsilon);
        }
    }

    /**
     * Test of subtract method, of class Interval.
     */
    @Test
    public void testSubtract_Interval() {
        Interval expResult, result;
        for( double[] test : tests ) {
            
            Interval X = new Interval( test[0], test[1] );
            Interval Y = new Interval( test[2], test[3] );
            
            result = X.subtract(Y);
            expResult = new Interval( test[6], test[7] );
            Assert.assertEquals(expResult.getMinBound(), result.getMinBound(), epsilon);
            Assert.assertEquals(expResult.getMaxBound(), result.getMaxBound(), epsilon);
        }
    }

    /**
     * Test of subtract method, of class Interval.
     */
    @Test
    public void testSubtract_double() {
        Interval expResult, result;
        for( double[] test : tests ) {
            
            Interval X = new Interval( test[0], test[1] );
            Interval Y = new Interval( test[2] );
            
            result = X.subtract(test[2]);
            expResult = X.subtract(Y);
            Assert.assertEquals(expResult.getMinBound(), result.getMinBound(), epsilon);
            Assert.assertEquals(expResult.getMaxBound(), result.getMaxBound(), epsilon);
        }
    }

    /**
     * Test of mult method, of class Interval.
     */
    @Test
    public void testMult_Interval() {
        Interval expResult, result;
        for( double[] test : tests ) {
            
            Interval X = new Interval( test[0], test[1] );
            Interval Y = new Interval( test[2], test[3] );
            
            result = X.mult(Y);
            expResult = new Interval( test[8], test[9] );
            Assert.assertEquals(expResult.getMinBound(), result.getMinBound(), epsilon);
            Assert.assertEquals(expResult.getMaxBound(), result.getMaxBound(), epsilon);
        }
    }

    /**
     * Test of mult method, of class Interval.
     */
    @Test
    public void testMult_double() {
        Interval expResult, result;
        for( double[] test : tests ) {
            
            Interval X = new Interval( test[0], test[1] );
            Interval Y = new Interval( test[2] );
            
            result = X.mult(test[2]);
            expResult = X.mult(Y);
            Assert.assertEquals(expResult.getMinBound(), result.getMinBound(), epsilon);
            Assert.assertEquals(expResult.getMaxBound(), result.getMaxBound(), epsilon);
        }
    }

    /**
     * Test of divide method, of class Interval.
     */
    @Test
    public void testDivide_Interval() {
        Interval expResult=null, result=null;
        for( double[] test : tests ) {
            
            Interval X = new Interval( test[0], test[1] );
            Interval Y = new Interval( test[2], test[3] );
            
            boolean boolTest = true;
            try {
                expResult = new Interval( test[10], test[11] );
                result = X.divide(Y);
            } catch ( ArithmeticException ae ) {
                boolTest = false;
            } finally {
                if( boolTest ) {
                    Assert.assertEquals(expResult.getMinBound(), result.getMinBound(), epsilon);
                    Assert.assertEquals(expResult.getMaxBound(), result.getMaxBound(), epsilon);
                } else {
                    if( X.zeroElementOfThis() ) {
                        Assert.fail("InvalidArgumentException not thrown.");
                    }
                }
            }
        }
    }

    /**
     * Test of divide method, of class Interval.
     */
    @Test
    public void testDivide_double() {
        Interval expResult, result;
        for( double[] test : tests ) {
            
            Interval X = new Interval( test[0], test[1] );
            Interval Y = new Interval( test[2] );
            
            result = X.divide(test[2]);
            expResult = X.divide(Y);
            Assert.assertEquals(expResult.getMinBound(), result.getMinBound(), epsilon);
            Assert.assertEquals(expResult.getMaxBound(), result.getMaxBound(), epsilon);
        }
    }

    /**
     * Test of toString method, of class Interval.
     */
    @Test
    public void testToString() {
        // Nothing t deal with toString!! Just testing properties...
        
        Interval x = new Interval( -1., 2. );
        Interval y = new Interval( -4., -3. );
        Interval z = new Interval( 5., 7. );
        
        Interval result = x.mult(y.add(z));
        Interval expResult = new Interval(-4., 8.);
        Assert.assertEquals(expResult.getMinBound(), result.getMinBound(), epsilon);
        Assert.assertEquals(expResult.getMaxBound(), result.getMaxBound(), epsilon);
        
        Interval result2 = (x.mult(y)).add(x.mult(z));
        Interval expResult2 = new Interval(-15., 18.);
        Assert.assertEquals(expResult2.getMinBound(), result2.getMinBound(), epsilon);
        Assert.assertEquals(expResult2.getMaxBound(), result2.getMaxBound(), epsilon);
        
        Assert.assertEquals( true, result.isSubsetOf(result2));
    }

    /**
     * Test of zeroNotElementOfThis method, of class Interval.
     */
    @Test
    public void testZeroElementOfThis() {
        for( double[] test : zeroElementTest ) {
            Interval instance = new Interval( test[0], test[1] );
            boolean expResult = (test[2]==1.);
            boolean result = instance.zeroElementOfThis();
            Assert.assertEquals(expResult, result);
        }
    }

    /**
     * Test of zero method, of class Interval.
     */
    @Test
    public void testZero() {
        Interval instance = new Interval();
        Interval expResult = new Interval(0., 0.);
        Interval result = instance.zero();
        Assert.assertEquals(expResult.getMinBound(), result.getMinBound(), epsilon);
        Assert.assertEquals(expResult.getMaxBound(), result.getMaxBound(), epsilon);
    }

    /**
     * Test of one method, of class Interval.
     */
    @Test
    public void testOne() {
        Interval instance = new Interval();
        Interval expResult = new Interval(1., 1.);
        Interval result = instance.one();
        Assert.assertEquals(expResult.getMinBound(), result.getMinBound(), epsilon);
        Assert.assertEquals(expResult.getMaxBound(), result.getMaxBound(), epsilon);
    }

    /**
     * Test of width method, of class Interval.
     */
    @Test
    public void testWidth() {
        for( double[] test : midWidthRadMigTest ) {
            Interval instance = new Interval(test[0], test[1]);
            double expResult = test[3];
            double result = instance.width();
            Assert.assertEquals(expResult, result, epsilon);
        }
    }

    /**
     * Test of rad method, of class Interval.
     */
    @Test
    public void testRad() {
        for( double[] test : midWidthRadMigTest ) {
            Interval instance = new Interval(test[0], test[1]);
            double expResult = test[4];
            double result = instance.rad();
            Assert.assertEquals(expResult, result, epsilon);
        }
    }

    /**
     * Test of mig method, of class Interval.
     */
    @Test
    public void testMig() {
        for( double[] test : midWidthRadMigTest ) {
            Interval instance = new Interval(test[0], test[1]);
            double expResult = test[5];
            double result = instance.mig();
            Assert.assertEquals(expResult, result, epsilon);
        }
    }

    /**
     * Test of sq method, of class Interval.
     */
    @Test
    public void testSq() {
        for( double[] test : squareTest ) {
            Interval instance = new Interval(test[0], test[1]);
            Interval expResult = new Interval(test[2], test[3]);
            Interval result = instance.sq();
            Assert.assertEquals(expResult.getMinBound(), result.getMinBound(), epsilon);
            Assert.assertEquals(expResult.getMaxBound(), result.getMaxBound(), epsilon);
        }
    }

    /**
     * Test of sqrt method, of class Interval.
     */
    @Test
    public void testSqrt() {
        for( double[] test : sqrtTest ) {
            Interval instance = new Interval(test[0], test[1]);
            Interval expResult = new Interval(test[2], test[3]);
            Interval result = instance.sqrt();
            if( !(test[0]<0. || test[1]<0.) ) {
                Assert.assertEquals(expResult.getMinBound(), result.getMinBound(), epsilon);
                Assert.assertEquals(expResult.getMaxBound(), result.getMaxBound(), epsilon);
            } else {
                if( test[0]<0. )  assert( Double.isNaN( result.getMinBound() ));
                if( test[1]<0. )  assert( Double.isNaN( result.getMaxBound() ));
            }
        }
    }

    /**
     * Test of isSubsetOf method, of class Interval.
     */
    @Test
    public void testIsSubsetOf() {
        for( double[] test : isSubsetTest ) {
            Interval    other = new Interval(test[0], test[1]);
            Interval instance = new Interval(test[2], test[3]);
            boolean expResult = (test[4]==1.);
            boolean result = instance.isSubsetOf(other);
            Assert.assertEquals(expResult, result);
        }
    }

    /**
     * Test of isZero method, of class Interval.
     */
    @Test
    public void testIsZero() {
        Interval.resetEpsilon();
        Interval instance = (new Interval()).zero();
        boolean expResult = true;
        boolean result = instance.isZero();
        Assert.assertEquals(expResult, result);
        
        instance = instance.one();
        expResult = false;
        result = instance.isZero();
        Assert.assertEquals(expResult, result);
        
        
        Interval.resetEpsilon();
        instance = new Interval(7., 5.);
        expResult = false;
        result = instance.isZero();
        Assert.assertEquals(expResult, result);
        Interval.setEpsilon(1.);
        expResult = false;
        result = instance.isZero();
        Assert.assertEquals(expResult, result);
        Interval.setEpsilon(10.);
        expResult = true;
        result = instance.isZero();
        Assert.assertEquals(expResult, result);
        
        Interval.resetEpsilon();
        instance = new Interval(-7., 5.);
        expResult = false;
        result = instance.isZero();
        Assert.assertEquals(expResult, result);
        Interval.setEpsilon(1.);
        expResult = false;
        result = instance.isZero();
        Assert.assertEquals(expResult, result);
        Interval.setEpsilon(10.);
        expResult = true;
        result = instance.isZero();
        Assert.assertEquals(expResult, result);
        
        Interval.resetEpsilon();
        instance = new Interval(-7., -5.);
        expResult = false;
        result = instance.isZero();
        Assert.assertEquals(expResult, result);
        Interval.setEpsilon(1.);
        expResult = false;
        result = instance.isZero();
        Assert.assertEquals(expResult, result);
        Interval.setEpsilon(10.);
        expResult = true;
        result = instance.isZero();
        Assert.assertEquals(expResult, result);
        
        Interval.resetEpsilon();
        instance = new Interval(7., -5.);
        expResult = false;
        result = instance.isZero();
        Assert.assertEquals(expResult, result);
        Interval.setEpsilon(1.);
        expResult = false;
        result = instance.isZero();
        Assert.assertEquals(expResult, result);
        Interval.setEpsilon(10.);
        expResult = true;
        result = instance.isZero();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of isOne method, of class Interval.
     */
    @Test
    public void testIsOne() {
        Interval.resetEpsilon();
        Interval instance = (new Interval()).one();
        boolean expResult = true;
        boolean result = instance.isOne();
        Assert.assertEquals(expResult, result);
        
        instance = instance.zero();
        expResult = false;
        result = instance.isOne();
        Assert.assertEquals(expResult, result);
        
        Interval.resetEpsilon();
        instance = new Interval(7., 5.);
        expResult = false;
        result = instance.isOne();
        Assert.assertEquals(expResult, result);
        Interval.setEpsilon(1.);
        expResult = false;
        result = instance.isOne();
        Assert.assertEquals(expResult, result);
        Interval.setEpsilon(10.);
        expResult = true;
        result = instance.isOne();
        Assert.assertEquals(expResult, result);
        
        Interval.resetEpsilon();
        instance = new Interval(7., -5.);
        expResult = false;
        result = instance.isOne();
        Assert.assertEquals(expResult, result);
        Interval.setEpsilon(1.);
        expResult = false;
        result = instance.isOne();
        Assert.assertEquals(expResult, result);
        Interval.setEpsilon(10.);
        expResult = true;
        result = instance.isOne();
        Assert.assertEquals(expResult, result);
        
        Interval.resetEpsilon();
        instance = new Interval(-7., 5.);
        expResult = false;
        result = instance.isOne();
        Assert.assertEquals(expResult, result);
        Interval.setEpsilon(1.);
        expResult = false;
        result = instance.isOne();
        Assert.assertEquals(expResult, result);
        Interval.setEpsilon(10.);
        expResult = true;
        result = instance.isOne();
        Assert.assertEquals(expResult, result);
        
        Interval.resetEpsilon();
        instance = new Interval(-7., -5.);
        expResult = false;
        result = instance.isOne();
        Assert.assertEquals(expResult, result);
        Interval.setEpsilon(1.);
        expResult = false;
        result = instance.isOne();
        Assert.assertEquals(expResult, result);
        Interval.setEpsilon(10.);
        expResult = true;
        result = instance.isOne();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of pow method, of class Interval.
     */
    @Test
    public void testPow() {
        Interval a = new Interval( -4., 5. );
        boolean t = false;
        try {
            a.pow(-1);
        } catch ( ArithmeticException ae ) {
            t = true;
        }
        if( !t ) { // here an exception must de thrown
            Assert.fail("pow fail");
        }
        
        a = new Interval( 4., 5. );
        t = false;
        try {
            a.pow(-1);
        } catch ( ArithmeticException ae ) {
            t = true;
        }
        if( t ) { // here an exception mustn't de thrown
            Assert.fail("pow fail");
        }
        
        for( double[] test : powTests) {
            Interval instance = new Interval( test[0], test[1] );
            for( int i=2; i<test.length; i+=2 ) {
                Interval expResult = new Interval(test[i], test[i+1]);
                Interval result = null;
                boolean testEcep = false;
                int n = i/2 - 5;
                try {
                    
                    result = instance.pow(n);
                } catch ( ArithmeticException ae ) {
                    testEcep = true;
                }
                if( testEcep ) {
                    // Exception thrown
                    if( instance.zeroElementOfThis() && n<0 ) {
                        // OK: Exception trown
                    } else {
                        Assert.fail("IllegalArgumentException not thrown in pow(n) with n<0 and zero is element of this");
                    }
                    
                } else {
                    // Exception not thrown
                    if( !instance.zeroElementOfThis() || n>=0) {
                        Assert.assertEquals(expResult.getMinBound(), result.getMinBound(), epsilon);
                        Assert.assertEquals(expResult.getMaxBound(), result.getMaxBound(), epsilon);
                    } else {
                        // never reached
                        Assert.fail("this line should never be reached");
                    }
                }
            }
        }
    }

    /**
     * Test of intersect method, of class Interval.
     */
    @Test
    public void testIntersects() {
        
        Interval.resetEpsilon();
        Interval instance = new Interval(-7., 5.);
        Interval other = new Interval(-1., 10);
        boolean expResult = true;
        boolean result = instance.intersects(other);
        assertTrue(expResult == result);
        
        instance = new Interval(-7., 5.);
        other = new Interval(-10., 2);
        expResult = true;
        result = instance.intersects(other);
        assertTrue(expResult == result);
        
        
        instance = new Interval(-7., 5.);
        other = new Interval(-5., 2);
        expResult = true;
        result = instance.intersects(other);
        assertTrue(expResult == result);
        
        instance = new Interval(-7., 5.);
        other = new Interval(-10., 10);
        expResult = true;
        result = instance.intersects(other);
        assertTrue(expResult == result);
        
        
        instance = new Interval(-7., 5.);
        other = new Interval(-10., -8);
        expResult = false;
        result = instance.intersects(other);
        assertTrue(expResult == result);
        
        instance = new Interval(-7., 5.);
        other = new Interval(6., 8);
        expResult = false;
        result = instance.intersects(other);
        assertTrue(expResult == result);
    }

    /**
     * Test of intersection method, of class Interval.
     */
    @Test
    public void testIntersection() {
        
        for( double[] test : intersectionUnionTests ) {
            Interval other = new Interval( test[2], test[3] );
            Interval instance = new Interval( test[0], test[1] );
            Interval result = instance.intersection( other );
            Interval expResult;
            
            if( test[5] == 1. ) {
                expResult = new Interval( test[6], test[7] );
                assertEquals( expResult.getMinBound(), result.getMinBound(), epsilon );
                assertEquals( expResult.getMaxBound(), result.getMaxBound(), epsilon );
            } else {
                expResult = null;
                assertTrue( expResult == result );
            }
        }
    }

    /**
     * Test of union method, of class Interval.
     */
    @Test
    public void testUnion() {
        
        for( double[] test : intersectionUnionTests ) {
            Interval other = new Interval( test[2], test[3] );
            Interval instance = new Interval( test[0], test[1] );
            Interval result = instance.union( other );
            Interval expResult;
        
            if( test[5] == 1. ) {
                expResult = new Interval( test[8], test[9] );
                assertEquals( expResult.getMinBound(), result.getMinBound(), epsilon );
                assertEquals( expResult.getMaxBound(), result.getMaxBound(), epsilon );
            } else {
                expResult = null;
                assertTrue( expResult == result );
            }
        }
    }

    /**
     * Test of hashCode method, of class Interval.
     */
    @Test
    @Ignore
    public void testHashCode() {
        System.out.println("hashCode");
        Interval instance = new Interval();
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class Interval.
     */
    @Test
    @Ignore
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        Interval instance = new Interval();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEpsilon method, of class Interval.
     */
    @Test
    @Ignore
    public void testSetEpsilon() {
        System.out.println("setEpsilon");
        double epsilon = 0.0;
        Interval.setEpsilon(epsilon);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of resetEpsilon method, of class Interval.
     */
    @Test
    @Ignore
    public void testResetEpsilon() {
        System.out.println("resetEpsilon");
        Interval.resetEpsilon();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}


