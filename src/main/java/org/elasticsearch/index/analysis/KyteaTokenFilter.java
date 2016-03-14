package org.elasticsearch.index.analysis;

import com.linkedin.kytea.Mykytea;
import com.linkedin.kytea.TagsVector;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public final class KyteaTokenFilter extends TokenFilter {

    private final ESLogger log = Loggers.getLogger(KyteaTokenFilter.class);

    private Mykytea myKytea;

    private Iterator<KyteaToken> tokenIter;
    private List<KyteaToken> array;
    private File configFile;
    private boolean nativeLibLoaded = false;

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);


    private void loadKeyteaLib() {
        if (!nativeLibLoaded) {
            try {
                System.err.println("Now Loading the kytea lib ...");
                String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
                File libFile = null;
                if ((OS.contains("mac")) || (OS.contains("darwin"))) {
                    libFile = new File(configFile + "/kytea/_LiKytea.dylib");
                } else if (OS.contains("nux")) {
                    libFile = new File(configFile + "/kytea/_LiKytea.so");
                } else {
                    System.err.println("Not supported OS. Failed to load the kytea lib.\n");
                    System.exit(1);
                }
                System.load(libFile.getAbsolutePath());
                System.err.println("Finish to  the kytea lib ...");
            } catch (Exception e) {
                System.err.println("Failed to load the kytea lib.\n" + e);
                System.exit(1);
            }
        }
        nativeLibLoaded = true;
        myKytea = new Mykytea("--model " + configFile.getAbsolutePath() + "/kytea/model.bin");
    }

    public KyteaTokenFilter(File configFile, TokenStream input) {
        super(input);
        this.configFile = configFile;
        loadKeyteaLib();
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (tokenIter == null || !tokenIter.hasNext()) {
            if (input.incrementToken()) {
                array = getSegList(termAtt.toString());
                tokenIter = array.iterator();
                if (!tokenIter.hasNext())
                    return false;
            } else {
                return false;
            }
        }

        clearAttributes();

        KyteaToken token = tokenIter.next();
        offsetAtt.setOffset(token.startOffset, token.endOffset);
        String tokenString = token.word;
        termAtt.copyBuffer(tokenString.toCharArray(), 0, tokenString.length());
        typeAtt.setType("word");
        return true;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        tokenIter = null;
    }

    private List<KyteaToken> getSegList(String stringReadFromReader) {
        List<KyteaToken> tokens = new ArrayList<KyteaToken>();
        KyteaToken token;
        TagsVector tv = myKytea.getAllTags(stringReadFromReader);
        int startPos = 0;
        for (int i = 0; i < tv.size(); i++) {
            String word = tv.get(i).getSurface();
            int startOffset = stringReadFromReader.indexOf(word, startPos);
            int endOffset = startOffset + word.length();
            token = new KyteaToken(word, startOffset, endOffset);
            startPos += word.length();
            tokens.add(token);
        }
        return tokens;
    }


}
