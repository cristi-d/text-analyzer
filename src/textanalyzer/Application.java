/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textanalyzer;

import java.io.File;
import java.io.IOException;
import textanalyzer.gui.MainWindow;
import textanalyzer.model.doc.Document;

/**
 *
 * @author Cristi
 */
public class Application {
    public static Document doc;
    private static MainWindow window;
    
    public static boolean loadProject(File f) {
        try {
            doc = new Document(f);
            window.setDocument(doc);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    public static boolean saveProject() {
        try {
            doc.save();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    public static boolean createNewProject(File textFile, File projectFile) {
        try {
            doc = new Document(textFile, projectFile);
            window.setDocument(doc);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    public static void main(String[] args) throws IOException {
        window = new MainWindow();
        window.setVisible(true);
    }
}
