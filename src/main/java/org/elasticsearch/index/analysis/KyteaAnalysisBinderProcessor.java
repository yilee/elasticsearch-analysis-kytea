package org.elasticsearch.index.analysis;

/**
 * Created by kshi on 3/8/16.
 */

public class KyteaAnalysisBinderProcessor extends AnalysisModule.AnalysisBinderProcessor {

    @Override
    public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {
        tokenFiltersBindings.processTokenFilter("kytea", KyteaTokenFilterFactory.class);
        super.processTokenFilters(tokenFiltersBindings);
    }

    @Override
    public void processAnalyzers(AnalyzersBindings analyzersBindings) {
        analyzersBindings.processAnalyzer("kytea", KyteaAnalyzerProvider.class);
        super.processAnalyzers(analyzersBindings);
    }

    @Override
    public void processTokenizers(TokenizersBindings tokenizersBindings) {
        tokenizersBindings.processTokenizer("kytea", KyteaTokenizerFactory.class);
        super.processTokenizers(tokenizersBindings);
    }
}
