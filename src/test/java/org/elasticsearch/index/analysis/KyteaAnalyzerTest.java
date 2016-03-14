package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

public class KyteaAnalyzerTest {

    String[] sentences = new String[]{
            "他掉进了无底洞里",
            "中国的首都是北京",
            "你认识那个和主席握手的的哥吗？他开一辆黑色的士。",
            "枪杆子中出政权"};

    @Test
    public void test() throws IOException {
        System.out.println("KyteaAnalyzerTest start ...");
        KyteaAnalyzer analyzer = new KyteaAnalyzer(new File(System.getProperty("basedir"), "data"));
        for (String s : sentences) {
            TokenStream tokenStream = analyzer.tokenStream(null, new StringReader(s));
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                CharTermAttribute termAtt = tokenStream.getAttribute(CharTermAttribute.class);
                System.out.println(termAtt.toString());
            }
            tokenStream.reset();
        }

        analyzer.close();
    }

}
