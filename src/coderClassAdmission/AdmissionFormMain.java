package coderClassAdmission;

import javax.swing.SwingUtilities;

public class AdmissionFormMain {

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create the form object
                AdmissionForm app = new AdmissionForm();
                
                // Call the method to make the window visible
                app.showWindow();
            }
        });
    }
}   