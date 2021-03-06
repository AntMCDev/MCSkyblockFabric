package com.ant.mcskyblock.mixin;

import com.ant.mcskyblock.MCSkyBlock;
import com.ant.mcskyblock.skyblock.SkyblockChunkGenerator;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleStructurePiece.class)
public class MixinSimpleStructurePiece {
    @Inject(at = @At("HEAD"), method = "generate", cancellable = true)
    public void generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos, BlockPos pivot, CallbackInfo cir) {
        if (chunkGenerator instanceof SkyblockChunkGenerator) {
            cir.cancel();
        }
    }
}
