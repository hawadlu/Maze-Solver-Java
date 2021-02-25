package tests;

import application.Application;
import org.junit.jupiter.api.Test;
import server.LocalClient;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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

    //Test valid type of immediateReturn
    @Test
    public void testValidImmediateReturn() {
        String[] arguments = new String[2];
        arguments[0] = "5000";
        arguments[1] = "true";
        Server.main(arguments);
    }

    //Connect a single client to the server
    @Test
    public void connectClient() throws InterruptedException {
        //Create a server
        Server server = new Server(5000);
        server.start();

        //Wait for the server to start
        TimeUnit.MILLISECONDS.sleep(10);

        System.out.println("Client joining server");
        //Connect a client to the server
        Application application = new Application();
        application.connectToServer(5000);

        //Wait for the server to add the player
        TimeUnit.MILLISECONDS.sleep(10);
        assertEquals(1, server.getClientCount());
    }
}
