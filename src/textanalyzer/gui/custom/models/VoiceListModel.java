/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.gui.custom.models;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author Cristi
 */
public class VoiceListModel extends AbstractListModel implements ColorListModel {
    private List<String> names;
    private List<Color> colors;
    
    public VoiceListModel() {
        names = new ArrayList<String>();
        colors = new ArrayList<Color>();
    }
    
    public void clearAll() {
        names.clear();
        colors.clear();
    }
    
    @Override
    public int getSize() {
        return names.size();
    }
    
    public void addVoice(String name, Color color) {
        names.add(name);
        colors.add(color);
    }
    
    public void removeVoice(int index) {
        names.remove(index);
        colors.remove(index);
    }
    
    @Override
    public Color getColorAt(int index) {
        return colors.get(index);
    }

    @Override
    public Object getElementAt(int index) {
        return names.get(index);
    }
    
    public boolean hasVoiceName(String name) {
        return names.contains(name);
    }
    
    public boolean hasColor(Color c) {
        return colors.contains(c);
    }

    @Override
    public Color getObjectColor(Object obj) {
        int index = names.indexOf(obj);
        
        if (index != -1) {
            return colors.get(index);
        } else {
            return null;
        }
    }
}
