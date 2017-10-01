package com.github.berthy.utils.math.intervals;

import com.github.berthy.utils.math.Arithmetic;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author berthy
 */
public class GenralizedIntervalTest {
    
    private final static double[][][][] PUT_TESTS;
    private final static double[][][][] EQUALS_TESTS;
    
    static {
        PUT_TESTS = new double[][][][] {
            {
                // instance
                { { -5., -3. }, { -1., 2 }, { 3., 6. } },
                // interval to put
                { { -10., -7. } },
                // expectedResult
                { { -10., -7. }, { -5., -3. }, { -1., 2 }, { 3., 6. } }
            },
            {
                // instance
                { { -5., -3. }, { -1., 2 }, { 3., 6. } },
                // interval to put
                { { -2., -1.5 } },
                // expectedResult
                { { -5., -3. }, { -2., -1.5 }, { -1., 2 }, { 3., 6. } }
            },
            {
                // instance
                { { -5., -3. }, { -1., 2 }, { 3., 6. } },
                // interval to put
                { { -2., -1. } },
                // expectedResult
                { { -5., -3. }, { -2., 2 }, { 3., 6. } }
            },
            {
                // instance
                { { -5., -3. }, { -1., 2 }, { 3., 6. } },
                // interval to put
                { { -2., 3.5 } },
                // expectedResult
                { { -5., -3. }, { -2., 6. } }
            },
            {
                // instance
                { { -5., -3. }, { -1., 2 }, { 3., 6. } },
                // interval to put
                { { 7., 10. } },
                // expectedResult
                { { -5., -3. }, { -1., 2 }, { 3., 6. }, { 7., 10. } }
            },
        };
        
        EQUALS_TESTS = new double[][][][] {
            {
                // instance
                { { -5., -3. }, { -1., 2 }, { 3., 6. } },
                // other
                { { -5., -3. }, { -1., 2 }, { 3., 6. } },
                // expResult
                { { 1. } }, // true
            },
            {
                // instance
                { { -5., -3. }, { -1., 1 }, { 3., 6. } },
                // other
                { { -5., -3. }, { -1., 2 }, { 3., 6. } },
                // expResult
                { { 0. } }, // false
            },
            {
                // instance
                { { -5., -3. }, { -1., 2 }, { 3., 6. }, { 9., 11. } },
                // other
                { { -5., -3. }, { -1., 2 }, { 3., 6. } },
                // expResult
                { { 0. } }, // false
            },
        };
        
    }
    
    public GenralizedIntervalTest() {
    }

    /**
     * Test of put method, of class GenralizedInterval.
     */
    @Test
    public void testPut() {
        System.out.println("put");
        
        for( double[][][] test : PUT_TESTS ) {
            
            Interval interval = new Interval( test[1][0][0], test[1][0][1] );
            
            List<Interval> listInstance = new ArrayList<>();
            for( double[] inter : test[0] )
                listInstance.add( new Interval( inter[0], inter[1] ) );
            GenralizedInterval instance = new GenralizedInterval( listInstance );
            
            List<Interval> listExpResult = new ArrayList<>();
            for( double[] inter : test[2] )
                listExpResult.add( new Interval( inter[0], inter[1] ) );
            GenralizedInterval expResult = new GenralizedInterval( listExpResult );
            
            GenralizedInterval result = instance.put( interval );
            
            
            assertEquals( expResult, result );
        }
    }

