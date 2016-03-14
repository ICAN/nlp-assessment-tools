/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlpassessment;

/**
 *
 * @author Neal
 */
public class Token {
    public int tokenInText;
    public int tokenInSentence;
    public String token;
    public String tag;
    
    public Token (int tokenInText, int tokenInSentence, String token, String tag) {
        this.tokenInText = tokenInText;
        this.tokenInSentence = tokenInSentence;
        this.token = token.trim();
        this.tag = tag.trim();
    }
    
    public String toString() {
        return tokenInText + "\t " + tokenInSentence + "\t " + token + "\t " + tag;
    }
    
}
