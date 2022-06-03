package net.fabricmc.minja.world.structure;

import net.fabricmc.minja.Minja;
import net.fabricmc.minja.mixin.StructureFeatureAccessor;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class ModStructures {

    /**
     /**
     * Registers the structure itself and sets what its path is. In this case, the
     * structure will have the Identifier of structure_tutorial:run_down_house.
     *
     * It is always a good idea to register your Structures so that other mods and datapacks can
     * use them too directly from the registries. It great for mod/datapacks compatibility.
     */
    public static StructureFeature<?> SKY_STRUCTURES = new SkyStructures(StructurePoolFeatureConfig.CODEC);

    /**
     * This is where we use Fabric API's structure API to setup the StructureFeature
     * See the comments in below for more details.
     */
    public static void RegisterStructureFeatures() {

        // This is Fabric API's builder for structures.
        // It has many options to make sure your structure will spawn and work properly.
        // Give it your structure and the identifier you want for it.
        StructureFeatureAccessor.callRegister("minja:sky_structures", SKY_STRUCTURES, GenerationStep.Feature.SURFACE_STRUCTURES);
    }
}