package mod.chiselsandbits.fabric.platform.configuration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;
import java.util.function.Supplier;

public class FabricConfigurationValue<T> implements Supplier<T>
{
    private final Logger LOGGER = LogManager.getLogger();

    protected final FabricConfigurationSource source;
    protected final String key;
    protected final Function<JsonElement, T> adapter;

    protected T value = null;
    protected final T defaultValue;

    public FabricConfigurationValue(final FabricConfigurationSource source, final String key, final Function<JsonElement, T> adapter, final T defaultValue) {this.source = source;
        this.key = key;
        this.adapter = adapter;
        this.defaultValue = defaultValue;
    }

    @Override
    public T get()
    {
        if (value != null) {
            return value;
        }

        try {
            final JsonElement valueElement = resolve();
            final T value = adapter.apply(valueElement);
            this.value = verify(value);
            return value;
        } catch (KeyResolveException e) {
            LOGGER.error(e.getMessage(), e);
            value = defaultValue;
            return value;
        } catch (Exception e) {
            LOGGER.error("General failure during configuration parsing", e);
            value = defaultValue;
            return value;
        }
    }

    protected T verify(final T value) {
        return value;
    }

    public void resetCache() {
        this.value = null;
    }

    private JsonElement resolve() {
        final JsonObject sourceObject = source.getConfig();

        final JsonObject parentKey = resolveFirstParent(key, sourceObject);
        if (parentKey == null) {
            throw new KeyResolveException(key, source.getName());
        }

        final String valueKey = getValueKey(key);
        final JsonElement element = parentKey.get(valueKey);
        if (element == null) {
            throw new KeyResolveException(key, source.getName());
        }

        return element;
    }

    private static JsonObject resolveFirstParent(final String key, final JsonObject source) {
        final String[] parts = key.split("\\.");
        JsonObject current = source;

        for (int i = 0; i < parts.length; i++) {
            final String part = parts[i];
            final JsonElement element = current.get(part);

            if (element == null) {
                return null;
            }

            if (i == parts.length - 1) {
                return current;
            }

            if (element.isJsonObject()) {
                current = element.getAsJsonObject();
            } else {
                return null;
            }
        }

        return null;
    }

    private static String getValueKey(final String key) {
        final String[] parts = key.split("\\.");
        return parts[parts.length - 1];
    }

    private static final class KeyResolveException extends IllegalStateException {
        public KeyResolveException(final String key, final String configurationName) {
            super("Could not resolve configuration key: " + key + " in: " + configurationName);
        }
    }
}
