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

package com.acmutv.ontoqa.ui;

import com.acmutv.ontoqa.config.AppConfiguration;
import com.acmutv.ontoqa.config.AppConfigurationService;
import com.acmutv.ontoqa.config.AppManifest;
import com.acmutv.ontoqa.config.serial.AppConfigurationFormat;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * The Command Line Interface services.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 */
public class CliService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CliService.class);

  /**
   * Handles the command line arguments passed to the main method, according to {@link BaseOptions}.
   * Loads the configuration and returns the list of arguments.
   * @param argv the command line arguments passed to the main method.
   * @return the arguments list.
   * @see CommandLine
   * @see AppConfiguration
   */
  public static List<String> handleArguments(String[] argv) {
    LOGGER.trace("Processing arguments: {}", Arrays.asList(argv));
    CommandLine cmd = getCommandLine(argv);

    /* OPTION: silent */
    if (cmd.hasOption("silent")) {
      LOGGER.trace("Detected option SILENT");
      activateSilent();
    }

    /* OPTION: trace */
    if (cmd.hasOption("trace")) {
      LOGGER.trace("Detected option TRACE");
      activateTrace();
    }

    /* OPTION: version */
    if (cmd.hasOption("version")) {
      LOGGER.trace("Detected option VERSION");
      printVersion();
      System.exit(0);
    }

    /* OPTION: help */
    if (cmd.hasOption("help")) {
      LOGGER.trace("Detected option HELP");
      printHelp();
      System.exit(0);
    }

    boolean configured = false;
    /* OPTION: config */
    if (cmd.hasOption("config")) {
      final String configPath = cmd.getOptionValue("config");
      LOGGER.trace("Detected option CONFIG with configPath={}", configPath);
      LOGGER.trace("Loading custom configuration {}", configPath);
      try {
        loadConfiguration(configPath);
        configured = true;
      } catch (IOException exc) {
        LOGGER.warn("Cannot load custom configuration");
      }
    }

    if (!configured) {
      final String configPath = AppConfigurationService.DEFAULT_CONFIG_FILENAME;
      LOGGER.trace("Loading local configuration {}", configPath);
      try {
        loadConfiguration(configPath);
        configured = true;
      } catch (IOException exc) {
        LOGGER.warn("Cannot load local configuration");
      }
    }

    if (!configured) {
      LOGGER.trace("Loading default configuration");
      AppConfigurationService.loadDefault();
    }

    LOGGER.trace("Configuration loaded: {}",
        AppConfigurationService.getConfigurations());

    return cmd.getArgList();
  }

  /**
   * Prints on {@code SYTEM_OUT} the string branded with the  {@code APP_NAME}
   * from {@link AppManifest}.
   * @param format the string format.
   * @param args the argument to fill {@code format} with.
   */
  public static void print(String format, Object... args) {
    final String brandedFormat = String.format("[%s]> %s\n", AppManifest.APP_NAME, format);
    System.out.format(brandedFormat, args);
  }

  /**
   * Returns command line options/arguments parsing utility.
   * @param argv The command line arguments passed to the main method.
   * @return The command line options/arguments parsing utility.
   * @see CommandLineParser
   * @see CommandLine
   */
  private static CommandLine getCommandLine(String argv[]) {
    CommandLineParser cmdParser = new DefaultParser();
    CommandLine cmd = null;

    try {
      cmd = cmdParser.parse(BaseOptions.getInstance(), argv);
    } catch (ParseException e) {
      LOGGER.error(e.getMessage());
      printHelp();
    }

    return cmd;
  }

  /**
   * Prints the application version.
   */
  private static void printVersion() {
    System.out.format("%s version %s\n",
        AppManifest.APP_NAME,
        AppManifest.APP_VERSION);
  }

  /**
   * Prints the application command line helper.
   * @see Option
   * @see Options
   */
  private static void printHelp() {
    System.out.format("%s version %s\nTeam: %s (%s)\n\n%s\n\n",
        AppManifest.APP_NAME,
        AppManifest.APP_VERSION,
        AppManifest.APP_TEAM_NAME,
        AppManifest.APP_TEAM_URL,
        AppManifest.APP_DESCRIPTION.replaceAll("(.{80})", "$1\n"));
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp(AppManifest.APP_NAME, BaseOptions.getInstance(), true);
  }

  /**
   * Activates the app silent mode.
   */
  private static void activateSilent() {
    /*
    LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
    LoggerConfig loggerConfig = ctx.getConfiguration().getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
    loggerConfig.setLevel(Level.OFF);
    ctx.updateLoggers();
    */
  }

  /**
   * Activates the app trace mode.
   */
  private static void activateTrace() {
    /*
    LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
    LoggerConfig loggerConfig = ctx.getConfiguration().getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
    loggerConfig.setLevel(Level.ALL);
    ctx.updateLoggers();
    */
  }

  /**
   * Configures the app with the specified YAML configuration file.
   * @param configPath the path to configuration file.
   * @throws IOException when configuration cannot be read.
   */
  private static void loadConfiguration(final String configPath) throws IOException {
    try(InputStream in = new FileInputStream(configPath)) {
      AppConfigurationService.load(AppConfigurationFormat.YAML, in);
    }
  }

  /**
   * Return the user input on {@code STDIN}.
   * @param message The message to ask input.
   * @return The user input.
   */
  public static String getInput(String message) {
    System.out.format("[%s]> %s: ", AppManifest.APP_NAME, message);
    String input;
    Scanner scanner = new Scanner(System.in);
    input = scanner.nextLine();
    return input;
  }
}