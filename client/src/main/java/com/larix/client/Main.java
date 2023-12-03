package com.larix.client;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialOceanicIJTheme;
import com.larix.client.view.EspressoClient;
import org.apache.commons.cli.*;

import javax.swing.*;

import static com.formdev.flatlaf.util.SystemInfo.isMacOS;
import static java.lang.Integer.parseInt;
import static java.lang.System.err;
import static java.lang.System.setProperty;

public class Main {

    public static void main(final String[] args) {
        // set macOS properties for look and feel
        if (isMacOS) {
            setProperty("apple.awt.application.name", "Espresso Chat");
            setProperty("apple.awt.application.appearance", "system");
        }

        // command line options
        final Options options = new Options();
        options.addOption(Option.builder("u").longOpt("username").hasArg().argName("username").desc("Username").required().build());
        options.addOption(Option.builder("h").longOpt("host").hasArg().argName("host").desc("Server Hostname").required().build());
        options.addOption(Option.builder("p").longOpt("port").hasArg().argName("port").desc("Server Port").required().build());

        try {
            // parse options
            final CommandLine cmd = new DefaultParser().parse(options, args);
            final String username = cmd.getOptionValue("u");
            final String host = cmd.getOptionValue("h");
            final String port = cmd.getOptionValue("p");

            SwingUtilities.invokeLater(() -> startApp(username, host, port));
        } catch (final ParseException e) {
            err.println(e.getMessage());
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Espresso Chat Client", options);
        }
    }

    private static void startApp(final String username, final String host, final String port) {
        FlatMaterialOceanicIJTheme.setup();
        new EspressoClient(username, host, parseInt(port)).start();
    }

}