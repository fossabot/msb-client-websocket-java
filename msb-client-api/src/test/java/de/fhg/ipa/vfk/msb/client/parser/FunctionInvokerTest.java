/*
 * Copyright (c) 2019 Fraunhofer Institute for Manufacturing Engineering and Automation (IPA).
 * Authors: Daniel Schel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package de.fhg.ipa.vfk.msb.client.parser;

import de.fhg.ipa.vfk.msb.client.api.messages.FunctionCallMessage;
import de.fhg.ipa.vfk.msb.client.util.MsbDateFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The type Function invoker test.
 *
 * @author des
 */
public class FunctionInvokerTest {

    private Object obj;
    private int number;
    private Long longNumber;
    private boolean bool;
    private String[] array;
    private Date date;
    private byte[] byteArray;
    private BigDecimal bigDecimal;
    private boolean called;

    private FunctionCallReference callback;

    /**
     * Init.
     *
     * @throws NoSuchMethodException the no such method exception
     */
    @Before
    public void init() throws NoSuchMethodException {
        Map<String, Type> functionCallbackParameters = new LinkedHashMap<>();
        functionCallbackParameters.put("obj",Object.class);
        functionCallbackParameters.put("number",int.class);
        functionCallbackParameters.put("longNumber",Long.class);
        functionCallbackParameters.put("bool",Boolean.class);
        functionCallbackParameters.put("array",String[].class);
        functionCallbackParameters.put("date",Date.class);
        functionCallbackParameters.put("byte",byte[].class);
        functionCallbackParameters.put("bigDecimal",BigDecimal.class);
        callback = new FunctionCallReference();
        callback.setFunctionHandlerInstance(this);
        callback.setMethod(this.getClass().getDeclaredMethod("testFunction", Object.class, int.class, Long.class, boolean.class, String[].class, Date.class, byte[].class, BigDecimal.class));
        callback.setParameters(functionCallbackParameters);
    }

    /**
     * Test call function.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     * @throws ParseException            the parse exception
     */
    @Test
    public void testCallFunction() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException, ParseException {
        obj=null;
        number=0;
        longNumber=0L;
        bool=false;
        array=null;
        date = null;
        byteArray = null;
        bigDecimal = null;

        Map<String,Object> functionParameters = new LinkedHashMap<>();
        functionParameters.put("obj",new Object());
        functionParameters.put("number",1);
        functionParameters.put("longNumber",2);
        functionParameters.put("bool",true);
        functionParameters.put("array",new String[]{});
        functionParameters.put("date","2018-03-01T12:48:58.771+01");
        functionParameters.put("byte","test byte string".getBytes());
        functionParameters.put("bigDecimal",BigDecimal.valueOf(1.2));
        FunctionCallMessage outData = new FunctionCallMessage("","testFunction",null,functionParameters);
        FunctionInvoker.callFunctions(outData,callback);
        Assert.assertNotNull("is null",obj);
        Assert.assertEquals("is not 1",1,number);
        Assert.assertEquals("is not 2",2L, longNumber.longValue());
        Assert.assertTrue("is false",bool);
        Assert.assertNotNull("is null",array);
        Assert.assertEquals("date not equals",new MsbDateFormat().parse("2018-03-01T12:48:58.771+01"),date);
        Assert.assertEquals("byte array not equals","test byte string",new String(byteArray));
        Assert.assertEquals("bigDecimal not equals",BigDecimal.valueOf(1.2),bigDecimal);
    }

