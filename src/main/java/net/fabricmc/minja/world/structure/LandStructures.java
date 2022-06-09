package net.fabricmc.minja.world.structure;

import com.mojang.serialization.Codec;
import net.fabricmc.minja.Minja;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.PostPlacementProcessor;
import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.structure.StructurePiecesGenerator;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import org.apache.logging.log4j.Level;

import java.util.Optional;

/**
 * Item representing our own-made structures that need to spawn on the land <br><br>
 *
 * LandStructures will be used by the structure json files to determine the place of the structure.
 *
 *
 */
public class LandStructures extends StructureFeature<StructurePoolFeatureConfig> {

    /**
     * Create the pieces layout of the structure and give it to the game
     * @param codec
     */
    public LandStructures(Codec<StructurePoolFeatureConfig> codec) {
        super(codec, LandStructures::createPiecesGenerator, PostPlacementProcessor.EMPTY);
    }

    /**
     * Defines which hostiles mobs will spawn naturally over time in our structures.
     *
     * @Information No other hostiles mobs will spawn in the structure of the same entity classification.
     */
    public static final Pool<SpawnSettings.SpawnEntry> STRUCTURE_MONSTERS = Pool.of(
            new SpawnSettings.SpawnEntry(EntityType.VINDICATOR, 100, 4, 9),
            new SpawnSettings.SpawnEntry(EntityType.WITCH, 100, 4, 9)
    );

    /**
     * Defines which non-hostiles mobs will spawn naturally over time in our structures.
     *
     * @Information No other non-hostiles mobs will spawn in the structure of the same entity classification.
     */
    public static final Pool<SpawnSettings.SpawnEntry> STRUCTURE_CREATURES = Pool.of(
            new SpawnSettings.SpawnEntry(EntityType.SHEEP, 30, 10, 15),
            new SpawnSettings.SpawnEntry(EntityType.RABBIT, 100, 1, 2)
    );

    /**
     * This is where extra checks can be done to determine if the structure can spawn here.
     * This only needs to be overridden if you're adding additional spawn conditions.
     * Basically, this method is used for determining if the land is at a suitable height,
     * if certain other structures are too close or not, or some other restrictive condition.
     * @param context
     * @return whether the structure can spawn here
     */
    private static boolean isFeatureChunk(StructureGeneratorFactory.Context<StructurePoolFeatureConfig> context) {
        BlockPos spawnXZPosition = context.chunkPos().getCenterAtY(0);

        // Grab height of land. Will stop at first non-air block.
        int landHeight = context.chunkGenerator().getHeightInGround(spawnXZPosition.getX(), spawnXZPosition.getZ(), Heightmap.Type.WORLD_SURFACE_WG, context.world());

        // Grabs column of blocks at given position. In overworld, this column will be made of stone, water, and air.
        VerticalBlockSample columnOfBlocks = context.chunkGenerator().getColumnSample(spawnXZPosition.getX(), spawnXZPosition.getZ(), context.world());

        // Combine the column of blocks with land height and you get the top block itself which you can test.
        BlockState topBlock = columnOfBlocks.getState(landHeight);

        // Test to make sure our structure is not spawning on water or other fluids.
        return topBlock.getFluidState().isEmpty(); //landHeight > 100;
    }

    /**
     * Make our structure be generated in the world
     * @param context
     * @return the pieces generator that is now set up so that the game runs it when it needs to create the layout of structure pieces
     * (Returning an empty optional tells the game to skip this spot as it will not generate the structure)
     */
    public static Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> createPiecesGenerator(StructureGeneratorFactory.Context<StructurePoolFeatureConfig> context) {

        // Check if the spot is valid for our structure.
        if (!LandStructures.isFeatureChunk(context)) {
            return Optional.empty();
        }

        // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
        BlockPos blockpos = context.chunkPos().getCenterAtY(0);

        Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> structurePiecesGenerator =
                StructurePoolBasedGenerator.generate(
                        context, // Used for StructurePoolBasedGenerator to get all the proper behaviors done.
                        PoolStructurePiece::new, // Needed in order to create a list of jigsaw pieces when making the structure's layout.
                        blockpos, // Position of the structure. Y value is ignored if last parameter is set to true.
                        false, // Special boundary adjustments for villages. false = make pieces not be partially intersecting.
                        true // Place at heightmap (top land).
                );

        if(structurePiecesGenerator.isPresent()) {
            // This is returning the coordinates of the center starting piece, for debug
            Minja.LOGGER.debug("Land structure at " + blockpos, Level.DEBUG);
        }

        return structurePiecesGenerator;
    }
}