/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.model.doc;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristi
 */
public class Personage {
    private String name;
    private List<String> aliases;
    
    public Personage(String name) {
        this.name = name;
        this.aliases = new ArrayList<String>();
    }
    
    public Personage(String name, List<String> aliases) {
        this.name = name;
        this.aliases = aliases;
    }
    
    public void addAlias(String alias) {
        aliases.add(alias);
    }
    
    public void removeAlias(String alias) {
        aliases.remove(alias);
    }
    
    public String getName() {
        return name;
    }
    
    public List<String> getAliases() {
        return aliases;
    }
    
}