    /**
     * Test call function with missing some parameters.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     */
    @Test
    public void testCallFunctionWithMissingSomeParameters() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        obj=null;
        number=0;
        longNumber=0L;
        bool=false;
        array=null;
        date = null;
        byteArray = null;
        bigDecimal = null;
        Map<String,Object> functionParameters = new LinkedHashMap<>();
        functionParameters.put("number",2);
        FunctionCallMessage outData = new FunctionCallMessage("","testFunction",null,functionParameters);
        FunctionInvoker.callFunctions(outData,callback);
        Assert.assertNull("not null",obj);
        Assert.assertEquals("is not 2",2, number);
        Assert.assertEquals("is not 0",0L, longNumber.longValue());
        Assert.assertFalse("is true",bool);
        Assert.assertNull("not null",array);
        Assert.assertNull("not null",date);
        Assert.assertNull("not null",byteArray);
        Assert.assertNull("not null",bigDecimal);
    }

    /**
     * Test call function with missing all parameters.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     */
    @Test
    public void testCallFunctionWithMissingAllParameters() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        obj=null;
        number=0;
        longNumber=0L;
        bool=false;
        array=null;
        date = null;
        byteArray = null;
        bigDecimal = null;
        FunctionCallMessage outData = new FunctionCallMessage("","testFunction",null,new HashMap<String, Object>());
        FunctionInvoker.callFunctions(outData,callback);
        Assert.assertNull("not null",obj);
        Assert.assertEquals("is not 0",0,number);
        Assert.assertEquals("is not 0",0L, longNumber.longValue());
        Assert.assertFalse("is true",bool);
        Assert.assertNull("not null",array);
        Assert.assertNull("not null",date);
        Assert.assertNull("not null",byteArray);
        Assert.assertNull("not null",bigDecimal);
    }

    /**
     * Test fail call function.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFailCallFunction() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        obj=null;
        number=0;
        longNumber=0L;
        bool=false;
        array=null;
        date = null;
        byteArray = null;
        Map<String,Object> functionParameters = new LinkedHashMap<>();
        functionParameters.put("number",null);
        functionParameters.put("bool",null);
        FunctionCallMessage outData = new FunctionCallMessage("","testFunction",null,functionParameters);
        FunctionInvoker.callFunctions(outData,callback);
    }

    /**
     * Test fail call function 2.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     */
    @Test(expected = IllegalAccessException.class)
    public void testFailCallFunction2() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        FunctionCallMessage outData = new FunctionCallMessage("","testFunction",null,new HashMap<String,Object>());
        FunctionInvoker.callFunctions(outData,null);
    }

    /**
     * Test fail call function without parameters.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     * @throws NoSuchMethodException     the no such method exception
     */
    @Test
    public void testFailCallFunctionWithoutParameters() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException, NoSuchMethodException {
        called = false;
        FunctionCallMessage outData = new FunctionCallMessage("", "testFunction", null, new HashMap<String, Object>());
        FunctionCallReference callback = new FunctionCallReference();
        callback.setFunctionHandlerInstance(this);
        callback.setMethod(this.getClass().getDeclaredMethod("testFunction" ));
        FunctionInvoker.callFunctions(outData, callback);
        Assert.assertTrue(called);
    }

    /**
     * Test fail call function fails.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     */
    @Test
    public void testFailCallFunctionFails() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException {
        FunctionCallMessage outData = new FunctionCallMessage("", "testFunction", null, new HashMap<String, Object>());
        FunctionCallReference callback = new FunctionCallReference();
        Object result = FunctionInvoker.callFunctions(outData, callback);
        Assert.assertNull(result);
    }

    /**
     * Test fail call function fails 2.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     * @throws NoSuchMethodException     the no such method exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFailCallFunctionFails2() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException, NoSuchMethodException {
        Map<String,Object> functionParameters = new LinkedHashMap<>();
        functionParameters.put("obj",new Object());
        functionParameters.put("number",1);
        functionParameters.put("longNumber",2);
        functionParameters.put("bool",true);
        functionParameters.put("array",new String[]{});
        functionParameters.put("date","2018-03-01T12:48:58.771+01");
        functionParameters.put("byte","test byte string".getBytes());
        functionParameters.put("bigDecimal",BigDecimal.valueOf(1.2));
        FunctionCallMessage outData = new FunctionCallMessage("","testFunction",null,functionParameters);
        Map<String, Type> functionCallbackParameters = new LinkedHashMap<>();
        functionCallbackParameters.put("obj",null);
        functionCallbackParameters.put("number",Integer.class);
        functionCallbackParameters.put("longNumber",Long.class);
        functionCallbackParameters.put("bool",Boolean.class);
        functionCallbackParameters.put("array",String[].class);
        functionCallbackParameters.put("date",Date.class);
        functionCallbackParameters.put("byte",byte[].class);
        functionCallbackParameters.put("bigDecimal",BigDecimal.class);
        callback = new FunctionCallReference();
        callback.setFunctionHandlerInstance(this);
        callback.setMethod(this.getClass().getDeclaredMethod("testFunction", Object.class, int.class, Long.class, boolean.class, String[].class, Date.class, byte[].class, BigDecimal.class));
        callback.setParameters(functionCallbackParameters);
        FunctionInvoker.callFunctions(outData, callback);
    }

    /**
     * Test call function with convertable types.
     *
     * @throws ClassNotFoundException    the class not found exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IOException               the io exception
     * @throws ParseException            the parse exception
     */
    @Test
    public void testCallFunctionWithConvertableTypes() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException, ParseException {
        obj=null;
        number=0;
        longNumber=0L;
        bool=false;
        array=null;
        date = null;
        byteArray = null;
        bigDecimal = null;
        Map<String,Object> functionParameters = new LinkedHashMap<>();
        functionParameters.put("obj",new Object());
        functionParameters.put("number",1.2F);
        functionParameters.put("longNumber",2L);
        functionParameters.put("bool",true);
        functionParameters.put("array",new String[]{});
        functionParameters.put("date","2018-03-01T12:48:58.771+01");
        functionParameters.put("byte","test byte string".getBytes());
        functionParameters.put("bigDecimal",1.2);
        FunctionCallMessage outData = new FunctionCallMessage("","testFunction",null,functionParameters);
        FunctionInvoker.callFunctions(outData, callback);
        Assert.assertNotNull("is null",obj);
        Assert.assertEquals("is not 1",1,number);
        Assert.assertEquals("is not 2",2L, longNumber.longValue());
        Assert.assertTrue("is false",bool);
        Assert.assertNotNull("is null",array);
        Assert.assertEquals("date not equals",new MsbDateFormat().parse("2018-03-01T12:48:58.771+01"),date);
        Assert.assertEquals("byte array not equals","test byte string",new String(byteArray));
        Assert.assertEquals("bigDecimal not equals",BigDecimal.valueOf(1.2),bigDecimal);
    }

    /**
     * Test function.
     *
     * @param obj        the obj
     * @param number     the number
     * @param longNumber the long number
     * @param bool       the bool
     * @param array      the array
     * @param date       the date
     * @param byteArray  the byte array
     * @param bigDecimal the big decimal
     */
    public void testFunction(Object obj, int number, Long longNumber, boolean bool, String[] array, Date date, byte[] byteArray, BigDecimal bigDecimal){
        this.obj = obj;
        this.number = number;
        this.longNumber = longNumber;
        this.bool = bool;
        this.array = array;
        this.date = date;
        this.byteArray = byteArray;
        this.bigDecimal = bigDecimal;
    }

    /**
     * Test function.
     */
    public void testFunction(){
        this.called = true;
    }

}
