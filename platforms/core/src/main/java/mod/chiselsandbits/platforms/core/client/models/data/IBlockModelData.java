package mod.chiselsandbits.platforms.core.client.models.data;

import org.jetbrains.annotations.Nullable;

public interface IBlockModelData
{
    /**
     * Check if this data has a property, even if the value is {@code null}. Can be
     * used by code that intends to fill in data for a render pipeline.
     *
     * @param prop The property to check for inclusion in this model data
     * @return {@code true} if this data has the given property, even if no value is present
     */
    boolean hasProperty(IModelDataKey<?> prop);

    @Nullable
    <T> T getData(IModelDataKey<T> prop);

    @Nullable
    <T> T setData(IModelDataKey<T> prop, T data);
}
