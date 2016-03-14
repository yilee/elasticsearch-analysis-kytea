package org.elasticsearch.index.analysis;

import java.io.File;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettings;


public class KyteaTokenFilterFactory extends AbstractTokenFilterFactory {
    private final ESLogger log = Loggers.getLogger(KyteaTokenFilterFactory.class);
    private File configFile;

    @Inject
    public KyteaTokenFilterFactory(Index index,
                                   @IndexSettings Settings indexSettings,
                                   @Assisted String name,
                                   @Assisted Settings settings) {
        super(index, indexSettings, name, settings);
        Environment env = new Environment(indexSettings);
        this.configFile = env.configFile();
    }

    @Override
    public TokenStream create(TokenStream input) {
        return new KyteaTokenFilter(configFile, input);
    }

}
