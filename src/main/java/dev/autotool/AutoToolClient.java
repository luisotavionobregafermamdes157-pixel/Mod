package dev.autotool;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Selects the fastest item already present in the hotbar when the player
 * starts a new block-mining click.
 */
public final class AutoToolClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPreAttackCallback.EVENT.register(AutoToolClient::onPreAttack);
    }

    private static boolean onPreAttack(Minecraft client, LocalPlayer player, int clickCount) {
        // Fabric calls this event while clicking and while holding the mouse.
        // A non-zero clickCount identifies the initial click.
        if (clickCount == 0
                || client.level == null
                || client.gameMode == null
                || player.isSpectator()) {
            return false;
        }

        HitResult hit = client.hitResult;
        if (!(hit instanceof BlockHitResult blockHit)
                || hit.getType() != HitResult.Type.BLOCK) {
            return false;
        }

        BlockPos pos = blockHit.getBlockPos();
        BlockState state = client.level.getBlockState(pos);
        if (state.isAir()) {
            return false;
        }

        Inventory inventory = player.getInventory();
        int currentSlot = inventory.getSelectedSlot();
        int bestSlot = currentSlot;
        float bestSpeed = speedFor(inventory.getItem(currentSlot), state);

        // Only inspect the hotbar. Do not move or rearrange inventory items.
        for (int slot = 0; slot < Inventory.getSelectionSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }

            float speed = speedFor(stack, state);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = slot;
            }
        }

        if (bestSlot != currentSlot) {
            inventory.setSelectedSlot(bestSlot);
        }

        // Do not cancel the vanilla attack/mining behavior.
        return false;
    }

    private static float speedFor(ItemStack stack, BlockState state) {
        return stack.isEmpty() ? 1.0F : stack.getDestroySpeed(state);
    }
}
