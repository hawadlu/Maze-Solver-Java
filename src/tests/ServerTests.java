package tests;

import application.Application;
import org.junit.jupiter.api.Test;
import server.LocalClient;
import server.Server;
import utility.Exceptions.InvalidArguments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
        assertThrows(InvalidArguments.class, () -> {
            String[] arguments = new String[0];
            Server.main(arguments);
        });
    }

    //Create a range of invalid ports
    @Test
    public void testInvalidPorts() {
        //test a range of port numbers of port numbers from -65353 to 100,000.
        //The numbers -65353 are not ports and 0 - 1023 are reserved so both should fail.
        //1024 - 65353 should pass while anything greater should not.
        String[] arguments = new String[1];
        arguments[0] = "-65421";

        //todo add tests from -65421 to 100000
        //port info here. https://www.sciencedirect.com/topics/computer-science/registered-port#:~:text=Well%2DKnown%20Ports,application%20processes%20on%20other%20hosts.

        assertThrows(InvalidArguments.class, () -> {
            Server.main(arguments);
        });
    }

    //Test an argument of an invalid type (port only)
    @Test
    public void testInvalidPortType() throws InvalidArguments {
        assertThrows(InvalidArguments.class, () -> {
            String[] arguments = new String[1];
            arguments[0] = "hello world";
            Server.main(arguments);
        });
    }

    //VALID TESTS

    //Test creating a server with a range of port numbers
    @Test
    public void testValidPort() throws InvalidArguments {
        String[] arguments = new String[1];
        arguments[0] = "5000";
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
