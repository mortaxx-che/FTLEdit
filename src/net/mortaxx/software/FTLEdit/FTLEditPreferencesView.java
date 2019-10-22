package net.mortaxx.software.FTLEdit;

/*
Copyright (c) 2009-2019, Christian Hecht. 
All rights reserved.

This software is published under the "Simplified BSD License" (2-clause license)

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies,
either expressed or implied, of the FTLEdit project.

These class is part of program FTLEdit.

FTLEdit includes and uses libraries and source code from some other contributors:

--> FileTree.java - Extension of JTree class to show a file system explorer - Copyright 2005 by Janni Kovacs
- Copied from the tutorial at java-forum.org
- For further information, see <http://www.java-forum.org/java-faq-beitraege/23430-jtree-anwendung-1-directorychooser.html>

--> Parts of GUI code generated by GuiGenie - Copyright (c) 2004 Mario Awad.
- Home Page http://guigenie.cjb.net - Check often for new versions!

--> Rsyntaxtextarea/RSTALanguageSupport/AutoComplete
Copyright (c) 2012, Robert Futrell - https://github.com/bobbylight

--> Rhino
Copyright (c) 2012, Mozilla Corporation - https://github.com/mozilla/rhino

--> Chromium Embedded Framwork (cef)/Java Chromium Embeddes Framework (jcef)
Copyright (c) 2008-2013, Marshall A. Greenblatt. 
Portions Copyright (c) 2006-2009 Google Inc.
https://bitbucket.org/chromiumembedded/java-cef/src/master/
https://bitbucket.org/chromiumembedded/cef/src/master/

--> Gluegen/Jogl
Copyright (c) 2010 JogAmp Community - http://jogamp.org/

*/

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FTLEditPreferencesView extends JDialog {

	private Locale currentlocale;
    private JLabel encodingLabel;
    private JComboBox encodingComboBox;
    private GridBagLayout gbLayout;
    private GridBagConstraints gbc;

    private JPanel prefPane;
    private JPanel buttonPanel;
    
    private JButton saveButton;
    private JButton cancelButton;

	private ResourceBundle messagebundle;
	private int buttonPressed;
	private Hashtable preferencesTable;
	
	public FTLEditPreferencesView(Frame parent, String title, boolean modal, Locale currloc) {
		  
		super(parent, title, modal);
		currentlocale = currloc;
		messagebundle = ResourceBundle.getBundle("resources.MessagesFFE", currentlocale);
		preferencesTable = new Hashtable();
		initGUI();

	}
	
	private void initGUI() {
		
// Panel erzeugen mit GridBagLayout - derzeit zwei Zeilen - zwei Spalten
/* _____________________________________
   | encodingLabel | encodingComboBox  |
   ------------------------------------|
   |          buttonPanel              |
   _____________________________________
 */	
		prefPane = new JPanel();

		gbLayout = new GridBagLayout();
		prefPane.setLayout(gbLayout);
		
//	    String[] EncodingComboBoxItems = {"Use orginal file encoding", "UTF-8"};

	    encodingLabel = new JLabel (messagebundle.getString("022"));
	    encodingComboBox = new JComboBox ();
	    encodingComboBox.addItem(new JComboKeyValue("FILE", messagebundle.getString("020")));
	    encodingComboBox.addItem(new JComboKeyValue("UTF-8", "UTF-8"));

	    encodingComboBox.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	          encodingComboBox_actionPerformed(e);
	      }
	    });
	    
		gbc = new GridBagConstraints();
		
// Festlegen, dass dieses Element das Gitterfeld in keiner Richtung ausfüllt, damit das Label immer gleich groß bleibt
		gbc.fill = GridBagConstraints.NONE;
// In die linke obere Ecke der Zelle positionieren
		gbc.anchor = GridBagConstraints.NORTHEAST;
// Die Abstände der einzelnen GUI-Elemente zu den gedachten Gitterlinien festlegen:
		gbc.insets = new Insets(15,10,10,10);  

		gbc.gridx = 0;  // x-Position im gedachten Gitter
		gbc.gridy = 0;  // y-Position im gedachten Gitter
		gbc.gridheight = 1;  // Ein Gitter-Feld hoch
		gbc.gridwidth = 1; // Ein Gitter-Feld breit
		gbc.weightx = 0; // Beim Größerziehen keinen zusätzlichen Platz links/rechts zuwweisen
		gbc.weighty = 0; // Beim Größerziehen keinen zusätzlichen Platz oben/unten zuwweisen


	    gbLayout.setConstraints(encodingLabel, gbc);
	    prefPane.add(encodingLabel);

		gbc = new GridBagConstraints();

// Festlegen, dass dieses GUI-Element das Gitterfeld in waagerechter Richtung ausfüllt, damit die Komponente den restlichen Platz einnimmt
		gbc.fill = GridBagConstraints.HORIZONTAL;
// In die linke obere Ecke der gedachten Zelle positionieren
		gbc.anchor = GridBagConstraints.NORTHEAST;
