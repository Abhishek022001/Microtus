package net.minestom.server.network.socket;

import net.minestom.server.listener.manager.PacketListenerManager;
import net.minestom.server.network.PacketProcessor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnixDomainSocketAddress;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class ServerAddressTest {

    @Test
    void inetAddressTest() throws IOException {
        // These like to fail on github actions
        assumeTrue(System.getenv("GITHUB_ACTIONS") == null);

        InetSocketAddress address = new InetSocketAddress("localhost", 25565);
        var server = new Server(new PacketProcessor(new PacketListenerManager()));
        server.init(address);
        assertSame(address, server.socketAddress());
        assertEquals(address.getHostString(), server.getAddress());
        assertEquals(address.getPort(), server.getPort());

        assertDoesNotThrow(server::start);
        assertDoesNotThrow(server::stop);
    }

    @Test
    void inetAddressDynamicTest() throws IOException {
        // These like to fail on github actions
        assumeTrue(System.getenv("GITHUB_ACTIONS") == null);

        InetSocketAddress address = new InetSocketAddress("localhost", 0);
        var server = new Server(new PacketProcessor(new PacketListenerManager()));
        server.init(address);
        assertSame(address, server.socketAddress());
        assertEquals(address.getHostString(), server.getAddress());
        assertNotEquals(address.getPort(), server.getPort());

        assertDoesNotThrow(server::start);
        assertDoesNotThrow(server::stop);
    }

    @Test
    void unixAddressTest() throws IOException {
        // These like to fail on github actions
        assumeTrue(System.getenv("GITHUB_ACTIONS") == null);

        UnixDomainSocketAddress address = UnixDomainSocketAddress.of("minestom.sock");
        var server = new Server(new PacketProcessor(new PacketListenerManager()));
        server.init(address);
        assertTrue(Files.exists(address.getPath()));
        assertSame(address, server.socketAddress());
        assertEquals("unix://" + address.getPath(), server.getAddress());
        assertEquals(0, server.getPort());

        assertDoesNotThrow(server::start);
        assertDoesNotThrow(server::stop);
        assertFalse(Files.exists(address.getPath()), "The socket file should be deleted");
    }

    @Test
    void noAddressTest() throws IOException {
        var server = new Server(new PacketProcessor(new PacketListenerManager()));
        assertDoesNotThrow(server::stop);
    }
}
