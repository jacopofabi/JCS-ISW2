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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

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
@Category(JCSUniTest.class)
public class JCSUniTest {
	private JCS jcs;
	private Random random;
	private LinkedList list;
	private String key_value;
	
    public JCSUniTest(String key_value) {
        this.key_value = key_value;
    }
    
	/*
	 * Configurazione dell'ambiente prima dell'esecuzione di ogni test
	 */
	@Before
	public void configure() throws CacheException {
        JCS.setConfigFilename("/cache.ccf");
		jcs = JCS.getInstance("testCache1");
		random = new Random();
		list = buildList();
	}
	
	/*
	 * Valori dei parametri da testare
	 */
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"some:key"} 
        });
    }

    /**
     * @throws Exception
     */
    @Test
    public void testJCS() throws Exception {
        jcs.put(key_value, list);
        
        assertEquals(list, jcs.get(key_value));
    }

    private LinkedList buildList() {
        LinkedList list = new LinkedList();
        
        for ( int i = 0; i < 100; i++ ) {
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
    
	@After
	public void cleanUp() {
        jcs.dispose();
	}
}
