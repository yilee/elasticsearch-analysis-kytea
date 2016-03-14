package org.elasticsearch.plugin.analysis.kytea;

import org.elasticsearch.common.inject.Module;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.KyteaAnalysisBinderProcessor;
import org.elasticsearch.plugins.AbstractPlugin;

public class AnalysisKyteaPlugin extends AbstractPlugin {

    @Override
    public String name() {
        return "analysis-kytea";
    }

    @Override
    public String description() {
        return "kytea analysis";
    }

    @Override
    public void processModule(Module module) {
        if (module instanceof AnalysisModule) {
            AnalysisModule analysisModule = (AnalysisModule) module;
            analysisModule.addProcessor(new KyteaAnalysisBinderProcessor());
        }
    }
}
