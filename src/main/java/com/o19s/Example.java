package com.o19s;

import com.foundationdb.*;
import java.nio.charset.Charset;


/**
 * FoundationDB
 * Hello world!
 * 
 * This example is based on
 *
 */
public class Example 
{
    public static void main( String[] args ) throws Throwable
    {
    	Computer computer = new Computer();

    	JUnitCore jUnitCore = new JUnitCore();
    	jUnitCore.run(computer, MySuite.class);
    }
}
