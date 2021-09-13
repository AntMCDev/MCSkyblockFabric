package com.ant.mcskyblock.world;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class SkyblockWorldType extends GeneratorType {
    public SkyblockWorldType(String string) {
        super(string);
        GeneratorType.VALUES.add(this);
    }

    @Override
    public GeneratorOptions createDefaultOptions(DynamicRegistryManager.Impl registryManager, long seed, boolean generateStructures, boolean bonusChest) {
        Registry<Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);
        Registry<DimensionType> dimensionRegistry = registryManager.get(Registry.DIMENSION_TYPE_KEY);
        Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry = registryManager.get(Registry.CHUNK_GENERATOR_SETTINGS_KEY);
        SimpleRegistry<DimensionOptions> simpleRegistry = new SimpleRegistry(Registry.DIMENSION_KEY, Lifecycle.stable());

        simpleRegistry.add(DimensionOptions.NETHER, new DimensionOptions(() -> {
            return (DimensionType)dimensionRegistry.getOrThrow(DimensionType.THE_NETHER_REGISTRY_KEY);
        }, createNetherGenerator(biomeRegistry, seed)), Lifecycle.stable());
        simpleRegistry.add(DimensionOptions.END, new DimensionOptions(() -> {
            return (DimensionType)dimensionRegistry.getOrThrow(DimensionType.THE_END_REGISTRY_KEY);
        }, createEndGenerator(biomeRegistry, seed)), Lifecycle.stable());

        simpleRegistry = GeneratorOptions.getRegistryWithReplacedOverworldGenerator(dimensionRegistry, simpleRegistry, new SkyblockChunkGenerator(new VanillaLayeredBiomeSource(seed, false, false, biomeRegistry), seed));

        return new GeneratorOptions(seed, generateStructures, bonusChest, simpleRegistry);
    }

    @Override
    protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
        return new SkyblockChunkGenerator(new VanillaLayeredBiomeSource(seed, false, false, biomeRegistry), seed);
    }

    private static ChunkGenerator createEndGenerator(Registry<Biome> biomeRegistry, long seed) {
        return new SkyblockChunkGenerator(new TheEndBiomeSource(biomeRegistry, seed), seed);
    }

    private static ChunkGenerator createNetherGenerator(Registry<Biome> biomeRegistry, long seed) {
        return new SkyblockChunkGenerator(MultiNoiseBiomeSource.Preset.NETHER.getBiomeSource(biomeRegistry, seed), seed);
    }
}