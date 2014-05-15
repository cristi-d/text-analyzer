/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.gui.custom.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import textanalyzer.model.doc.Document;
import textanalyzer.model.doc.Utterance;

/**
 *
 * @author Cristi
 */
public class TextDisplayerBorder extends JPanel {
    private Document doc;
    private TextDisplayer displayer;
    
    public TextDisplayerBorder(TextDisplayer displayer) {
        this.setLayout(null);
        doc = null;
        this.displayer = displayer;
        this.setBackground(Color.ORANGE);
    }
    
    public void setDocument(Document doc) {
        this.doc = doc;
    }
    
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        if (doc != null && doc.getUtterances() != null) {
            Rectangle clipBounds = g.getClipBounds();
            int startY, endY;
            startY = clipBounds.y;
            endY = clipBounds.y + clipBounds.height;
            
//            System.out.println("CLIP: " + startY + ", " + endY);
                    
            
            for (Utterance u : doc.getUtterances()) {
                try {
                    Rectangle r = displayer.modelToView(u.getBeginOffset());
//                    if (r.y > startY && r.y < endY) {
                        if (u.getEmmiter() != null) {
                            g.drawString(u.getEmmiter().getName(), 0, r.y + 5 + r.height / 2);    
                        }
                        
//                    }
                } catch (BadLocationException ex) {
                    Logger.getLogger(TextDisplayerBorder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
