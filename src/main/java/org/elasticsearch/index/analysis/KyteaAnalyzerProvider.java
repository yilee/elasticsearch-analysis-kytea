package org.elasticsearch.index.analysis;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettings;

public class KyteaAnalyzerProvider extends
        AbstractIndexAnalyzerProvider<KyteaAnalyzer> {
    private final ESLogger log = Loggers.getLogger(KyteaAnalyzerProvider.class);
    private final KyteaAnalyzer analyzer;

    @Inject
    public KyteaAnalyzerProvider(Index index,
                                 @IndexSettings Settings indexSettings,
                                 Environment env,
                                 @Assisted String name,
                                 @Assisted Settings settings) {
        super(index, indexSettings, name, settings);
        analyzer = new KyteaAnalyzer(indexSettings, settings);
    }

    @Override
    public KyteaAnalyzer get() {
        return this.analyzer;
    }
}
