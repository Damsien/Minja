package net.fabricmc.minja.world.structure;

import net.fabricmc.minja.Minja;
import net.fabricmc.minja.mixin.StructureFeatureAccessor;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

/*
 * ModStructures is used to register all of our own-made structures into the minecraft server
 */
public class ModStructures {

    /**
     /**
     * Registers the structure itself and sets what its path is.
     */
    public static StructureFeature<?> SKY_STRUCTURES = new SkyStructures(StructurePoolFeatureConfig.CODEC);

    /**
     * This method the mixin class (StructureFeatureAccessor) to add our structure to minecraft world
     */
    public static void RegisterStructureFeatures() {

        StructureFeatureAccessor.callRegister("minja:sky_structures", SKY_STRUCTURES, GenerationStep.Feature.SURFACE_STRUCTURES);
    }
}