    /**
     * Test of remove method, of class GenralizedInterval.
     */
    @Test
    @Ignore
    public void testRemove() {
        System.out.println("remove");
        Interval interval = null;
        GenralizedInterval instance = new GenralizedInterval();
        GenralizedInterval expResult = null;
        GenralizedInterval result = instance.remove(interval);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of union method, of class GenralizedInterval.
     */
    @Test
    @Ignore
    public void testUnion() {
        System.out.println("union");
        GenralizedInterval other = null;
        GenralizedInterval instance = new GenralizedInterval();
        GenralizedInterval expResult = null;
        GenralizedInterval result = instance.union(other);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of intersection method, of class GenralizedInterval.
     */
    @Test
    @Ignore
    public void testIntersection() {
        System.out.println("intersection");
        GenralizedInterval other = null;
        GenralizedInterval instance = new GenralizedInterval();
        GenralizedInterval expResult = null;
        GenralizedInterval result = instance.intersection(other);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class GenralizedInterval.
     */
    @Test
    @Ignore
    public void testAdd_Arithmetic() {
        System.out.println("add");
        Arithmetic other = null;
        GenralizedInterval instance = new GenralizedInterval();
        Arithmetic expResult = null;
        Arithmetic result = instance.add(other);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class GenralizedInterval.
     */
    @Test
    @Ignore
    public void testAdd_double() {
        System.out.println("add");
        double d = 0.0;
        GenralizedInterval instance = new GenralizedInterval();
        Arithmetic expResult = null;
        Arithmetic result = instance.add(d);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of subtract method, of class GenralizedInterval.
     */
    @Test
    @Ignore
    public void testSubtract() {
        System.out.println("subtract");
        Arithmetic other = null;
        GenralizedInterval instance = new GenralizedInterval();
        Arithmetic expResult = null;
        Arithmetic result = instance.subtract(other);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mult method, of class GenralizedInterval.
     */
    @Test
    @Ignore
    public void testMult() {
        System.out.println("mult");
        Arithmetic other = null;
        GenralizedInterval instance = new GenralizedInterval();
        Arithmetic expResult = null;
        Arithmetic result = instance.mult(other);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of divide method, of class GenralizedInterval.
     */
    @Test
    @Ignore
    public void testDivide() {
        System.out.println("divide");
        Arithmetic other = null;
        GenralizedInterval instance = new GenralizedInterval();
        Arithmetic expResult = null;
        Arithmetic result = instance.divide(other);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of zero method, of class GenralizedInterval.
     */
    @Test
    @Ignore
    public void testZero() {
        System.out.println("zero");
        GenralizedInterval instance = new GenralizedInterval();
        Arithmetic expResult = null;
        Arithmetic result = instance.zero();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isZero method, of class GenralizedInterval.
     */
    @Test
    @Ignore
    public void testIsZero() {
        System.out.println("isZero");
        GenralizedInterval instance = new GenralizedInterval();
        boolean expResult = false;
        boolean result = instance.isZero();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of one method, of class GenralizedInterval.
     */
    @Test
    @Ignore
    public void testOne() {
        System.out.println("one");
        GenralizedInterval instance = new GenralizedInterval();
        Arithmetic expResult = null;
        Arithmetic result = instance.one();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isOne method, of class GenralizedInterval.
     */
    @Test
    @Ignore
    public void testIsOne() {
        System.out.println("isOne");
        GenralizedInterval instance = new GenralizedInterval();
        boolean expResult = false;
        boolean result = instance.isOne();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pow method, of class GenralizedInterval.
     */
    @Test
    @Ignore
    public void testPow() {
        System.out.println("pow");
        int n = 0;
        GenralizedInterval instance = new GenralizedInterval();
        Arithmetic expResult = null;
        Arithmetic result = instance.pow(n);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class GenralizedInterval.
     */
    @Test
    @Ignore
    public void testHashCode() {
        System.out.println("hashCode");
        GenralizedInterval instance = new GenralizedInterval();
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class GenralizedInterval.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        
        for( double[][][] test : EQUALS_TESTS ) {
            
            List<Interval> listInstance = new ArrayList<>();
            for( double[] inter : test[0] )
                listInstance.add( new Interval( inter[0], inter[1] ) );
            GenralizedInterval instance = new GenralizedInterval( listInstance );
            
            List<Interval> listObj= new ArrayList<>();
            for( double[] inter : test[1] )
                listObj.add( new Interval( inter[0], inter[1] ) );
            GenralizedInterval obj = new GenralizedInterval( listObj );
            
            boolean expResult = test[2][0][0] == 1.;
            
            boolean result = instance.equals(obj);
            
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of toString method, of class GenralizedInterval.
     */
    @Test
    @Ignore
    public void testToString() {
        System.out.println("toString");
        GenralizedInterval instance = new GenralizedInterval();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
