package dev.autotool;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/** Selects the fastest hotbar item for the block under the crosshair. */
public final class AutoToolClient implements ClientModInitializer {
    private static final int HUD_X = 8;
    private static final int HUD_Y = 8;
    private static final int BAR_WIDTH = 76;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(AutoToolClient::onClientTick);
        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath("autotool", "durability_hud"),
                AutoToolClient::renderDurabilityHud
        );
    }

    /** Re-evaluates the targeted block every client tick, without requiring a click. */
    private static void onClientTick(Minecraft client) {
        if (client.player == null || client.level == null
                || client.gameMode == null || client.player.isSpectator()) {
            return;
        }

        HitResult hit = client.hitResult;
        if (!(hit instanceof BlockHitResult blockHit)
                || hit.getType() != HitResult.Type.BLOCK) {
            return;
        }

        BlockPos pos = blockHit.getBlockPos();
        BlockState state = client.level.getBlockState(pos);
        if (state.isAir()) {
            return;
        }

        Inventory inventory = client.player.getInventory();
        int currentSlot = inventory.getSelectedSlot();
        int bestSlot = currentSlot;
        float bestSpeed = speedFor(inventory.getItem(currentSlot), state);

        // Search only the nine hotbar slots. Do not move or rearrange items.
        for (int slot = 0; slot < Inventory.getSelectionSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.isEmpty()) continue;

            float speed = speedFor(stack, state);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = slot;
            }
        }

        if (bestSlot != currentSlot) {
            inventory.setSelectedSlot(bestSlot);
        }
    }

    private static float speedFor(ItemStack stack, BlockState state) {
        return stack.isEmpty() ? 1.0F : stack.getDestroySpeed(state);
    }

    private static void renderDurabilityHud(GuiGraphics graphics, DeltaTracker tickCounter) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.options.hideGui) return;

        Inventory inventory = client.player.getInventory();
        int y = HUD_Y;
        int shown = 0;

        for (int slot = 0; slot < Inventory.getSelectionSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (stack.isEmpty() || !stack.isDamageableItem()) continue;

            int maxDamage = stack.getMaxDamage();
            if (maxDamage <= 0) continue;

            int remaining = Math.max(0, maxDamage - stack.getDamageValue());
            int percent = Math.round(remaining * 100.0F / maxDamage);
            int color = percent <= 10 ? 0xFFFF5555
                    : percent <= 30 ? 0xFFFFAA00 : 0xFF55FF55;

            String label = "[" + (slot + 1) + "] "
                    + stack.getHoverName().getString() + ": " + percent + "%";

            graphics.fill(HUD_X - 3, y - 2, HUD_X + BAR_WIDTH + 7, y + 19, 0x99000000);
            graphics.drawString(client.font, Component.literal(label), HUD_X, y, 0xFFFFFFFF, true);
            graphics.fill(HUD_X, y + 10, HUD_X + BAR_WIDTH, y + 14, 0xFF555555);
            int filledWidth = Math.round(BAR_WIDTH * remaining / (float) maxDamage);
            if (filledWidth > 0) {
                graphics.fill(HUD_X, y + 10, HUD_X + filledWidth, y + 14, color);
            }
            y += 22;
            shown++;
        }

        if (shown == 0) {
            graphics.drawString(client.font, Component.literal("Durabilidade: —"),
                    HUD_X, HUD_Y, 0xFFCCCCCC, true);
        }
    }
}
