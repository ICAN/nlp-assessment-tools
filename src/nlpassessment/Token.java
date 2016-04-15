/*
 * The MIT License
 *
 * Copyright 2016 Neal Logan.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package nlpassessment;

import java.util.ArrayList;

/**
 *
 * @author Neal Logan
 * 
 */
public class Token {
    public int indexInText = -1;
    public int indexInSentence = -1;
    public String token = "";
    public String tagset = "";
    public boolean semantic = true;
    public boolean multitag = false;
    
    public String[] getTaglist() {
        return tagset.split("\\t");
    }
    
    public Token(String token, String tag) {
        this.token = token;
        this.tagset = tag;
    }
    
    public Token (int tokenInText, int tokenInSentence, String token, String tag) {
        this.indexInText = tokenInText;
        this.indexInSentence = tokenInSentence;
        this.token = token.trim();
        this.tagset = tag.trim();
        this.semantic = semantic;
    }
    
    public String toString() {
        return indexInText + "\t " + indexInSentence + "\t " + token + "\t " + tagset;
    }
    
}
