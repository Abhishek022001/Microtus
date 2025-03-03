package net.minestom.server.utils;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.minestom.server.adventure.MinestomAdventure;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.SetSlotPacket;
import net.minestom.testing.Env;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.network.packet.server.play.SystemChatPacket;
import net.minestom.testing.extension.MicrotusExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.text.MessageFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MicrotusExtension.class)
class TranslationIntegrationTest {

    @BeforeAll
    static void translator() {
        final var translator = TranslationRegistry.create(Key.key("test.reg"));
        translator.register("test.key", MinestomAdventure.getDefaultLocale(), new MessageFormat("This is a test message", MinestomAdventure.getDefaultLocale()));

        GlobalTranslator.translator().addSource(translator);
    }

    @Test
    void testTranslationEnabled(final Env env) {
        final var instance = env.createFlatInstance();
        final var connection = env.createConnection();
        final var player = connection.connect(instance, new Pos(0, 40, 0)).join();
        final var collector = connection.trackIncoming(SystemChatPacket.class);

        MinestomAdventure.AUTOMATIC_COMPONENT_TRANSLATION = true;
        final var message = Component.translatable("test.key");
        final var packet = new SystemChatPacket(message, false);
        PacketUtils.sendGroupedPacket(List.of(player), packet);

        // the message should not be changed if translations are enabled.
        // the translation of the message itself will be proceeded in PlayerConnectionImpl class
        collector.assertSingle(received -> {
            assertNotEquals(message, received.message());
        });
    }

    @Test
    void testTranslationDisabled(final Env env) {
        final var instance = env.createFlatInstance();
        final var connection = env.createConnection();
        final var player = connection.connect(instance, new Pos(0, 40, 0)).join();
        final var collector = connection.trackIncoming(SystemChatPacket.class);

        MinestomAdventure.AUTOMATIC_COMPONENT_TRANSLATION = false;
        final var message = Component.translatable("test.key");
        final var packet = new SystemChatPacket(message, false);
        PacketUtils.sendGroupedPacket(List.of(player), packet);

        collector.assertSingle(received -> {
            assertEquals(message, received.message());
        });
    }

    @Test
    void testItemStackTranslation(final Env env) {
        final var instance = env.createFlatInstance();
        final var connection = env.createConnection();
        final var player = connection.connect(instance, new Pos(0, 40, 0)).join();
        final var collector = connection.trackIncoming(SetSlotPacket.class);

        MinestomAdventure.AUTOMATIC_COMPONENT_TRANSLATION = true;
        final var message = Component.translatable("test.key");
        final var itemStack = ItemStack.of(Material.STONE)
                .with(ItemComponent.ITEM_NAME, message)
                .with(ItemComponent.CUSTOM_NAME, message);
        final var packet = new SetSlotPacket((byte) 0x01, 1, (short) 1, itemStack);
        PacketUtils.sendGroupedPacket(List.of(player), packet);

        collector.assertSingle(received -> {
            assertNotEquals(message, received.itemStack().get(ItemComponent.ITEM_NAME));
            assertNotEquals(message, received.itemStack().get(ItemComponent.CUSTOM_NAME));
        });
    }
}
