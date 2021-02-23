package tests;

import org.junit.jupiter.api.Test;
import server.Server;

/**
 * This class is used to test elements of the server
 */
public class ServerTests {
    //INVALID TESTS

    //Try to create a server without any arguments
    @Test
    public void testInvalidArgumentCount() {
        //todo throw an invalid argument count failure
        String[] arguments = new String[0];
        Server.main(arguments);
    }

    //Test an argument of an invalid type (port only)
    @Test
    public void testInvalidPortType() {
        //todo thrown an invalid argument failure, make sure has no decimals
        String[] arguments = new String[2];
        arguments[0] = "hello world";
        arguments[1] = "true";
        Server.main(arguments);
    }

    //Test an argument of an invalid type (immediateReturn only)
    @Test
    public void testInvalidImmediateReturnType() {
        //todo thrown an invalid argument failure, make sure has no decimals
        String[] arguments = new String[2];
        arguments[0] = "hello world";
        arguments[1] = "true";
        Server.main(arguments);
    }

    //VALID TESTS

    //Test creating a server with a range of port numbers
    @Test
    public void testValidPort() {
        String[] arguments = new String[2];
        arguments[0] = "5000";
        arguments[1] = "true";
        Server.main(arguments);
    }
}
