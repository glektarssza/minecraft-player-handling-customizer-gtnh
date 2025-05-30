package com.glektarssza.gtnh_customizer.mixins.early.vanilla;

import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;

import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for the {@code EntityAIOwnerHurtByTarget} class.
 */
@Mixin(EntityAIOwnerHurtByTarget.class)
public class EntityAIOwnerHurtByTargetMixin {
    // -- Right now this does nothing as it inherits from EntityAITarget
}
