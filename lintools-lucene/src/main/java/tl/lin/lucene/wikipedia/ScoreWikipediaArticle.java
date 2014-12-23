package tl.lin.lucene.wikipedia;

import java.io.File;
import java.io.PrintStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ScoreWikipediaArticle {
  private static final String INDEX_OPTION = "index";
  private static final String ID_OPTION = "id";
  private static final String TITLE_OPTION = "title";
  private static final String QUERY_OPTION = "q";

  @SuppressWarnings("static-access")
  public static void main(String[] args) throws Exception {
    Options options = new Options();
    options.addOption(OptionBuilder.withArgName("path").hasArg()
        .withDescription("index location").create(INDEX_OPTION));
    options.addOption(OptionBuilder.withArgName("num").hasArg()
        .withDescription("article id").create(ID_OPTION));
    options.addOption(OptionBuilder.withArgName("string").hasArg()
        .withDescription("article title").create(TITLE_OPTION));
    options.addOption(OptionBuilder.withArgName("string").hasArg()
        .withDescription("query text").create(QUERY_OPTION));

    CommandLine cmdline = null;
    CommandLineParser parser = new GnuParser();
    try {
      cmdline = parser.parse(options, args);
    } catch (ParseException exp) {
      System.err.println("Error parsing command line: " + exp.getMessage());
      System.exit(-1);
    }

    if (!(cmdline.hasOption(ID_OPTION) || cmdline.hasOption(TITLE_OPTION)) || 
        !cmdline.hasOption(INDEX_OPTION) || !cmdline.hasOption(QUERY_OPTION)) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(ScoreWikipediaArticle.class.getName(), options);
      System.exit(-1);
    }

    File indexLocation = new File(cmdline.getOptionValue(INDEX_OPTION));
    if (!indexLocation.exists()) {
      System.err.println("Error: " + indexLocation + " does not exist!");
      System.exit(-1);
    }

    String queryText = cmdline.getOptionValue(QUERY_OPTION);

    WikipediaSearcher searcher = new WikipediaSearcher(indexLocation);
    PrintStream out = new PrintStream(System.out, true, "UTF-8");

    if (cmdline.hasOption(ID_OPTION)) {
      out.println("score: " + searcher.scoreArticle(queryText,
          Integer.parseInt(cmdline.getOptionValue(ID_OPTION))));
    } else {
      out.println("score: " + searcher.scoreArticle(queryText, cmdline.getOptionValue(TITLE_OPTION)));
    }

    searcher.close();
    out.close();
  }
}
