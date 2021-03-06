package com.ant.mcskyblock.mixin;

import com.ant.mcskyblock.skyblock.SkyblockChunkGenerator;
import net.minecraft.block.Blocks;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.EndPortalFeature;
import net.minecraft.world.gen.feature.FeatureConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonFight.class)
public class MixinEnderDragonFight {
    @Shadow
    private BlockPos exitPortalLocation;

    @Final
    @Shadow
    private ServerWorld world;

    @Inject(at = @At("HEAD"), method = "generateEndPortal", cancellable = true)
    private void generateEndPortal(boolean previouslyKilled, CallbackInfo ci) {
        if (world.getChunkManager().getChunkGenerator() instanceof SkyblockChunkGenerator) {
            EndPortalFeature endPortalFeature = new EndPortalFeature(previouslyKilled);
            if (this.exitPortalLocation == null) {
                for (this.exitPortalLocation = this.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPortalFeature.ORIGIN).down(); this.world.getBlockState(this.exitPortalLocation).isOf(Blocks.BEDROCK) && this.exitPortalLocation.getY() > this.world.getSeaLevel(); this.exitPortalLocation = this.exitPortalLocation.down()) {
                }
            }

            BlockPos.Mutable pos = this.exitPortalLocation.mutableCopy();
            pos.setY(Math.max(this.exitPortalLocation.getY(), 2));
            this.exitPortalLocation = pos.toImmutable();

            endPortalFeature.generateIfValid(FeatureConfig.DEFAULT, this.world, this.world.getChunkManager().getChunkGenerator(), Random.create(), this.exitPortalLocation);

            ci.cancel();
        }
    }
}
