package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettings;

import java.io.Reader;

public class KyteaTokenizerFactory extends AbstractTokenizerFactory {
    private Environment environment;
    private Settings settings;
    private final ESLogger log = Loggers.getLogger(KyteaTokenizerFactory.class);

    @Inject
    public KyteaTokenizerFactory(Index index,
                                 @IndexSettings Settings indexSettings,
                                 Environment env,
                                 @Assisted String name,
                                 @Assisted Settings settings) {
        super(index, indexSettings, name, settings);
        this.environment = env;
        this.settings = settings;
    }


    @Override
    public Tokenizer create(Reader reader) {
        return new KyteaTokenizer(reader, settings, environment);
    }

}
