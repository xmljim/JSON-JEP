package org.ghotibeaun.json.factory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.ghotibeaun.json.JSONFactory;
import org.ghotibeaun.json.converters.utils.ClassUtils;
import org.ghotibeaun.json.exception.JSONFactoryException;
import org.ghotibeaun.json.exception.JSONRuntimeException;

/**
 * Holds all the various factories' settings for instantiation of various JSON objects. All keys defined have
 * default settings, which can be overridden with the appropriate implementation for that interface.
 *
 * For example, if you created a different implementation for the {@linkplain JSONFactory}, before you invoked the factory,
 * you call the {@linkplain FactorySettings#applySetting(String, String)} method using the {@link #JSON_FACTORY} key with the
 * classname of your new implementation
 *
 * <pre>
 * FactorySettings.applySetting(FactorySettings.JSON_FACTORY, "com.example.MyJsonFactory");
 * </pre>
 *
 * By default, this will change the settings from the default settings to your modified configuration. However, if you wanted to go back to the
 * default settings, you can set the {@link #setUseDefaultSettings(boolean)} to <code>true</code>
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
public class FactorySettings extends Properties {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static FactorySettings instance = null;
    private Properties defaults;
    private boolean useDefaultSettings;
    private final FactoryClassCache cache;
    private FactorySettings() {
        cache = new FactoryClassCache();

        try {
            super.load(this.getClass().getResourceAsStream("/org/ghotibeaun/json/factory/jsonlib.properties"));
            defaults = new Properties();

            for (final Object k : this.keySet()) {
                final String ks = (String) k;

                defaults.setProperty(ks, this.getProperty(ks));

                //final boolean isClass = !Arrays.stream(excludeFromCache).anyMatch(key -> key.equals(ks));

                final Setting setting = Setting.fromPropertyName(ks);
                if (setting != null && setting.isClassValue()) {
                    cache.cacheClass(ks, this.getProperty(ks));
                }
            }

            useDefaultSettings = false;

        } catch (final IOException | ClassNotFoundException e) {
            throw new JSONRuntimeException(e);
        }
    }

    private static FactorySettings getInstance() {
        if (instance == null) {
            instance = new FactorySettings();
        }

        return instance;
    }

    public static <T> T createFactoryClass(Setting setting) throws JSONFactoryException {
        if (setting.isClassValue()) {
            return createFactoryClass(setting.getPropertyName());
        } else {
            throw new JSONFactoryException("Setting [" + setting.getPropertyName() + "] is not a cached class");
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T createFactoryClass(String key) throws JSONFactoryException {
        final Optional<Class<?>> clazz = getInstance().getCache().getClassFromCache(key);
        if (clazz.isPresent()) {
            //System.out.println("** Create Instance [" + key + "] -- " + clazz.get().getName());
            return (T)ClassUtils.createInstance(clazz.get());
        } else {
            throw new JSONFactoryException("No class found for key: " + key);
        }
    }

    public static Optional<Class<?>> getFactoryClass(String key) {
        return getInstance().getCache().getClassFromCache(key);
    }

    public static Optional<Class<?>> getFactoryClass(Setting setting) {
        return getFactoryClass(setting.getPropertyName());
    }

    public FactoryClassCache getCache() {
        return cache;
    }

    /**
     * Applies a setting.  By default, this will configure change use from the default settings to your custom settings when you
     * call {@link #getSetting(String)}. If you wish to use the default again, you can override with {@link #getSetting(String, boolean)}, and
     * apply the setting flag to <code>true</code>.  Alternatively, you can globally override to defaults by calling {@link #setUseDefaultSettings(boolean)}
     * and setting the flag to <code>true</code>.
     * @param name the setting key.  These are defined as constants
     * @param value the setting value
     */
    public static void applySetting(String name, String value) throws JSONFactoryException {
        getInstance().setProperty(name, value);
        final Setting setting = Setting.fromPropertyName(name);
        if (setting != null && setting.isClassValue()) {
            try {
                getInstance().getCache().cacheClass(name, value);
            } catch (final ClassNotFoundException e) {
                throw new JSONFactoryException(e);
            }
        }
        setUseDefaultSettings(false);
    }

    public static void applySetting(Setting setting, String value) throws JSONFactoryException {
        getInstance().setProperty(setting.getPropertyName(), value);
        setUseDefaultSettings(false);
        if (setting.isClassValue()) {
            try {
                getInstance().getCache().cacheClass(setting.getPropertyName(), value);
            } catch (final ClassNotFoundException e) {
                throw new JSONFactoryException(e);
            }
        }
    }

    /**
     * Returns a setting value
     * @param name the setting key
     * @return the setting value. The setting value will be determined based on whether {@link #getUseDefaultSettings()} is <code>true</code>
     * or <code>false</code>. If <code>true</code>, then the out of the box settings and configuration will be used. If <code>false</code>,
     * then the modified settings value will be used. Note that if you haven't changed that particular setting, then the default and custom
     * settings will be the same, until changed.
     */
    public static String getSetting(String name) {
        return getSetting(name, getInstance().useDefaultSettings);
    }

    public static String getSetting(Setting setting) {
        return getSetting(setting.getPropertyName());
    }

    /**
     * Returns a setting value from either the default or custom settings
     * @param name the setting key
     * @param useDefault flag to indicate whether to use the default (<code>true</code>) or custom (<code>false</code>) settings
     * @return The setting value. If <code>useDefault</code> is set to <code>true</code> then the setting's value will be returned from the
     * default (out of the box) settings. If the value is <code>false</code> then the custom settings will be used. <em>This will not
     * change the "global" flag to use either the default or custom settings</em>
     */
    public static String getSetting(String name, boolean useDefault) {
        if (useDefault == true) {
            return getInstance().defaults.getProperty(name);
        } else {
            return getInstance().getProperty(name);
        }
    }

    public static String getSetting(Setting setting, boolean useDefault) {
        return getSetting(setting.getPropertyName(), useDefault);
    }

    /**
     * Return the default setting
     * @param name the Setting key
     * @return the default setting for this key. It's the equivalent of <code><em>getSetting([settingkey], true)</em></code>. <em>This will not
     * change the "global" flag to use either the default or custom settings</em>
     */
    public static String getDefaultSetting(String name) {
        return getSetting(name, true);
    }

    public static String getDefaultSetting(Setting setting) {
        return getDefaultSetting(setting.getPropertyName());
    }

    /**
     * Return the custom setting
     * @param name the Setting key
     * @return the custom setting for this key. It's the equivalent of <code><em>getSetting([settingkey], false)</em></code>. <em>This will not
     * change the "global" flag to use either the default or custom settings</em>
     */
    public static String getCustomSetting(String name) {
        return getSetting(name, false);
    }

    public static String getCustomSetting(Setting setting) {
        return getCustomSetting(setting.getPropertyName());
    }

    /**
     * Global sets which configuration settings to use.
     * @param use If set to <code>true</code>, then the default settings will be used; if
     * set to <code>false</code>, then the custom settings will be applied
     */
    public static void setUseDefaultSettings(boolean use) {
        getInstance().useDefaultSettings = use;
    }

    /**
     * Returns whether the default or custom settings are used
     * @return <code>true</code> if the Default (out of the box) settings are used globally; if <code>false</code>, the
     * custom settings will be applied globally. This setting informs the {@link #getSetting(String)} method.
     */
    public static boolean getUseDefaultSettings() {
        return getInstance().useDefaultSettings;
    }

    private class FactoryClassCache {
        private final Map<String, Class<?>> cache = new HashMap<>();

        public FactoryClassCache() {

        }

        public void cacheClass(String key, String className) throws ClassNotFoundException {
            final Class<?> clazz = Class.forName(className, true, this.getClass().getClassLoader());
            //System.out.println("Caching class: [" + key + "] -- " + clazz.getName());
            cache.put(key, clazz);
        }

        public Optional<Class<?>> getClassFromCache(String key) {
            return Optional.ofNullable(cache.get(key));
        }



    }
}
