/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.gui.custom.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import textanalyzer.model.doc.Document;
import textanalyzer.model.doc.Personage;
import textanalyzer.model.doc.Utterance;
import textanalyzer.model.doc.Voice;
import textanalyzer.model.lang.WordOccurance;

/**
 *
 * @author Cristi
 */
public class VoiceDisplayPanel extends JPanel implements MouseMotionListener, MouseListener {
    private Document doc;
    private List<Utterance> markedUtterances;
    private List<Personage> personages;
    private boolean doPaint;
    private int chartWidth, chartHeight;
    public static int UTTERANCE_WIDTH = 10, PERSONAGE_SPACING = 100;
    public static int CHART_OFFSET_X = 20;
    public static int CHART_OFFSET_Y = 20;
    private int mouseXCoord;
    private boolean mouseOver;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private Color backgroundColor;
    
    public VoiceDisplayPanel() {
        doPaint = false;
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.setLayout(null);
        
        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        
        scrollPane = new JScrollPane(textArea);
        this.add(scrollPane);
        
        backgroundColor = new Color(236, 233, 216);
    }
    
    public void setDocument(Document doc) {
        this.doc = doc;
        
        if (doc != null && 
                doc.getVoices() != null && doc.getVoices().isEmpty() == false && 
                doc.getPersonages() != null && doc.getPersonages().isEmpty() == false) {
            markedUtterances = doc.getMarkedUtterances();
            personages = doc.getPersonages();
            doPaint = true;
            chartWidth = markedUtterances.size() * UTTERANCE_WIDTH;
            chartHeight = (personages.size() - 1) * PERSONAGE_SPACING + 50;
            
            this.setSize(new Dimension(40 + chartWidth, 40 + chartHeight + 100));
            this.setPreferredSize(this.getSize());
        } else {
            doPaint = false;
        }
        
    }
    