// Die Abstände der einzelnen GUI-Elemente zu den gedachten Gitterlinien festlegen:
		gbc.insets = new Insets(10,10,10,10);  

		gbc.gridx = 1;  // x-Position im gedachten Gitter
		gbc.gridy = 0;  // y-Position im gedachten Gitter
		gbc.gridheight = 1;  // Ein Gitter-Felder hoch
		gbc.gridwidth = 1;  // Ein Gitter-Feld breit
		gbc.weightx = 0; // Beim Größerziehen der Komponenete keinen Platz links/rechts zuwweisen
		gbc.weighty = 0; // Beim Größerziehen der Komponenete keinen Platz oben/unten zuwweisen

	    gbLayout.setConstraints(encodingComboBox, gbc);
	    
	    prefPane.add(encodingComboBox);
	    
// Letzte Zeile - Speicher- und Abbrechen-Button
	    
	    buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    saveButton = new JButton (messagebundle.getString("004"));
	    cancelButton = new JButton (messagebundle.getString("021"));

	    saveButton.setSize(new Dimension(100,25));
	    cancelButton.setSize(new Dimension(100,25));
	
// Listener hinzufügen
	    
	    cancelButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	          cancelButton_actionPerformed(e);
	        }
	      });
	    
	    saveButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	          saveButton_actionPerformed(e);
	        }
	    });
	    
	    buttonPanel.add (saveButton);
	    buttonPanel.add (cancelButton);

		gbc = new GridBagConstraints();

// Festlegen, dass dieses GUI-Element das Gitterfeld in waagerechter Richtung ausfüllt, damit die Komponente den restlichen Platz einnimmt
		gbc.fill = GridBagConstraints.HORIZONTAL;
// In die linke obere Ecke der gedachten Zelle positionieren
		gbc.anchor = GridBagConstraints.NORTHEAST;
// Die Abstände der einzelnen GUI-Elemente zu den gedachten Gitterlinien festlegen:
		gbc.insets = new Insets(10,10,10,10);  

		gbc.gridx = 0;  // x-Position im gedachten Gitter
		gbc.gridy = 1;  // y-Position im gedachten Gitter
		gbc.gridheight = 1;  // Ein Gitter-Felder hoch
		gbc.gridwidth = 2; // Zwei Gitter-Felder breit
// !!! Achtung, wenn die Komponenten alle oben links ausgerichtet bleiben sollen, so muß mindestens ein Element
// der letzten Zeilen allen Platz beim Größerziehen zugewiesen bekommen, sonst rutscht alles nach unten
// Bezieht sich nicht auf die Elemente im Container (hier buttonPanel) sondern auf das umgebende JPanel selbst !!!
		
		gbc.weightx = 1; // Beim Größerziehen der Komponenete allen zusätzlichen Platz links/rechts zuwweisen
		gbc.weighty = 1; // Beim Größerziehen der Komponenete allen zusätzlichen Platz oben/unten zuwweisen

		gbLayout.setConstraints(buttonPanel, gbc);
	
		prefPane.add(buttonPanel);

	    add(prefPane);
	    setSize(new Dimension(800,600));
	    setLocation(400,200);
	    pack();
	    
	}
	
    private void encodingComboBox_actionPerformed(ActionEvent e) {

    	JComboKeyValue selkey = (JComboKeyValue) encodingComboBox.getSelectedItem();
    	preferencesTable.put("saveEncoding", selkey.getKey());

    }
    
    private void saveButton_actionPerformed(ActionEvent e) {

    	buttonPressed = 1;
        this.dispose();
    }
    
    private void cancelButton_actionPerformed(ActionEvent e) {

    	buttonPressed = 2;
        this.dispose();
    }
    
    public Hashtable getPreferences() {
    	
    	return preferencesTable;
    }

    public void setPreferences(Hashtable prefs) {
    	
    	buttonPressed = 0;
    	preferencesTable = prefs;
    	
// Richtigen Eintrag in der ComboBox auswählen
    	if (preferencesTable.get("saveEncoding").toString().equals("FILE")) {
        	encodingComboBox.setSelectedItem(new JComboKeyValue(preferencesTable.get("saveEncoding").toString(), messagebundle.getString("020")));
    	}
    	else if (preferencesTable.get("saveEncoding").toString().equals("UTF-8")) {
        	encodingComboBox.setSelectedItem(new JComboKeyValue(preferencesTable.get("saveEncoding").toString(), "UTF-8"));    		
    	}

    }
    
    public int getAction() {
    	return buttonPressed;
    }
    
	private class JComboKeyValue {
		String key, value;
	 
		public JComboKeyValue (String key, String value) {
			this.key = key;
			this.value = value;
		}
	 
		public String getValue() { return value; }
		public String getKey() { return key; }
	 
		@Override
		public String toString() { return value; }
	 
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof JComboKeyValue) {
				JComboKeyValue kv = (JComboKeyValue) obj;
				return (kv.value.equals(this.value));
			}
			return false;
		}
	 
		@Override
		public int hashCode() {
			int hash = 7;
			hash = 97 * hash + (this.value != null ? this.value.hashCode() : 0);
			return hash;
		}
	}
}
