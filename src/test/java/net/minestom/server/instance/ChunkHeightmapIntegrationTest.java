package net.minestom.server.instance;

import net.minestom.server.instance.block.Block;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MicrotusExtension.class)
class ChunkHeightmapIntegrationTest {
    @Test
    void testChunkHeightmap(Env env) {
        var instance = env.createFlatInstance();
        instance.loadChunk(0, 0).join();
        var chunk = instance.getChunk(0, 0);

        var heightmap = chunk.motionBlockingHeightmap().getHeight(0, 0);
        assertEquals(39, heightmap);
    }

    @Test
    void heightMapPlaceTest(Env env) {
        var instance = env.createFlatInstance();
        instance.loadChunk(0, 0).join();
        var chunk = instance.getChunk(0, 0);

        {
            instance.setBlock(0, 40, 0, Block.STONE);
            var heightmap = chunk.motionBlockingHeightmap().getHeight(0, 0);
            assertEquals(40, heightmap);
        }

        {
            instance.setBlock(0, 45, 0, Block.STONE);
            var heightmap = chunk.motionBlockingHeightmap().getHeight(0, 0);
            assertEquals(45, heightmap);
        }
    }

    @Test
    void heightMapRemoveTest(Env env) {
        var instance = env.createFlatInstance();
        instance.loadChunk(0, 0).join();
        var chunk = instance.getChunk(0, 0);

        {
            instance.setBlock(0, 45, 0, Block.STONE);
            var heightmap = chunk.motionBlockingHeightmap().getHeight(0, 0);
            assertEquals(45, heightmap);
        }

        {
            instance.setBlock(0, 45, 0, Block.AIR);
            var heightmap = chunk.motionBlockingHeightmap().getHeight(0, 0);
            assertEquals(39, heightmap);
        }
    }
}
