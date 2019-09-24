package org.ghotibeaun.json.factory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONFactory;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.exception.JSONRuntimeException;
import org.ghotibeaun.json.parser.JSONParser;
import org.ghotibeaun.json.serializer.JSONSerializer;

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

    /**
     * Key for the {@link JSONFactory} implementation.
     */
    public static final String JSON_FACTORY = "org.ghotibeaun.json.factory";

    /**
     * Key for the {@linkplain JSONParser} implementation
     */
    public static final String JSON_PARSER = "org.ghotibeaun.json.parser";

    /**
     * Key for the {@linkplain JSONSerializer} implementation
     */
    public static final String JSON_SERIALIZER = "org.ghotibeaun.json.serializer";

    /**
     * Key for the {@linkplain JSONObject} implementation
     */
    public static final String JSON_OBJECT = "org.ghotibeaun.json.object";

    /**
     * Key for the {@linkplain JSONArray} implementation
     */
    public static final String JSON_ARRAY = "org.ghotibeaun.json.array";

    /**
     * Key for the {@linkplain JSONValue} implementation (specifically the <code>JSONValue&lt;JSONObject&gt;</code> instance)
     */
    public static final String JSON_VALUE_OBJECT = "org.ghotibeaun.json.value.object";

    /**
     * Key for the {@linkplain JSONValue} implementation (specifically the <code>JSONValue&lt;JSONArray&gt;</code> instance)
     */
    public static final String JSON_VALUE_ARRAY = "org.ghotibeaun.json.value.array";

    /**
     * Key for the {@linkplain JSONValue} implementation (specifically the <code>JSONValue&lt;Boolean&gt;</code> instance)
     */
    public static final String JSON_VALUE_BOOLEAN = "org.ghotibeaun.json.value.boolean";

    /**
     * Key for the {@linkplain JSONValue} implementation (specifically the <code>JSONValue&lt;Date&gt;</code> instance)
     */
    public static final String JSON_VALUE_DATE = "org.ghotibeaun.json.value.date";

    /**
     * Key for the {@linkplain JSONValue} implementation (specifically the <code>JSONValue&lt;NullObject&gt;</code> instance)
     */
    public static final String JSON_VALUE_NULL = "org.ghotibeaun.json.value.null";

    /**
     * Key for the {@linkplain JSONValue} implementation (specifically the <code>JSONValue&lt;Number&gt;</code> instance)
     */
    public static final String JSON_VALUE_NUMBER = "org.ghotibeaun.json.value.number";

    /**
     * Key for the {@linkplain JSONValue} implementation (specifically the <code>JSONValue&lt;String&gt;</code> instance)
     */
    public static final String JSON_VALUE_STRING = "org.ghotibeaun.json.value.string";

    /**
     * Key for the InputStream Character Set
     */
    public static final String JSON_INPUTSTREAM_CHARSET = "org.ghotibeaun.json.inputstream.charset";

    /**
     * Key for the Date format to apply to date objects, uses the {@linkplain SimpleDateFormat syntax}
     */
    public static final String JSON_DATE_FORMAT = "org.ghotibeaun.json.date.format";

    /**
     * Key for the XML Serializer
     */
    public static final String XML_SERIALIZER = "org.ghotibeaun.json.xmlserializer";


    private static FactorySettings instance = null;
    private Properties defaults;
    private boolean useDefaultSettings;

    private FactorySettings() {
        try {
            super.load(this.getClass().getResourceAsStream("/org/ghotibeaun/json/factory/jsonlib.properties"));
            defaults = new Properties();

            for (final Object k : this.keySet()) {
                final String ks = (String) k;

                defaults.setProperty(ks, this.getProperty(ks));
            }

            useDefaultSettings = false;
        } catch (final IOException e) {
            throw new JSONRuntimeException(e);
        }
    }

    private static FactorySettings getInstance() {
        if (instance == null) {
            instance = new FactorySettings();
        }

        return instance;
    }

    /**
     * Applies a setting.  By default, this will configure change use from the default settings to your custom settings when you
     * call {@link #getSetting(String)}. If you wish to use the default again, you can override with {@link #getSetting(String, boolean)}, and
     * apply the setting flag to <code>true</code>.  Alternatively, you can globally override to defaults by calling {@link #setUseDefaultSettings(boolean)}
     * and setting the flag to <code>true</code>.
     * @param name the setting key.  These are defined as constants
     * @param value the setting value
     */
    public static void applySetting(String name, String value) {
        getInstance().setProperty(name, value);
        setUseDefaultSettings(false);
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

    /**
     * Return the default setting
     * @param name the Setting key
     * @return the default setting for this key. It's the equivalent of <code><em>getSetting([settingkey], true)</em></code>. <em>This will not
     * change the "global" flag to use either the default or custom settings</em>
     */
    public static String getDefaultSetting(String name) {
        return getSetting(name, true);
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
