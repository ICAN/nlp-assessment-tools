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
    
    public String token;
    public String tag;
    
    public Token (String token, String tag) {
        this.token = token.trim();
        this.tag = tag.trim();
    }
    
    
    public String toString() {
        return token + ": " + tag;
    }
    
}
