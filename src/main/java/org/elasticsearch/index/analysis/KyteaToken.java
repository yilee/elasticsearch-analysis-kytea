package org.elasticsearch.index.analysis;

/**
 * Created by kshi on 3/11/16.
 */

public class KyteaToken {
    public String word;
    public int startOffset;
    public int endOffset;

    public KyteaToken(String word, int startOffset, int endOffset) {
        this.word = word;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    public String toString() {
        return "[" + this.word + ", " + this.startOffset + ", " + this.endOffset + "]";
    }
}
