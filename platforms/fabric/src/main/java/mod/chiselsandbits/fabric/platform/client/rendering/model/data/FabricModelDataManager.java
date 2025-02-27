package mod.chiselsandbits.fabric.platform.client.rendering.model.data;

import mod.chiselsandbits.platforms.core.client.models.data.IModelDataManager;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class FabricModelDataManager implements IModelDataManager
{
    private static final FabricModelDataManager INSTANCE = new FabricModelDataManager();

    public static FabricModelDataManager getInstance()
    {
        return INSTANCE;
    }

    private FabricModelDataManager()
    {
    }

    @Override
    public void requestModelDataRefresh(final BlockEntity blockEntity)
    {
        // NO-OP
    }
}
