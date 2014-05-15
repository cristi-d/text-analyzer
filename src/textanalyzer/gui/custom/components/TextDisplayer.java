/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.gui.custom.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import textanalyzer.gui.MainWindow;
import textanalyzer.model.doc.Document;
import textanalyzer.model.doc.Personage;
import textanalyzer.model.doc.Utterance;
import textanalyzer.model.lang.Word;
import textanalyzer.model.lang.WordOccurance;

/**
 *
 * @author Cristi
 */
public class TextDisplayer extends JTextArea implements MouseListener, ActionListener {
    private List<WordOccurance> occurances;
    private Map<Word, List<WordOccurance>> occurancesMap;
    private List<List<WordOccurance>> coOccs;
    private Document doc;
    private Object lastHighlightTag = null;
    private Highlighter.HighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.ORANGE);
    private static final String ACTION_MARK = "MARK";
    private static final String ACTION_UNMARK = "UNMARK";
    private int selectedUtteranceIndex = -1;
    private MainWindow parent;
    private MarkUtteranceDialog dialog;
    
    public TextDisplayer(MainWindow parent, MarkUtteranceDialog dialog) {
        super();
        this.setWrapStyleWord(true);
        this.setLineWrap(true);
        doc = null;
        this.addMouseListener(this);
        
        JPopupMenu menu = new JPopupMenu(DEFAULT_KEYMAP);
        JMenuItem item = new JMenuItem("Mark");
        item.addActionListener(this);
        item.setActionCommand(ACTION_MARK);
        menu.add(item);
        item = new JMenuItem("Unmark");
        item.setActionCommand(ACTION_UNMARK);
        item.addActionListener(this);
        menu.add(item);
        
        this.add(menu);
        setComponentPopupMenu(menu);
        this.parent = parent;
        this.dialog = dialog;
    }
    
    public void setDocument(Document doc) {
        this.doc = doc;
        this.setText(doc.getText());
    }
    
    
    public void resetDisplay() {
        occurances = null;
        coOccs = null;
        this.getHighlighter().removeAllHighlights();
    }
    
    public void setOccurances(List<WordOccurance> occurances) {
        this.occurances = occurances;
        occurancesMap = new HashMap<Word, List<WordOccurance>>();
        
        if (occurances != null) {
            for (WordOccurance wOcc : occurances) {
                List<WordOccurance> occLst = occurancesMap.get(wOcc.getWord());

                if (occLst == null) {
                    occLst = new ArrayList<WordOccurance>();
                    occurancesMap.put(wOcc.getWord(), occLst);
                }

                occLst.add(wOcc);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (occurances != null) {
            for (int i = 0; i < occurances.size(); i++) {
                try {
                    Rectangle modelToView;
                    modelToView = modelToView(occurances.get(i).getBeginOffset());
                    int x, y, h;
                    x = (int) modelToView.getX();
                    y = (int) modelToView.getY();
                    h = (int) modelToView.getHeight();
                    
                    Rectangle modelToView2 = modelToView(occurances.get(i).getEndOffset());
                    int x2;
                    x2 = (int) modelToView2.getX();
                    
                    
                    g.drawRect(x, y, x2 - x, h);
                } catch (BadLocationException ex) {
                    Logger.getLogger(TextDisplayer.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
        if (coOccs != null) {
            for (List<WordOccurance> occLst : coOccs) {
                
                for (int i = 0; i < occLst.size() - 1; i++) {
                    WordOccurance occ1, occ2;
                    occ1 = occLst.get(i);
                    occ2 = occLst.get(i + 1);
                    
                    int x1, y1, x2, y2;
                    int w1, w2, h1, h2;
                    
                    try {
                        Rectangle occ1BoundsStart = modelToView(occ1.getBeginOffset());
                        Rectangle occ2BoundsStart = modelToView(occ2.getBeginOffset());
                        Rectangle occ1BoundsEnd = modelToView(occ1.getEndOffset());
                        Rectangle occ2BoundsEnd = modelToView(occ2.getEndOffset());
                        
                        x1 = (int) occ1BoundsStart.getX();
                        y1 = (int) occ1BoundsStart.getY();
                        h1 = (int) occ1BoundsStart.getHeight();
                        x2 = (int) occ2BoundsStart.getX();
                        y2 = (int) occ2BoundsStart.getY();
                        h2 = (int) occ2BoundsStart.getHeight();
                        
                        w1 = ((int) occ1BoundsEnd.getX()) - x1;
                        w2 = ((int) occ2BoundsEnd.getX()) - x2;
                        
                        if (y1 == y2) {
                            g.drawLine(x1 + w1, y1 + h1 / 2, x2, y2 + h2 /2);
                        } else {
                            g.drawLine(x1 + w1 /2, y1 + h1, x2 + w2 /2, y2);
                        }
                        
                    } catch (BadLocationException ex) {
                        Logger.getLogger(TextDisplayer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
        int y = e.getY();
        int offset = viewToModel(new Point(0, y));
        
        if (doc != null && doc.getUtterances() != null) {
            if (lastHighlightTag != null) {
                    this.getHighlighter().removeHighlight(lastHighlightTag);
                }
            int i = -1;
            selectedUtteranceIndex = -1;
            for (Utterance u : doc.getUtterances()) {
                i++;
                if (u.getBeginOffset() <= offset && u.getEndOffset() >= offset) {
                    try {
                        lastHighlightTag = this.getHighlighter().addHighlight(u.getBeginOffset(), u.getEndOffset(), highlightPainter);
                        selectedUtteranceIndex = i;
                        break;
                    } catch (BadLocationException ex) {
                        ;
                    }
                    
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getActionCommand().equals(ACTION_MARK)) {
//            Dimension dialogPrefSize = dialog.getPreferredSize();
//            dialog.setBounds(lastMouseClickPosition.x, lastMouseClickPosition.y, dialogPrefSize.width, dialogPrefSize.height);
            dialog.setVisible(true);
            int selectedPersonageIndex = dialog.getSelectedPersonageIndex();
            if (selectedPersonageIndex != -1) {
                Utterance utterance = doc.getUtterances().get(selectedUtteranceIndex);
                Personage personage = doc.getPersonages().get(selectedPersonageIndex);
            
                utterance.setEmmiter(personage);
                
                parent.computeWordFrequenciesSetEnabled(true);
            }
        }
        
        if (e.getActionCommand().equals(ACTION_UNMARK)) {
            Utterance utterance = doc.getUtterances().get(selectedUtteranceIndex);
            
            utterance.setEmmiter(null);
            parent.computeWordFrequenciesSetEnabled(true);
        }
        
        this.getParent().getParent().repaint();
    }
}
