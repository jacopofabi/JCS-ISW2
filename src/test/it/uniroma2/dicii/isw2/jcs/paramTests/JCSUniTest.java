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
import org.apache.jcs.access.exception.InvalidArgumentException;
import org.apache.jcs.engine.control.group.GroupId;
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
	private LinkedList obj;
	private String key;
	private String cache;
	private Object result;
	
    public JCSUniTest(String key, LinkedList obj, String cache, Object result) {
        this.key = key;
        this.obj = obj;
        this.cache = cache;
        this.result = result;
    }
    
	/*
	 * Configurazione dell'ambiente prima dell'esecuzione di ogni test
	 */
	@Before
	public void configure() throws CacheException {
        JCS.setConfigFilename("/cache.ccf");
		jcs = JCS.getInstance(cache);
	}
	
	/*
	 * Valori dei parametri da testare
	 */
    @Parameters
    public static Collection<Object[]> data() {
    	String key = "some:key";
    	LinkedList obj = buildList();
    	
        return Arrays.asList(new Object[][] {
                {key, obj, "testCache1", null},
                {null, obj, "testCache1", InvalidArgumentException.class},
                {key, null, "testCache1", InvalidArgumentException.class},
                {key, obj, "prova", CacheException.class}
        });
    }

    /**
     * @throws CacheException 
     */
    @Test
    public void testJCS() throws CacheException {
        try {
        	jcs.put(key, obj);
        	assertEquals(obj, jcs.get(key));
        }
        catch (Exception e) {
        	assertEquals(result, e.getClass());
        }
    }

    private static LinkedList buildList() {
        LinkedList list = new LinkedList();
        
        for ( int i = 0; i < 100; i++ ) {
            list.add( buildMap() );
        }

        return list;
    }

    private static HashMap buildMap() {
    	Random random = new Random();
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
