package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;

import java.io.File;
import java.io.Reader;

/**
 * Created by kshi on 3/8/16.
 */

public class KyteaAnalyzer extends Analyzer {

    private final ESLogger log = Loggers.getLogger(KyteaAnalyzer.class);
    private File configFile;

    public KyteaAnalyzer(File configFile) {
        super();
        this.configFile = configFile;
    }


    public KyteaAnalyzer(Settings indexSettings, Settings settings) {
        super();
        Environment env = new Environment(indexSettings);
        this.configFile = env.configFile();
        System.out.println("KyteaAnalyzersettings:" + this.configFile.getAbsolutePath());
        System.out.println("KyteaAnalyzersettings:" + settings.get("seg_mode"));
    }

    @Override
    protected TokenStreamComponents createComponents(String s, Reader reader) {
        Tokenizer tokenizer;
        tokenizer = new SentenceTokenizer(reader);
        TokenStream result = new KyteaTokenFilter(configFile, tokenizer);
        return new TokenStreamComponents(tokenizer, result);
    }
}
