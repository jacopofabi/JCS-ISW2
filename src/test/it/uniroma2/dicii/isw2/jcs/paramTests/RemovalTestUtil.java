package test.it.uniroma2.dicii.isw2.jcs.paramTests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.junit.AfterClass;
import org.junit.Assume;
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


/**
 * Simple methods to be run by active test suites that test removal.
 *
 */
@RunWith(Parameterized.class)
@Category(RemovalTestUtil.class)
public class RemovalTestUtil {
	private static JCS jcs;
	private enum Type {RunTestPutThenRemoveCategorical, RunPutInRange, RunGetInRange};
	private Type type;
	private int start;
	private int end;
	private boolean check = false;

    /**
     * Constructor for the TestSimpleLoad object
     *
     * @param testName
     *            Description of the Parameter
     */
    public RemovalTestUtil(Type type, int start, int end, boolean check) {
    	this.type = type;
    	this.start = start;
    	this.end = end;
    	this.check = check;
    }
    
	/*
	 * Configurazione dell'ambiente prima dell'esecuzione della test suite
	 */
	@BeforeClass
	public static void configure() throws CacheException {
		 jcs = JCS.getInstance("testCache1");
	}
	
	/*
	 * Valori dei parametri da testare, ricavati dalla classe ConcurrentRemovalLoadTest.java, dove viene creata e lanciata
	 * la test suite per l'esecuzione dei test presenti in RemovalTestUtil.java
	 * Utilizziamo per ogni insieme di parametri un tipo, che identifica il preciso metodo di test.
	 * Quando la classe viene istanziata, il tipo specificato permette a JUnit di capire se il metodo deve essere eseguito o skippato
	 * per quello specifico insieme di parametri, sfruttando Assume.assumeTrue.
	 * In questo modo, eseguiamo i test solamente con i valori di default usati da JCS, ed evitiamo errori dei casi di test perchè 
	 * "runTestPutThenRemoveCategorical" e "runPutInRange" falliscono con start=0 e end=1000 utilizzati da "runGetInRange".
	 */
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {Type.RunTestPutThenRemoveCategorical,0,200,false},
                {Type.RunPutInRange,300,400,false},
                {Type.RunPutInRange,401,600,false},
                {Type.RunTestPutThenRemoveCategorical,601,700,false},
                {Type.RunTestPutThenRemoveCategorical,701,800,false},
                {Type.RunGetInRange,0,1000,false}
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
    public void runTestPutThenRemoveCategorical() throws Exception {
    	//se type della classe istanziata è "RunTestPutThenRemoveCategorical", il test viene eseguito con i parametri specifici
    	//della classe istanziata, altrimenti viene skippato
    	Assume.assumeTrue(this.type == Type.RunTestPutThenRemoveCategorical);
        for ( int i = this.start; i <= this.end; i++ )
        {
            jcs.put( i + ":key", "data" + i );
        }

        for ( int i = this.end; i >= this.start; i-- )
        {
            String res = (String) jcs.get( i + ":key" );
            if ( res == null )
            {
                assertNotNull( "[" + i + ":key] should not be null", res );
            }
        }
        System.out.println( "Confirmed that " + ( this.end - this.start ) + " items could be found" );

        for ( int i = this.start; i <= this.end; i++ )
        {
            jcs.remove( i + ":" );
            assertNull( jcs.get( i + ":key" ) );
        }
        System.out.println( "Confirmed that " + ( this.end - this.start ) + " items were removed" );

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
    @Test
    public void runPutInRange() throws Exception {
    	Assume.assumeTrue(this.type == Type.RunPutInRange);
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
    @Test
    public void runGetInRange() throws Exception {
    	Assume.assumeTrue(this.type == Type.RunGetInRange);
        // don't care if they are found
        for ( int i = this.end; i >= this.start; i-- )
        {
            String res = (String) jcs.get( i + ":key" );
            if ( this.check && res == null )
            {
                assertNotNull( "[" + i + ":key] should not be null", res );
            }

        }

    }
    
    
	@AfterClass
	public static void cleanUp() {
		 jcs.dispose();
	}

}
