package dev.autotool;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.client.player.ClientPreAttackCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public final class AutoToolClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPreAttackCallback.EVENT.register(AutoToolClient::onPreAttack);
    }

    private static boolean onPreAttack(Minecraft client, Player player, int clickCount) {
        if (clickCount == 0 || client.level == null || client.gameMode == null || player.isSpectator()) {
            return false;
        }

        HitResult hit = client.hitResult;
        if (!(hit instanceof BlockHitResult blockHit) || hit.getType() != HitResult.Type.BLOCK) {
            return false;
        }

        BlockPos pos = blockHit.getBlockPos();
        BlockState state = client.level.getBlockState(pos);
        if (state.isAir()) return false;

        Inventory inventory = player.getInventory();
        int bestSlot = inventory.selected;
        float bestSpeed = speedFor(inventory.getItem(bestSlot), state);

        for (int slot = 0; slot < 9; slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.isEmpty()) continue;
            float speed = speedFor(stack, state);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = slot;
            }
        }

        inventory.selected = bestSlot;
        return false;
    }

    private static float speedFor(ItemStack stack, BlockState state) {
        return stack.isEmpty() ? 1.0F : stack.getDestroySpeed(state);
    }
}