    public BufferedImage getSnapshot() {
        
        BufferedImage img = new BufferedImage(chartWidth + CHART_OFFSET_X, chartHeight + CHART_OFFSET_Y, BufferedImage.TYPE_INT_RGB);
        this.mouseOver = false;
        scrollPane.setVisible(false);
        Graphics g = img.getGraphics();
        
        this.paint(g);
        
        //Add legend
        
        BufferedImage img2 = new BufferedImage(img.getWidth() + 100, img.getHeight() + 100, BufferedImage.TYPE_INT_RGB);
        g = img2.getGraphics();
        g.setColor(backgroundColor);
        g.fillRect(0, 0, img2.getWidth(), img2.getHeight());
        
        g.setColor(Color.BLACK);
        int i = 0;
        for (Personage p : doc.getPersonages()) {
            g.drawString(p.getName(), 20, CHART_OFFSET_Y + i * PERSONAGE_SPACING + 5);
            i++;
        }
        
        i = 0;
        for (Voice v : doc.getVoices()) {
            g.setColor(v.getHighlightColor());
            g.fillRect(CHART_OFFSET_X + 100 + i * 120, chartHeight + CHART_OFFSET_Y + 50, 100, 20);
            g.setColor(Color.BLACK);
            g.drawString(v.getName(),CHART_OFFSET_X + 100 + i * 120, chartHeight + CHART_OFFSET_Y + 50 + 10);
            i++;
        }
        
        
        g.drawImage(img, 100, 0, null);
        
        return img2;
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paint(g);
        
        
        
        if (doPaint) {
            //Draw background
            g.setColor(backgroundColor);
            g.fillRect(0, 0, chartWidth + CHART_OFFSET_X, chartHeight + CHART_OFFSET_Y);
        
            g.drawLine(CHART_OFFSET_X, CHART_OFFSET_Y + chartHeight, CHART_OFFSET_X + chartWidth, CHART_OFFSET_Y + chartHeight);

            int voiceNr = 0;
            for (Voice v : doc.getVoices()) {
                g.setColor(v.getHighlightColor());
                
                
                for (int i = 0; i < v.getOccurances().size() - 1; i++) {
                    
                    WordOccurance wOcc1 = v.getOccurances().get(i);
                    WordOccurance wOcc2 = v.getOccurances().get(i + 1);
                    
                    
                    
                    int utteranceIndex1 = markedUtterances.indexOf(wOcc1.getUtterance());
                    int utteranceIndex2 = markedUtterances.indexOf(wOcc2.getUtterance());
                    int h1 = CHART_OFFSET_Y + personages.indexOf(wOcc1.getUtterance().getEmmiter()) * PERSONAGE_SPACING;
                    int h2 = CHART_OFFSET_Y + personages.indexOf(wOcc2.getUtterance().getEmmiter()) * PERSONAGE_SPACING;
                    
                    g.drawLine(CHART_OFFSET_X + utteranceIndex1 * UTTERANCE_WIDTH, h1 + 3, CHART_OFFSET_X + utteranceIndex2 * UTTERANCE_WIDTH, h2 + 3);
                    
                    g2d.fillOval(CHART_OFFSET_X + utteranceIndex1 * UTTERANCE_WIDTH, h1, 6, 6);
                }
                
                
                if (v.getOccurances().size() > 1) {
                    WordOccurance wOcc1 = v.getOccurances().get(v.getOccurances().size() - 1);
                    int utteranceIndex1 = markedUtterances.indexOf(wOcc1.getUtterance());
                    int h1 = CHART_OFFSET_Y + personages.indexOf(wOcc1.getUtterance().getEmmiter()) * PERSONAGE_SPACING;
                    g2d.fillOval(CHART_OFFSET_X + utteranceIndex1 * UTTERANCE_WIDTH, 
                        h1, 6, 6);
                } 
                
                voiceNr++;
            }
            
            if (mouseOver) {    
                g.setColor(Color.BLACK);
                
                int utteranceIndex = 0;
                int temp = mouseXCoord - CHART_OFFSET_X;
                
                while (temp > UTTERANCE_WIDTH) {
                    temp -= UTTERANCE_WIDTH;
                    utteranceIndex++;
                }
                
                if (temp > UTTERANCE_WIDTH / 2) {
                    utteranceIndex++;
                }
                
                if (utteranceIndex >= markedUtterances.size()) {
                    return;
                }
                
                textArea.setText(markedUtterances.get(utteranceIndex).getOriginalText());
                
                textArea.updateUI();
                
                JViewport viewport = (JViewport) this.getParent();
                int xOffset = viewport.getViewPosition().x;
                int viewportWidth = viewport.getWidth();
                
                scrollPane.setVisible(true);
                if (CHART_OFFSET_X + utteranceIndex * UTTERANCE_WIDTH  - xOffset < 200) {
                   scrollPane.setBounds(CHART_OFFSET_X + utteranceIndex * UTTERANCE_WIDTH, CHART_OFFSET_Y + chartHeight + 40, 400, 100);    
                } else {
                    if (CHART_OFFSET_X + utteranceIndex * UTTERANCE_WIDTH + 200 - xOffset > viewportWidth) {
                        scrollPane.setBounds(CHART_OFFSET_X + utteranceIndex * UTTERANCE_WIDTH - 400, CHART_OFFSET_Y + chartHeight + 40, 400, 100);    
                    } else {
                        scrollPane.setBounds(CHART_OFFSET_X + utteranceIndex * UTTERANCE_WIDTH - 200, CHART_OFFSET_Y + chartHeight + 40, 400, 100);    
                    }
                }
                
                
                g.drawLine(CHART_OFFSET_X + utteranceIndex * UTTERANCE_WIDTH, CHART_OFFSET_Y + chartHeight, CHART_OFFSET_X + utteranceIndex * UTTERANCE_WIDTH, CHART_OFFSET_Y);
                g.setColor(Color.BLACK);
                g.drawString(utteranceIndex + "", CHART_OFFSET_X + utteranceIndex * UTTERANCE_WIDTH, CHART_OFFSET_Y + chartHeight + 30);
            } else {
                scrollPane.setVisible(false);
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        ;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int xCoord = e.getX();
        
        
//        
//        JViewport viewPort = (JViewport) this.getParent();
//        int viewPortOffset = viewPort.getViewPosition().x;
//        int viewPortWidth = viewPort.getWidth();
//        
//        int sign = (xCoord > mouseXCoord) ? -1 : +1;
//        
//        if (sign < 0) {
//            
//        }
//        
//        if (xCoord + 400 > viewPortOffset + viewPortWidth) {
//            viewPort.setViewPosition(new Point(viewPortOffset + sign * (viewPortOffset + viewPortWidth - xCoord - 400), 0));
//        }
        
        this.repaint();
        mouseXCoord = xCoord;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        ;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        ;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        ;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseOver = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseOver = false;
    }
}
