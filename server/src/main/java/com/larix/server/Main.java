package com.larix.server;

import org.apache.commons.cli.*;

import javax.swing.*;

import static java.lang.Integer.parseInt;
import static java.lang.System.err;

public class Main {
    public static void main(final String[] args) {
        // command line options
        final Options options = new Options();
        options.addOption(Option.builder("p").longOpt("port").hasArg().argName("port").desc("Server Port").required().build());

        try {
            // parse options
            final CommandLine cmd = new DefaultParser().parse(options, args);
            final String port = cmd.getOptionValue("p");
            new EspressoServer(parseInt(port)).start();
        } catch (final ParseException e) {
            err.println(e.getMessage());
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Espresso Chat Server", options);
        }
    }
}