cat > src/main/java/com/grok/minesweeper/MinesweeperMod.java << 'EOF'
package com.grok.minesweeper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MinesweeperMod implements ClientModInitializer {

    private static KeyBinding minesweeperKey;

    @Override
    public void onInitializeClient() {
        minesweeperKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.minesweeper.open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                "category.minesweeper"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (minesweeperKey.wasPressed()) {
                client.setScreen(new MinesweeperScreen());
            }
        });
    }
}
EOF
