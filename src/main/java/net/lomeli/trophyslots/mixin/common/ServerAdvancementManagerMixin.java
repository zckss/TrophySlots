package net.lomeli.trophyslots.mixin.common;

import net.lomeli.trophyslots.core.AdvancementHandler;
import net.minecraft.advancement.ServerAdvancementManager;
import net.minecraft.advancement.SimpleAdvancement;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerAdvancementManager.class)
public class ServerAdvancementManagerMixin {

    @Shadow
    private ServerPlayerEntity field_13391;

    @Inject(method = "onAdvancement",
            at = @At(shift = At.Shift.AFTER,
                    value = "INVOKE",
                    target = "Lnet/minecraft/advancement/AdvancementRewards;apply(Lnet/minecraft/server/network/ServerPlayerEntity;)V",
                    opcode = Opcodes.INVOKEVIRTUAL))
    private void onAdvancement(SimpleAdvancement advancement, String critereon, CallbackInfoReturnable<Boolean> info) {
        AdvancementHandler.unlockedAdvancment(field_13391, advancement);
    }
}
