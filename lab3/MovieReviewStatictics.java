import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MovieReviewStatictics
{
    private static final String DOCUMENTS_PATH = "movies/";
    private int _verbCount = 0;
    private int _nounCount = 0;
    private int _adjectiveCount = 0;
    private int _adverbCount = 0;
    private int _totalTokensCount = 0;

    private PrintStream _statisticsWriter;

    private SentenceModel _sentenceModel;
    private TokenizerModel _tokenizerModel;
    private DictionaryLemmatizer _lemmatizer;
    private PorterStemmer _stemmer;
    private POSModel _posModel;
    private TokenNameFinderModel _peopleModel;
    private TokenNameFinderModel _placesModel;
    private TokenNameFinderModel _organizationsModel;

    public static void main(String[] args)
    {
        MovieReviewStatictics statictics = new MovieReviewStatictics();
        statictics.run();
    }

    private void run()
    {
        try
        {
            initModelsStemmerLemmatizer();

            File dir = new File(DOCUMENTS_PATH);
            File[] reviews = dir.listFiles((d, name) -> name.endsWith(".txt"));

            _statisticsWriter = new PrintStream("statistics.txt", "UTF-8");

            Arrays.sort(reviews, Comparator.comparing(File::getName));
            for (File file : reviews)
            {
                System.out.println("Movie: " + file.getName().replace(".txt", ""));
                _statisticsWriter.println("Movie: " + file.getName().replace(".txt", ""));

                String text = new String(Files.readAllBytes(file.toPath()));
                processFile(text);

                _statisticsWriter.println();
            }

            overallStatistics();
            _statisticsWriter.close();

        } catch (IOException ex)
        {
            Logger.getLogger(MovieReviewStatictics.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initModelsStemmerLemmatizer()
    {
        try
        {
            File modelFile1 = new File("models/en-lemmatizer.dict");
            File modelFile2 = new File("models/en-token.bin");
            File modelFile3 = new File("models/en-sent.bin");
            File modelFile4 = new File("models/en-pos-maxent.bin");
            File modelFile5 = new File("models/en-ner-person.bin");
            File modelFile6 = new File("models/en-ner-organization.bin");
            File modelFile7 = new File("models/en-ner-location.bin");

            _lemmatizer = new DictionaryLemmatizer(modelFile1);
            _stemmer = new PorterStemmer();
            _sentenceModel = new SentenceModel(modelFile3);
            _tokenizerModel = new TokenizerModel(modelFile2);
            _organizationsModel = new TokenNameFinderModel(modelFile6);
            _peopleModel = new TokenNameFinderModel(modelFile5);
            _placesModel = new TokenNameFinderModel(modelFile7);
            _posModel = new POSModel(modelFile4);

            //String[] lemy = _lemmatizer.lemmatize(text, tags);
            //String[] stemy = new String[text.length];
            //for (int i=0; i<text.length; i++) {
            //    stemy[i] = _stemmer.stem(text[i]);
            //}


            // TODO: load all OpenNLP models (+Porter stemmer + lemmatizer)
            // from files (use class variables)

        } catch (IOException ex)
        {
            Logger.getLogger(MovieReviewStatictics.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void processFile(String text)
    {
        // TODO: process the text to find the following statistics:
        // For each movie derive:
        //    - number of sentences
        int noSentences = 0;
        //    - number of tokens
        int noTokens = 0;
        //    - number of (unique) stemmed forms
        int noStemmed = 0;
        //    - number of (unique) words from a dictionary (lemmatization)
        int noWords = 0;
        //    -  people
        Span people[] = new Span[] { };
        //    - locations
        Span locations[] = new Span[] { };
        //    - organisations
        Span organisations[] = new Span[] { };

        // TODO + compute the following overall (for all movies) POS tagging statistics:
        //    - percentage number of adverbs (class variable, private int _verbCount = 0)
        //    - percentage number of adjectives (class variable, private int _nounCount = 0)
        //    - percentage number of verbs (class variable, private int _adjectiveCount = 0)
        //    - percentage number of nouns (class variable, private int _adverbCount = 0)
        //    + update _totalTokensCount



        // ------------------------------------------------------------------

        // TODO derive sentences (update noSentences variable)
        SentenceDetectorME zdania_podzial = new SentenceDetectorME(_sentenceModel);
        String[] zdania = zdania_podzial.sentDetect(text);
        noSentences = zdania.length;


        // TODO derive tokens and POS tags from text
        TokenizerME object = new TokenizerME(_tokenizerModel);
        String[] tokens = object.tokenize(text);
        noTokens = tokens.length;
        POSTaggerME object2 = new POSTaggerME(_posModel);
        String[] tags = object2.tag(tokens);
        //_totalTokensCount += noTokens;
        // (update noTokens and _totalTokensCount)

        // TODO perform stemming (use derived tokens)
        // (update noStemmed)
        Set <String> stems = new HashSet <>();

        //for (String token : tokens)
        //{
            // use .toLowerCase().replaceAll("[^a-z0-9]", ""); thereafter, ignore "" tokens
        //}

        for (String token : tokens)
        {
            stems.add(_stemmer.stem(token.toLowerCase().replaceAll("[^a-z0-9]", "")));
        }

        List<String> stems2 = new ArrayList<String>(stems);
        noStemmed = stems.size();


        // TODO perform lemmatization (use derived tokens)
        //DictionaryLemmatizer lem = new DictionaryLemmatizer(_lemmatizer);
        Set<String> uniqueLemas = new HashSet<String>();
        String[] lemy = _lemmatizer.lemmatize(tokens, tags);
        List<String> newLemitized = new ArrayList<String>();
        for (String lema : lemy)
        {
            if (lema != "O")
            {
                uniqueLemas.add(lema);
                newLemitized.add(lema);
            }
        }
        noWords = uniqueLemas.size();

        // (remove "O" from results - non-dictionary forms, update noWords)


        // TODO derive people, locations, organisations (use tokens),
        NameFinderME finder1 = new NameFinderME(_peopleModel);
        NameFinderME finder2 = new NameFinderME(_placesModel);
        NameFinderME finder3 = new NameFinderME(_organizationsModel);

        people = finder1.find(tokens);
        organisations = finder3.find(tokens);
        locations = finder2.find(tokens);
        // (update people, locations, organisations lists).

        // TODO update overall statistics - use tags and check first letters
        for (String tag : tags)
        {
            if (tag.charAt(0) == 'V' || tag.charAt(0) == 'M')
            {
                _verbCount ++;
            }
            else if (tag.charAt(0) == 'N')
            {
                _nounCount++;
            }
            else if (tag.charAt(0) == 'J')
            {
                _adjectiveCount++;
            }
            else if (tag.charAt(0) == 'R' || tag.equals("WRB"))
            {
                _adverbCount ++;
            }
            _totalTokensCount++;
        }

        // (see https://www.clips.uantwerpen.be/pages/mbsp-tags; first letter = "V" = verb?)

        // ------------------------------------------------------------------

        saveResults("Sentences", noSentences);
        saveResults("Tokens", noTokens);
        saveResults("Stemmed forms (unique)", noStemmed);
        saveResults("Words from a dictionary (unique)", noWords);

        saveNamedEntities("People", people, tokens);
        saveNamedEntities("Locations", locations, tokens);
        saveNamedEntities("Organizations", organisations, tokens);
    }


    private void saveResults(String feature, int count)
    {
        String s = feature + ": " + count;
        System.out.println("   " + s);
        _statisticsWriter.println(s);
    }

    private void saveNamedEntities(String entityType, Span spans[], String tokens[])
    {
        StringBuilder s = new StringBuilder(entityType + ": ");
        for (int sp = 0; sp < spans.length; sp++)
        {
            for (int i = spans[sp].getStart(); i < spans[sp].getEnd(); i++)
            {
                s.append(tokens[i]);
                if (i < spans[sp].getEnd() - 1) s.append(" ");
            }
            if (sp < spans.length - 1) s.append(", ");
        }

        System.out.println("   " + s);
        _statisticsWriter.println(s);
    }

    private void overallStatistics()
    {
        _statisticsWriter.println("---------OVERALL STATISTICS----------");
        DecimalFormat f = new DecimalFormat("#0.00");

        if (_totalTokensCount == 0) _totalTokensCount = 1;
        String verbs = f.format(((double) _verbCount * 100) / _totalTokensCount);
        String nouns = f.format(((double) _nounCount * 100) / _totalTokensCount);
        String adjectives = f.format(((double) _adjectiveCount * 100) / _totalTokensCount);
        String adverbs = f.format(((double) _adverbCount * 100) / _totalTokensCount);

        _statisticsWriter.println("Verbs: " + verbs + "%");
        _statisticsWriter.println("Nouns: " + nouns + "%");
        _statisticsWriter.println("Adjectives: " + adjectives + "%");
        _statisticsWriter.println("Adverbs: " + adverbs + "%");

        System.out.println("---------OVERALL STATISTICS----------");
        System.out.println("Adverbs: " + adverbs + "%");
        System.out.println("Adjectives: " + adjectives + "%");
        System.out.println("Verbs: " + verbs + "%");
        System.out.println("Nouns: " + nouns + "%");
    }

}
