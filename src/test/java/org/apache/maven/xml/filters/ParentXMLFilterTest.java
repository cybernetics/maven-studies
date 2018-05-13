package org.apache.maven.xml.filters;

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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.XMLFilter;

public class ParentXMLFilterTest extends AbstractXMLFilterTests
{
    private ParentXMLFilter filter;
    
    @Before
    public void setUp()
    {
        filter = new ParentXMLFilter( r -> "1.0.0" );
    }

    @Test
    public void testMinimum() throws Exception
    {
        String input = "<parent/>";
        String expected = input;
        String actual = transform( input, filter );
        assertEquals( expected, actual );
    }

    @Test
    public void testNoRelativePath() throws Exception
    {
        String input = "<parent>"
            + "<groupId>GROUPID</groupId>"
            + "<artifactId>ARTIFACTID</artifactId>"
            + "<version>VERSION</version>"
            + "</parent>";
        String expected = input;

        String actual = transform( input, filter );

        assertEquals( expected, actual );
    }

    @Test
    public void testDefaultRelativePath() throws Exception
    {
        String input = "<parent>"
            + "<groupId>GROUPID</groupId>"
            + "<artifactId>ARTIFACTID</artifactId>"
            + "</parent>";
        String expected = "<parent>"
                        + "<groupId>GROUPID</groupId>"
                        + "<artifactId>ARTIFACTID</artifactId>"
                        + "<version>1.0.0</version>"
                        + "</parent>";

        String actual = transform( input, filter );

        assertEquals( expected, actual );
    }

    @Test
    public void testNoVersion() throws Exception
    {
        String input = "<parent>"
            + "<groupId>GROUPID</groupId>"
            + "<artifactId>ARTIFACTID</artifactId>"
            + "<relativePath>RELATIVEPATH</relativePath>"
            + "</parent>";
        String expected = "<parent>"
                        + "<groupId>GROUPID</groupId>"
                        + "<artifactId>ARTIFACTID</artifactId>"
                        + "<version>1.0.0</version>"
                        + "</parent>";

        String actual = transform( input, filter );

        assertEquals( expected, actual );
    }

    @Test
    public void testInvalidRelativePath() throws Exception
    {
        XMLFilter filter = new ParentXMLFilter( r -> null );
        
        String input = "<parent>"
            + "<groupId>GROUPID</groupId>"
            + "<artifactId>ARTIFACTID</artifactId>"
            + "<relativePath>RELATIVEPATH</relativePath>"
            + "</parent>";
        String expected = input;

        String actual = transform( input, filter );

        assertEquals( expected, actual );
    }

    @Test
    public void testRelativePathAndVersion() throws Exception
    {
        String input = "<parent>"
            + "<groupId>GROUPID</groupId>"
            + "<artifactId>ARTIFACTID</artifactId>"
            + "<relativePath>RELATIVEPATH</relativePath>"
            + "<version>1.0.0</version>"
            + "</parent>";
        String expected = "<parent>"
                        + "<groupId>GROUPID</groupId>"
                        + "<artifactId>ARTIFACTID</artifactId>"
                        + "<version>1.0.0</version>"
                        + "</parent>";

        String actual = transform( input, filter );

        assertEquals( expected, actual );
    }

    @Test
    public void testWithWeirdNamespace() throws Exception
    {
        String input = "<relativePath:parent xmlns:relativePath=\"relativePath\">"
            + "<relativePath:groupId>GROUPID</relativePath:groupId>"
            + "<relativePath:artifactId>ARTIFACTID</relativePath:artifactId>"
            + "<relativePath:relativePath>RELATIVEPATH</relativePath:relativePath>"
            + "<relativePath:version>1.0.0</relativePath:version>"
            + "</relativePath:parent>";
        String expected = "<relativePath:parent xmlns:relativePath=\"relativePath\">"
                        + "<relativePath:groupId>GROUPID</relativePath:groupId>"
                        + "<relativePath:artifactId>ARTIFACTID</relativePath:artifactId>"
                        + "<relativePath:version>1.0.0</relativePath:version>"
                        + "</relativePath:parent>";

        String actual = transform( input, filter );

        assertEquals( expected, actual );
    }
}
