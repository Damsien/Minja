package net.fabricmc.minja.world.structure;

import net.fabricmc.minja.Minja;
import net.fabricmc.minja.mixin.StructureFeatureAccessor;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

/*
 *
 */
/**
 * ModStructures is used to register all of our own-made structures into the minecraft server. <br><br>
 *
 * @author 	Camille Perrin
 *
 */
public class ModStructures {

     /**
     * Creates an instance of each structure type
     */
    public static StructureFeature<?> SKY_STRUCTURES = new SkyStructures(StructurePoolFeatureConfig.CODEC);
    public static StructureFeature<?> LAND_STRUCTURES = new LandStructures(StructurePoolFeatureConfig.CODEC);

    /**
     * Call the mixin class (StructureFeatureAccessor) to add our structure to minecraft world :
     * Registers each structure type and sets what its path is.
     */
    public static void RegisterStructureFeatures() {

        StructureFeatureAccessor.callRegister("minja:sky_structures", SKY_STRUCTURES, GenerationStep.Feature.SURFACE_STRUCTURES);
        StructureFeatureAccessor.callRegister("minja:land_structures", LAND_STRUCTURES, GenerationStep.Feature.SURFACE_STRUCTURES);
    }
}