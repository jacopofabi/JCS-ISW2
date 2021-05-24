package test.it.uniroma2.dicii.isw2.jcs.paramTests;

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

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import junit.framework.TestSuite;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

/**
 * Simple test for the JCS class.
 *
 * @version $Id: JCSUniTest.java 536904 2007-05-10 16:03:42Z tv $
 */
@RunWith(Parameterized.class)
@Category(JUnitTest.class)
public class JCSUniTest {
	private static JCS jcs;
	private static Random random;
	private int count;

    public JCSUniTest(int count) {
        this.count = count;
    }
    
	/*
	 * Configurazione dell'ambiente prima dell'esecuzione della test suite
	 */
	@BeforeClass
	public static void configure() throws CacheException {
        random = new Random();
        JCS.setConfigFilename("/cache.ccf");
		jcs = JCS.getInstance("testCache1");
	}
	
	/*
	 * Valori dei parametri da testare ricavati dal Domain Partitioning
	 */
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {50},{60},{70},{80},{90},{100} //lasciare solo 100 e 10
        });
    }

    /**
     * @throws Exception
     */
    @Test
    public void testJCS() throws Exception {
        LinkedList list = buildList();
        
        jcs.put( "some:key", list );
        assertEquals( list, jcs.get( "some:key" ) );
    }

    private LinkedList buildList() {
        LinkedList list = new LinkedList();
        
        for ( int i = 0; i < this.count; i++ ) {
            list.add( buildMap() );
        }

        return list;
    }

    private HashMap buildMap() {
        HashMap map = new HashMap();

        byte[] keyBytes = new byte[32];
        byte[] valBytes = new byte[128];

        for ( int i = 0; i < 10; i++ ) {
            random.nextBytes( keyBytes );
            random.nextBytes( valBytes );
            map.put( new String( keyBytes ), new String( valBytes ) );
        }

        return map;
    }
}
