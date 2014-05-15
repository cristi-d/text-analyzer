/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer.gui.custom.animations;

import javax.swing.JProgressBar;

/**
 *
 * @author Cristi
 */
public class StatusBarAnimation implements Runnable {
    private final JProgressBar progressBar;
    private boolean isRunning;
    private static final int delay = 50;
    private static final int increment = 10;
    
    public StatusBarAnimation(JProgressBar progressBar) {
        this.progressBar = progressBar;
        isRunning = false;
    }
    
    
    public void stopAnimation() {
        isRunning = false;
    }
    
    @Override
    public void run() {
        isRunning = true;
        
        int sign = +1;
        while (isRunning) {
            
            if (sign > 0) {
                if (progressBar.getValue() >= progressBar.getMaximum()) {
                    sign = -1;
                }               
            } else {
                if (progressBar.getValue() <= 0) {
                    sign = +1;
                }
            }
            
            progressBar.setValue(progressBar.getValue() + sign * increment);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                ;
            }
        } 
        
        progressBar.setValue(0);
    }
    
}
