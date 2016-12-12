/*
  The MIT License (MIT)

  Copyright (c) 2016 Antonella Botte, Giacomo Marciani and Debora Partigianoni

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:


  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.


  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
 */

package com.acmutv.ontoqa.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.InputStream;

/**
 * This class realizes ...
 *
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see Configurator
 * @see Yaml
 */
@Data
@AllArgsConstructor
public class Configuration {

  public static boolean DEBUG = false;

  private boolean debug;

  private static Configuration instance;

  /**
   * Retrieves the class singleton.
   * @return the singleton
   */
  public static synchronized Configuration getInstance() {
    if (instance == null) {
      synchronized (Configuration.class) {
        if (instance == null) {
          instance = new Configuration();
        }
      }
    }
    return instance;
  }

  /**
   * Creates the default configuration.
   */
  public Configuration() {
    this.debug = DEBUG;
  }

  public Configuration fromDefault() {
    this.debug = DEBUG;

    return this;
  }

  /**
   * Overwrites the current configuration with the one in the specified YAML file.
   * If something goes wrong, the default configuration is loaded.
   * @param path The absolute path to the configuration file.
   * @return The configuration.
   */
  public Configuration fromYaml(final String path) {
    Yaml yaml = new Yaml(YamlConstructor.getInstance());

    FileReader file;
    try {
      file = new FileReader(path);
    } catch (Exception exc) {
      System.out.println(exc.getMessage());
      return new Configuration();
    }

    Configuration config = yaml.loadAs(file, Configuration.class);

    return this.fromConfig(config);
  }

  /**
   * Overwrites the current configuration with the one in the specified YAML file.
   * If something goes wrong, the default configuration is loaded.
   * @param configStream the configuration YAML file.
   * @return The current configuration.
   */
  public Configuration fromYaml(InputStream configStream) {
    Yaml yaml = new Yaml(YamlConstructor.getInstance());

    Configuration config = yaml.loadAs(configStream, Configuration.class);

    return this.fromConfig(config);
  }

  /**
   * Overwrites the current configuration with the one specified.
   * @param config the configuration to overwrite with.
   * @return The current configuration.
   */
  private Configuration fromConfig(final Configuration config) {
    if (config == null) {
      return this;
    }

    this.debug = config.isDebug();

    return this;
  }
}
