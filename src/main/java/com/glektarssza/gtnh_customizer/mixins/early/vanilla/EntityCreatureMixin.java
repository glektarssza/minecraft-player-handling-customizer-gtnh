package com.glektarssza.gtnh_customizer.mixins.early.vanilla;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.glektarssza.gtnh_customizer.api.immunity.ITargetingImmunity;
import com.glektarssza.gtnh_customizer.utils.ImmunityUtils;
import com.glektarssza.gtnh_customizer.utils.PlayerUtils;

/**
 * Mixin for the {@link EntityCreature} class.
 */
@Mixin(EntityCreature.class)
public class EntityCreatureMixin {

    /**
     * A shadow of the {@code entityToAttack} field.
     */
    @Shadow
    private Entity entityToAttack;

    /**
     * Mixin for the {@link EntityCreature#setTarget} method.
     */
    @Inject(method = "setTarget", at = @At(value = "HEAD"), cancellable = true)
    public void overrideSetTarget(Entity target, CallbackInfo ci) {
        EntityCreature self = (EntityCreature) (Object) this;
        EntityLiving attacker = (EntityLiving) self;
        if (target == null) {
            return;
        }
        if (!(target instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) target;
        List<ITargetingImmunity> immunities = PlayerUtils
            .getPlayerTargetingImmunities(player);
        if (ImmunityUtils.entityMatchesAnyTargetingImmunity(attacker,
            immunities)
            || PlayerUtils.getIsPlayerGloballyImmune(player)) {
            ci.cancel();
        }
    }

    /**
     * Mixin for the {@link EntityCreature#updateEntityActionState} method.
     */
    @Inject(method = "updateEntityActionState", at = @At(value = "INVOKE_ASSIGN", target = "net.minecraft.entity.EntityCreature.findPlayerToAttack()Lnet/minecraft/entity/Entity;"))
    public void overrideTargetedEntity(CallbackInfo ci) {
        EntityCreature self = (EntityCreature) (Object) this;
        EntityLiving attacker = self;
        Entity target = this.entityToAttack;
        if (target == null) {
            return;
        }
        if (!(target instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) target;
        List<ITargetingImmunity> immunities = PlayerUtils
            .getPlayerTargetingImmunities(player);
        if (ImmunityUtils.entityMatchesAnyTargetingImmunity(attacker,
            immunities)
            || PlayerUtils.getIsPlayerGloballyImmune(player)) {
            this.entityToAttack = null;
        }
    }
}
