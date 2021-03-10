/*
 *
 * # Released under MIT License
 *
 * Copyright (c) 2016-2021 Jim Earley.
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 */
package org.ghotibeaun.json.factory;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Optional;
import java.util.Properties;

import org.ghotibeaun.json.JSONFactory;
import org.ghotibeaun.json.converters.utils.ClassUtils;
import org.ghotibeaun.json.exception.JSONFactoryException;
import org.ghotibeaun.json.exception.JSONRuntimeException;
import org.ghotibeaun.json.factory.setting.ClassSetting;
import org.ghotibeaun.json.factory.setting.FactorySetting;
import org.ghotibeaun.json.factory.setting.StringSetting;

/**
 * Holds all the various factories' settings for instantiation of various JSON objects. All keys defined have
 * default settings, which can be overridden with the appropriate implementation for that interface.
 *
 * For example, if you created a different implementation for the {@linkplain JSONFactory}, before you invoked the factory,
 * you call the {@linkplain FactorySettings#applySetting(String, String)} method using the {@link #JSON_FACTORY} key with the
 * classname of your new implementation
 *
 * <pre>
 * FactorySettings.applySetting(Setting.FACTORY_CLASS, "com.example.MyJsonFactory");
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
    //private Properties defaults;
    private boolean useDefaultSettings;
    //private final FactoryClassCache defaultCache;
    //private final FactoryClassCache customCache;

    private final EnumMap<Setting, FactorySetting<?>> settingMap = new EnumMap<>(Setting.class);

    private FactorySettings() {

        try {
            super.load(this.getClass().getResourceAsStream("/org/ghotibeaun/json/factory/jsonlib.properties"));
            defaults = new Properties();

            for (final Object k : this.keySet()) {
                final String ks = (String) k;
                defaults.setProperty(ks, this.getProperty(ks));

                //final boolean isClass = !Arrays.stream(excludeFromCache).anyMatch(key -> key.equals(ks));

                final Setting setting = Setting.fromPropertyName(ks);

                if (setting != null) {
                    if (setting.isClassValue()) {
                        settingMap.put(setting, new ClassSetting(this.getProperty(ks)));
                    } else {
                        settingMap.put(setting, new StringSetting(this.getProperty(ks)));
                    }
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

    @SuppressWarnings("unchecked")
    public static <T> T createFactoryClass(Setting setting) throws JSONFactoryException {
        final Optional<Class<?>> clazz = getFactoryClass(setting);

        if (clazz.isPresent()) {
            return (T)ClassUtils.createInstance(clazz.get());
        } else {
            throw new JSONFactoryException("Setting [" + setting.getPropertyName() + "] is not a cached class");
        }
    }


    public static Optional<Class<?>> getFactoryClass(String key) {
        final Setting classSetting = Setting.fromPropertyName(key);

        return getFactoryClass(classSetting);
    }

    public static Optional<Class<?>> getFactoryClass(Setting setting) {
        if (setting == null) {
            return Optional.of(null);
        } else if (setting.isClassValue()) {
            final ClassSetting classSetting = (ClassSetting)getInstance().settingMap.get(setting);

            return Optional.of(getUseDefaultSettings() ? classSetting.getDefaultValue() :  classSetting.getValue());
        } else {
            return Optional.ofNullable(null);
        }
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
        final Setting setting = Setting.fromPropertyName(name);
        applySetting(setting, value);

        setUseDefaultSettings(false);
    }

    public static void applySetting(Setting setting, String value) throws JSONFactoryException {
        getInstance().setProperty(setting.getPropertyName(), value);
        setUseDefaultSettings(false);

        if (setting != null) {
            if (setting.isClassValue()) {
                final ClassSetting classSetting = (ClassSetting)getInstance().settingMap.get(setting);
                try {
                    classSetting.setValue(Class.forName(value));
                } catch (final ClassNotFoundException e) {
                    throw new JSONFactoryException(e.getMessage(), e);
                }
            } else {
                final StringSetting stringSetting = (StringSetting)getInstance().settingMap.get(setting);
                stringSetting.setValue(value);
            }
        }

    }

    public static void applySetting(Setting setting, Class<?> clazz) {
        final ClassSetting classSetting = (ClassSetting)getInstance().settingMap.get(setting);
        classSetting.setValue(clazz);
    }

    public static Class<?> getClassSetting(Setting setting) {
        final ClassSetting classSetting = (ClassSetting)getInstance().settingMap.get(setting);

        if (classSetting != null) {
            return classSetting.getValue();
        } else {
            return null;
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
        final Setting setting = Setting.fromPropertyName(name);

        if (setting != null) {
            return getSetting(setting);
        } else {
            return getInstance().getProperty(name);
        }
    }

    public static String getSetting(Setting setting) {
        if (setting != null) {
            if (setting.isClassValue()) {
                final ClassSetting classSetting = (ClassSetting)getInstance().settingMap.get(setting);
                if (classSetting != null) {
                    return classSetting.toString();
                } else {
                    return null;
                }
            } else {
                final StringSetting stringSetting = (StringSetting)getInstance().settingMap.get(setting);
                if (stringSetting != null) {
                    return stringSetting.toString();
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
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
            final Setting setting = Setting.fromPropertyName(name);
            return getSetting(setting, useDefault);
        } else {
            return getInstance().getProperty(name);
        }
    }

    public static String getSetting(Setting setting, boolean useDefault) {
        final FactorySetting<?> factorySetting = getInstance().settingMap.get(setting);

        if (useDefault) {
            return factorySetting.toDefaultString();
        } else {
            return factorySetting.toString();
        }
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
        return getSetting(setting, true);
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
        return getSetting(setting, false);
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

}
