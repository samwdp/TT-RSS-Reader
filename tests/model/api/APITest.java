/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.api;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashSet;
import java.util.Set;
import model.Feed;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import preferences.Constants;

/**
 *
 * @author samwdp
 */
public class APITest {

    private TTRSSApi api = new TTRSSApi();

    public APITest() {
    }

    @BeforeClass
    public static void setUpClass() {
        TTRSSApi api = new TTRSSApi();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void testLogin() throws UnsupportedEncodingException {
        Constants.username = "admin";
        Constants.password = "password";
        Constants.URL = "http://localhost/tt-rss";

        boolean expected = true;
        boolean result = api.login();

        assertEquals(expected, result);

    }

    @Test
    public void testGetFeeds() throws UnsupportedEncodingException {
        Constants.username = "admin";
        Constants.password = "password";
        Constants.URL = "http://localhost/tt-rss";
        api.login();

        Set<Feed> expected = api.getFeeds();
        Set<Feed> resuls = api.getFeeds();

        assertEquals(expected, resuls);
    }
    
    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
