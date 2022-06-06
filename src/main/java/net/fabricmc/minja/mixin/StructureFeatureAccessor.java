package net.fabricmc.minja.mixin;

import net.minecraft.util.CuboidBlockIterator;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/*
 * StructureFeatureAccessor is used to access the mixin of minecraft structure.
 * It is used to put our own-made structures in minecraft
 */
@Mixin(StructureFeature.class)
public interface StructureFeatureAccessor {
    @Invoker
    static <F extends StructureFeature<?>> F callRegister(String name, F structureFeature, GenerationStep.Feature step){
        throw new UnsupportedOperationException();
    }
}
