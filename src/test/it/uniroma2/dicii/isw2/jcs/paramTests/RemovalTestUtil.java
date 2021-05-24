package test.it.uniroma2.dicii.isw2.jcs.paramTests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import junit.framework.TestCase;

/**
 * Simple methods to be run by active test suites that test removal.
 *
 */
@RunWith(Parameterized.class)
@Category(JUnitTest.class)
public class RemovalTestUtil {
	private static JCS jcs;
	private int start;
	private int end;

    /**
     * Constructor for the TestSimpleLoad object
     *
     * @param testName
     *            Description of the Parameter
     */
    public RemovalTestUtil(int start, int end) {
    	this.start = start;
    	this.end = end;
    }
    
	/*
	 * Configurazione dell'ambiente prima dell'esecuzione della test suite
	 */
	@BeforeClass
	public static void configure() throws CacheException {
		 jcs = JCS.getInstance( "testCache1" );
	}
	
	/*
	 * Valori dei parametri da testare ricavati dal Domain Partitioning
	 */
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {1,2},{3,4}
        });
    }

    /**
     * Adds elements in the range specified and then removes them using the
     * categorical or substring removal method.
     *
     * @param start
     * @param end
     *
     * @exception Exception
     *                Description of the Exception
     */
    @Test
    public void runTestPutThenRemoveCategorical( int start, int end ) throws Exception {
        for ( int i = start; i <= end; i++ )
        {
            jcs.put( i + ":key", "data" + i );
        }

        for ( int i = end; i >= start; i-- )
        {
            String res = (String) jcs.get( i + ":key" );
            if ( res == null )
            {
                assertNotNull( "[" + i + ":key] should not be null", res );
            }
        }
        System.out.println( "Confirmed that " + ( end - start ) + " items could be found" );

        for ( int i = start; i <= end; i++ )
        {
            jcs.remove( i + ":" );
            assertNull( jcs.get( i + ":key" ) );
        }
        System.out.println( "Confirmed that " + ( end - start ) + " items were removed" );

        System.out.println( jcs.getStats() );

    }

    /**
     * Put items in the cache in this key range. Can be used to verify that
     * concurrent operations are not effected by things like hierchical removal.
     *
     * @param start
     *            int
     * @param end
     *            int
     * @throws Exception
     */
    public void runPutInRange( int start, int end ) throws Exception {

        for ( int i = start; i <= end; i++ )
        {
            jcs.put( i + ":key", "data" + i );
        }

        for ( int i = end; i >= start; i-- )
        {
            String res = (String) jcs.get( i + ":key" );
            if ( res == null )
            {
                assertNotNull( "[" + i + ":key] should not be null", res );
            }
        }

    }

    /**
     * Just get from start to end.
     *
     * @param start
     *            int
     * @param end
     *            int
     * @param check
     *            boolean -- check to see if the items are in the cache.
     * @throws Exception
     */
    public void runGetInRange( int start, int end, boolean check ) throws Exception {

        // don't care if they are found
        for ( int i = end; i >= start; i-- )
        {
            String res = (String) jcs.get( i + ":key" );
            if ( check && res == null )
            {
                assertNotNull( "[" + i + ":key] should not be null", res );
            }

        }

    }

}
