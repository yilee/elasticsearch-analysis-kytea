package org.elasticsearch.index.analysis;

import com.linkedin.kytea.Mykytea;
import com.linkedin.kytea.TagsVector;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by kshi on 3/11/16.
 */

public class KyteaTokenizer extends Tokenizer {
    private final ESLogger log = Loggers.getLogger(KyteaTokenizer.class);

    private Iterator<KyteaToken> tokenIter;

    private Mykytea myKytea;
    private boolean nativeLibLoaded = false;

    //词元文本属性
    private final CharTermAttribute termAtt;
    //词元位移属性
    private final OffsetAttribute offsetAtt;
    //词元分类属性
    private final TypeAttribute typeAtt;

    private PositionIncrementAttribute posIncrAtt;
    private File configFile;

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

    public KyteaTokenizer(Reader in, Settings settings, Environment environment) {
        super(in);
        offsetAtt = addAttribute(OffsetAttribute.class);
        termAtt = addAttribute(CharTermAttribute.class);
        typeAtt = addAttribute(TypeAttribute.class);
        posIncrAtt = addAttribute(PositionIncrementAttribute.class);
        this.configFile = environment.configFile();
        loadKeyteaLib();
    }


    @Override
    public boolean incrementToken() throws IOException {
        if (input == null) {
            return false;
        }

        if (tokenIter == null || !tokenIter.hasNext()) {
            String se = readerToString(input);
            tokenIter = getSegList(se).iterator();
            if (!tokenIter.hasNext())
                return false;
        }
        clearAttributes();
        KyteaToken token = tokenIter.next();
        offsetAtt.setOffset(token.startOffset, token.endOffset);
        String tokenString = token.word;
        termAtt.copyBuffer(tokenString.toCharArray(), 0, tokenString.length());
        typeAtt.setType("word");
        return true;
    }

    private String readerToString(Reader in) {
        StringBuilder builder = new StringBuilder();
        int charsRead = -1;
        char[] chars = new char[100];
        do {
            try {
                charsRead = in.read(chars, 0, chars.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (charsRead > 0)
                builder.append(chars, 0, charsRead);
        } while (charsRead > 0);
        String stringReadFromReader = builder.toString();
        return stringReadFromReader;
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
