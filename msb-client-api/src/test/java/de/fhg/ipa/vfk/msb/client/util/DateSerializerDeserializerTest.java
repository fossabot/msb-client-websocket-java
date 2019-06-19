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

package de.fhg.ipa.vfk.msb.client.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The type Date serializer deserializer test.
 *
 * @author des
 */
public class DateSerializerDeserializerTest {

    private ObjectMapper mapper;

    private final SimpleDateFormat dateFormat = new MsbDateFormat();

    /**
     * Before test.
     */
    @Before
    public void beforeTest(){
        mapper = new ObjectMapper();
        mapper.setDateFormat(dateFormat);
        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, new DateSerializer());
        module.addDeserializer(Date.class, new DateDeserializer());
        mapper.registerModule(module);
    }

    /**
     * Test date serializer.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void testDateSerializer() throws JsonProcessingException {
        Date date = new Date();
        String stringDate = mapper.writeValueAsString(date);
        Assert.assertEquals("\""+dateFormat.format(date)+"\"",stringDate);
    }

    /**
     * Test date serializer null.
     *
     * @throws JsonProcessingException the json processing exception
     */
    @Test
    public void testDateSerializerNull() throws JsonProcessingException {
        String stringDate = mapper.writeValueAsString(null);
        Assert.assertEquals("null",stringDate);
    }

    /**
     * Test date deserializer.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testDateDeserializer() throws IOException {
        Date date = new Date();
        String stringDate = mapper.writeValueAsString(date);
        Date result = mapper.readValue(stringDate, Date.class);
        Assert.assertEquals(date,result);
    }

    /**
     * Test date deserializer empty.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testDateDeserializerEmpty() throws IOException {
        String stringDate = "{}";
        Date result = mapper.readValue(stringDate, Date.class);
        Assert.assertNull(result);
    }

    /**
     * Test date deserializer wrong string.
     *
     * @throws IOException the io exception
     */
    @Test
    public void testDateDeserializerWrongString() throws IOException {
        String stringDate = "\"wrong_date\"";
        Date result = mapper.readValue(stringDate, Date.class);
        Assert.assertNull(result);
    }


}
