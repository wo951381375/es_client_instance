//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package es.utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

public class SysDataRequest {
        private static PropertiesConfiguration config = null;
        private static String GLOBLE_SYSCONFIG_FILE = "esconf.properties";

        private SysDataRequest() {
                try {
                        config = new PropertiesConfiguration();
                        config.setEncoding("UTF-8");
                        config.load(GLOBLE_SYSCONFIG_FILE);
                        config.setReloadingStrategy(new FileChangedReloadingStrategy());
                        config.setThrowExceptionOnMissing(false);
                } catch (ConfigurationException var2) {
                        var2.printStackTrace();
                }

        }

        public static SysDataRequest getInstance() {
                return SysDataRequest.SysGlobalConfigHolder.instance;
        }

        public String getString(String key) {
                return config == null ? null : config.getString(key);
        }

        public int getInt(String key) {
                return config.getInt(key);
        }

        private static class SysGlobalConfigHolder {
                private static SysDataRequest instance = new SysDataRequest();

                private SysGlobalConfigHolder() {
                }
        }
}
