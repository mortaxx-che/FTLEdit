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

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JSplitPane;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.fife.rsta.ac.AbstractSourceTree;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.rsta.ac.java.tree.JavaOutlineTree;
import org.fife.rsta.ac.js.tree.JavaScriptTreeNode;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.mozilla.javascript.ast.AstNode;

import net.mortaxx.software.osdfsutils.SpecialDataTypes;

@SuppressWarnings("serial")
//import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
//import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
//import org.fife.ui.rtextarea.RTextScrollPane;

class OutlineTreeNode extends DefaultMutableTreeNode {
	public OutlineTreeNode(Object object) {
                        // TODO Auto-generated constructor stub
		super(object);

	}

	public String toString() {
// Aus String-Array den Text für den Knoten extrahieren
		String[] object = (String[])this.getUserObject();
// Im ersten Feld des Arrays steht immer das, was im Baum angezeigt werden soll
		return object[0];

	}
}

class MXJavaScriptTreeCellRenderer extends DefaultTreeCellRenderer {

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
							row, hasFocus);
		if (value instanceof JavaScriptTreeNode) { // Should always be true
			JavaScriptTreeNode nodeact = (JavaScriptTreeNode)value;
			@SuppressWarnings("unchecked")
			List<AstNode> nodes = (List<AstNode>)nodeact.getUserObject();
			String xx = null;
			for (AstNode node : nodes) {
				if (node != null) {
				xx = node.getParent().shortName();
//				if (xx.equals("FunctionNode")) {
//				System.out.println(xx + ":");
//				}
				}
			}

//			System.out.println("Nodeact Icon:" + nodeact.getIcon());
			if (nodeact.getIcon() != null) {
			if (nodeact.getIcon().toString().contains("localvariable_obj.gif")) {
//			if (xx != null && nodeact.getText(sel).contains("(")) {

				setText(null);
				setIcon(null);
			} else {
				setText(nodeact.getText(sel));
				setIcon(nodeact.getIcon());
			}
			}
		}
		return this;
	}
}
@SuppressWarnings("serial")
public class FTLEditMainView extends JPanel {

	private ResourceBundle messagebundle;
	private Locale currentlocale;
	  
	JSplitPane sppFlatFusionWorkSpaceSplitPane; // Splitpanel zwischen JTree und Arbeitsbereich
	JSplitPane sppFlatFusionEditSplitPane; // Splitpanel für zweigeteilten Arbeitsbereich Editor - Outline
	JSplitPane sppFlatFusionTreeSplitPane; // Splitpanel für zweigeteilten Arbeitsbereich Outliner - Bookmarks
	JScrollPane scpPreviewArea; // Scrollkomponente für JTextPane
	JScrollPane scpprojecttree; // Scrollkomponente für JTree
	JScrollPane scpoutlinetree; // Scrollkomponente für JTree Javascript Outliner
	JScrollPane scpjavaoutlinetree; // Scrollkomponente für JTree Java Outliner
	JScrollPane scpbookmarktree; // Scrollkomponente für JTree Java Outliner
	
	JPanel pnTreePanel; // Container für Dateibaum
	JPanel pnSourcePanel; // Container für Quellcodeansicht
	JPanel pnPreviewPanel; // Container für Vorschauansicht
	JPanel pnbuttonPanel; // Container für Buttonleiste
	JPanel pnJSOutlinePanel; // Container für Javascript-Outliner
	JPanel pnJavaOutlinePanel; // Container für Java-Outliner
	JPanel pnBookmarkPanel; // Container für Lesezeichen-Baum
	
	FTLEditPreferencesView preferencesView; // Dialog für Einstellungen

	RSyntaxTextArea sourceCodeTextArea;
	RTextScrollPane scpsourceCodeTextArea;
	
	FileTree projecttree; //JTree für Dateiauswahl
	JTree bookmarktree;
	AbstractSourceTree jsoutlinetree;
	AbstractSourceTree javaoutlinetree;
	
	JToolBar buttonToolBar;  // Menüleiste für Buttons
	JButton jbEditDisplayToggle; //Toggle Button zum Umschalten Editieren - Anzeigen
	JButton jbSave; //Speichern-Button
	JButton jbRefreshPreview; // Aktualisieren der Vorschau
	JButton jbNewFile; // Button "Neue Datei"
	JButton jbRemoveFile; // Button "Datei löschen"
	JButton jbChangeDirectory; // Button "Verzeichnis wechseln"
	JButton jbPreferences; // Button "Verzeichnis wechseln"
	JTextField jtfSearchField; // Suchfeld
	JButton jbSearchNext; // Button Vorwärtssuche
	JButton jbSearchPrev; // Button Rückwärtssuche
	
	String projectDir;

	URL fileurl;
	File datei;
	FileInputStream fis;
	char[] cbuf;
	
//	JEditorPane taPreviewPane;  //

	  public FTLEditMainView(String projectdir) {

// Constructor
		  projectDir = projectdir;
		  String systemlocale = System.getProperty("user.language");
		  
		  if (systemlocale.equals("de")){
			  currentlocale = new Locale("de", "DE");  
		  }
		  else {
			  currentlocale = new Locale("en", "EN");
		  }
		  
		  messagebundle = ResourceBundle.getBundle("resources.MessagesFFE", currentlocale);
		  
// Oberfläche erzeugen
		  
		  initGUI();

	  }
	  
	  void initGUI() {
		  
// Layout fürs gesamte Fenster festlegen

		  setLayout( new BorderLayout() );

// SplitPanel für gesamten Arbeitsbereich initialisieren
		  sppFlatFusionWorkSpaceSplitPane = new JSplitPane( );
		  sppFlatFusionWorkSpaceSplitPane.setDividerLocation( 250 );
		  sppFlatFusionWorkSpaceSplitPane.setForeground( new Color( 0,0,0 ) );
		  sppFlatFusionWorkSpaceSplitPane.setLastDividerLocation( 248 );
		  sppFlatFusionWorkSpaceSplitPane.setOrientation( 1 ); // Vertikal geteilt

// Buttonleiste initialisieren
		  
		  Image image;
		  ImageIcon icon;
		  
		  pnbuttonPanel = new JPanel();
//		  FlowLayout buttonPanelLayout = new FlowLayout();
//		  buttonPanelLayout.setAlignment(FlowLayout.LEADING);
//		  pnbuttonPanel.setLayout(buttonPanelLayout);
		  pnbuttonPanel.setLayout(new BoxLayout(pnbuttonPanel, BoxLayout.LINE_AXIS));

// --- Button "Neue Datei"
		  jbNewFile = new JButton();
		  jbNewFile.setActionCommand("NewFile");
		  image = new ImageIcon(this.getClass().getResource("/resources/greyscale_21.gif")).getImage();
          icon = new ImageIcon(image);
          jbNewFile.setIcon(icon);
          jbNewFile.setToolTipText(messagebundle.getString("001"));
          jbNewFile.setPreferredSize(new Dimension(40,40));
          jbNewFile.setMaximumSize(new Dimension(40,40));
          
		  pnbuttonPanel.add(jbNewFile);
		  
// --- Button "Datei löschen"
		  jbRemoveFile = new JButton();
		  jbRemoveFile.setActionCommand("RemoveFile");
		  image = new ImageIcon(this.getClass().getResource("/resources/greyscale_12.gif")).getImage();
          icon = new ImageIcon(image);
          jbRemoveFile.setIcon(icon);
          jbRemoveFile.setToolTipText(messagebundle.getString("002"));
          jbRemoveFile.setPreferredSize(new Dimension(40,40));
          jbRemoveFile.setMaximumSize(new Dimension(40,40));
		  pnbuttonPanel.add(jbRemoveFile);
		  
// --- Toggle Button Anzeigen/Ändern
		  jbEditDisplayToggle = new JButton();
		  jbEditDisplayToggle.setActionCommand("ToggleEditDisplay");
		  
		  image = new ImageIcon(this.getClass().getResource("/resources/greyscale_19.gif")).getImage();
          icon = new ImageIcon(image);
          jbEditDisplayToggle.setIcon(icon);
          jbEditDisplayToggle.setToolTipText(messagebundle.getString("003"));
          jbEditDisplayToggle.setPreferredSize(new Dimension(40,40));
          jbEditDisplayToggle.setMaximumSize(new Dimension(40,40));
		  pnbuttonPanel.add(jbEditDisplayToggle);

// --- Speichern-Button
		  jbSave = new JButton();
		  jbSave.setActionCommand("Save");
		  
		  image = new ImageIcon(this.getClass().getResource("/resources/greyscale_32.gif")).getImage();
          icon = new ImageIcon(image);
          jbSave.setIcon(icon);
          jbSave.setToolTipText(messagebundle.getString("004"));
          jbSave.setPreferredSize(new Dimension(40,40));
          jbSave.setMaximumSize(new Dimension(40,40));
		  pnbuttonPanel.add(jbSave);

// --- Verzeichnis-wechseln-Button
		  jbChangeDirectory = new JButton();
		  jbChangeDirectory.setActionCommand("ChangeDir");
		  
		  image = new ImageIcon(this.getClass().getResource("/resources/greyscale_6.gif")).getImage();
          icon = new ImageIcon(image);
          jbChangeDirectory.setIcon(icon);
          jbChangeDirectory.setToolTipText(messagebundle.getString("005"));
          jbChangeDirectory.setPreferredSize(new Dimension(40,40));
          jbChangeDirectory.setMaximumSize(new Dimension(40,40));
		  pnbuttonPanel.add(jbChangeDirectory);
		  
// --- Einstellungen-Button
		  jbPreferences = new JButton();
		  jbPreferences.setActionCommand("Preferences");
		  
		  image = new ImageIcon(this.getClass().getResource("/resources/greyscale_35.gif")).getImage();
          icon = new ImageIcon(image);
          jbPreferences.setIcon(icon);
          jbPreferences.setToolTipText(messagebundle.getString("019"));
          jbPreferences.setPreferredSize(new Dimension(40,40));
          jbPreferences.setMaximumSize(new Dimension(40,40));
		  pnbuttonPanel.add(jbPreferences);

		  pnbuttonPanel.add(Box.createHorizontalStrut(20));
		  
// Suchfunktion
		  
		  jtfSearchField = new JTextField();
//		  jtfSearchField.setMargin(new Insets(0,10,0,0));
//		  jtfSearchField.setBorder(BorderFactory.createCompoundBorder(jtfSearchField.getBorder(), BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		  jtfSearchField.setMaximumSize(new Dimension(40000,30));
		  jtfSearchField.setPreferredSize(new Dimension(2000,32));
		  jbSearchPrev = new JButton();
		  image = new ImageIcon(this.getClass().getResource("/resources/Back16.gif")).getImage();
          icon = new ImageIcon(image);
		  jbSearchPrev.setActionCommand("FindPrev");
		  jbSearchPrev.setIcon(icon);

		  jbSearchNext = new JButton();
		  image = new ImageIcon(this.getClass().getResource("/resources/Forward16.gif")).getImage();
          icon = new ImageIcon(image);
		  jbSearchNext.setActionCommand("FindNext");
		  jbSearchNext.setIcon(icon);

		  pnbuttonPanel.add(jbSearchPrev);
		  pnbuttonPanel.add(Box.createHorizontalStrut(5));
		  pnbuttonPanel.add(jtfSearchField);
		  pnbuttonPanel.add(Box.createHorizontalStrut(5));
		  pnbuttonPanel.add(jbSearchNext);
		  

// Trennstrich einfügen
		  
		  pnbuttonPanel.add(Box.createHorizontalStrut(10));
		  pnbuttonPanel.add(new JSeparator(JSeparator.VERTICAL));
		  pnbuttonPanel.add(Box.createHorizontalStrut(5));
// Abstandhalter einfügen - Dieser hält dann zwischen den Elementen davor und danach den Platz in Pixeln frei
		  pnbuttonPanel.add(Box.createRigidArea(new Dimension(200,5)));
// Freien Bereich einfügen, der den maximal verfügbaren Platz einnimmt
// Dadurch werden danach eingefügte Buttons ganz rechts angezeigt
//		  pnbuttonPanel.add(Box.createGlue());
		  
		  pnbuttonPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		  
// --- Vorschau-Aktualisieren-Button
		  jbRefreshPreview = new JButton();
		  jbRefreshPreview.setActionCommand("Refresh");
		  image = new ImageIcon(this.getClass().getResource("/resources/greyscale_9.gif")).getImage();
          icon = new ImageIcon(image);
          jbRefreshPreview.setIcon(icon);
          jbRefreshPreview.setToolTipText(messagebundle.getString("006"));
          jbRefreshPreview.setPreferredSize(new Dimension(40,40));
          jbRefreshPreview.setMaximumSize(new Dimension(40,40));
		  pnbuttonPanel.add(jbRefreshPreview);

// Dateibaum initialisieren

		  pnTreePanel = new JPanel(new BorderLayout());
		  
		  projecttree = new FileTree(projectDir);
		  projecttree.setFileFilter(FileTree.SPECIAL_FILES_FILTER);
		  scpprojecttree = new JScrollPane(projecttree);

// Scrollpane im mittleren Bereich des BorderLayouts plazieren
		  
		  pnTreePanel.add(scpprojecttree, BorderLayout.CENTER);
		  
//SplitPanel Editor-Outline initialisieren
		  
		  sppFlatFusionEditSplitPane = new JSplitPane( );
// Rechter Split-Container bleibt immer zu 75 % sichtbar beim verkleinern
		  sppFlatFusionEditSplitPane.setResizeWeight(0.75);
		  sppFlatFusionEditSplitPane.setForeground( new Color( 0,0,0 ) );
		  sppFlatFusionEditSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT); // Vertikal geteilt

		  sppFlatFusionTreeSplitPane = new JSplitPane( );
		  sppFlatFusionTreeSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		  
// Quellcode-Container anlegen und mit BorderLayout versehen
		  
		  pnSourcePanel = new JPanel(new BorderLayout());
		  
		  sourceCodeTextArea = new RSyntaxTextArea();

		  scpsourceCodeTextArea = new RTextScrollPane(sourceCodeTextArea);
// Bookmarking anschalten - Bookmarks werden mit CTRL + F2 gesetzt/gelöscht - mit F2 kann man durchblättern
		  scpsourceCodeTextArea.getGutter().setBookmarkIcon(new ImageIcon(this.getClass().getResource("/resources/flag_red.png")));
		  scpsourceCodeTextArea.getGutter().setBookmarkingEnabled(true);
		  sourceCodeTextArea.setLineWrap(true);
// Für den Sprachsupport registrieren, nur dann funktioniert die Outliner-Funktion
		  LanguageSupportFactory lsf = LanguageSupportFactory.get();
		  lsf.register(sourceCodeTextArea);
		  sourceCodeTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
		  sourceCodeTextArea.setText("");
		  sourceCodeTextArea.setEditable(false);	
		  sourceCodeTextArea.setBackground(new Color(229,243,255)); //Helles Blau - wenn Panel im Anzeigemodus
		  
		  pnSourcePanel.add( scpsourceCodeTextArea, BorderLayout.CENTER); //JEditorPane zu Container hinzufügen
		  
// Outliner für alle unterstützten Sprachen erzeugen - derzeit Java/Javascript
		  jsoutlinetree = new FTLEditJavaScriptOutlineTree();
		  jsoutlinetree.setGotoSelectedElementOnClick(true);
		  javaoutlinetree = new JavaOutlineTree();
		  javaoutlinetree.setGotoSelectedElementOnClick(true);

// JScrollpane für die Outliner erzeugen
		  scpoutlinetree = new JScrollPane(jsoutlinetree);
		  scpjavaoutlinetree = new JScrollPane(javaoutlinetree);

// Listener und Cell-Renderer für die Outliner registrieren
		 jsoutlinetree.listenTo(sourceCodeTextArea);
		 jsoutlinetree.setCellRenderer(new MXJavaScriptTreeCellRenderer());
		 javaoutlinetree.listenTo(sourceCodeTextArea);
		 jsoutlinetree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		 javaoutlinetree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		 pnJSOutlinePanel = new JPanel(new BorderLayout());
		 pnJSOutlinePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), messagebundle.getString("024")));
		 pnJSOutlinePanel.add(scpoutlinetree, BorderLayout.CENTER);
		 pnJavaOutlinePanel = new JPanel(new BorderLayout());
		 pnJavaOutlinePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), messagebundle.getString("024")));
		 pnJavaOutlinePanel.add(scpjavaoutlinetree, BorderLayout.CENTER);
	     
// JTree für die Lesezeichen erzeugen
		 DefaultMutableTreeNode bookmarkroot = new DefaultMutableTreeNode("Root");
	     bookmarktree = new JTree(bookmarkroot);
	     bookmarktree.setRootVisible(false);
	     bookmarktree.setCellRenderer(new FTLEditBookmarkIconRenderer());
	     scpbookmarktree = new JScrollPane(bookmarktree);
	     
	     pnBookmarkPanel = new JPanel(new BorderLayout());
	     pnBookmarkPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), messagebundle.getString("023")));
	     pnBookmarkPanel.add(scpbookmarktree, BorderLayout.CENTER);
	     
// Jetzt alle initialisierten Komponenten zusammenfügen
	      		  
		  sppFlatFusionWorkSpaceSplitPane.setLeftComponent(pnTreePanel);
		  sppFlatFusionWorkSpaceSplitPane.setRightComponent(pnSourcePanel);
		  sppFlatFusionEditSplitPane.setLeftComponent(sppFlatFusionWorkSpaceSplitPane);
		  sppFlatFusionTreeSplitPane.setTopComponent(pnJSOutlinePanel);
		  sppFlatFusionTreeSplitPane.setBottomComponent(pnBookmarkPanel);
		  sppFlatFusionEditSplitPane.setRightComponent(sppFlatFusionTreeSplitPane);

		  add( pnbuttonPanel, BorderLayout.NORTH );
		  
//		  gbFlatFusionEditMain.setConstraints( sppFlatFusionWorkSpaceSplitPane, gbcWorkSpaceSplitPanel );
		  add( sppFlatFusionEditSplitPane, BorderLayout.CENTER);
//		  add( outlinetree, BorderLayout.EAST);
		  
//		  add (pnPreviewPanel, BorderLayout.SOUTH);

	  }
	  
	  public JPanel getEditorPanel() {
		  
		  return pnPreviewPanel;
	  }
	  
	  public FTLEditPreferencesView getPreferencesView() {
		  
		  return preferencesView;
	  }
	  
	  public FileTree getFileTree() {
		  
		  return projecttree;
	  }
	  
	  public void setSourceContent(String source, boolean refreshPrevOnly) {
		  
// Falls Kennzeichen "Nur Vorschau aktualisieren" nicht gesetzt ist, übergebenen
// Sourcecode in den Editor setzen und neu einfärben
// Ansonsten muß nur die Vorschau aufgefrischt werden
		  
		  if (refreshPrevOnly == false ) {
			  sourceCodeTextArea.setText(source);
// Suche nach leerem String, damit die Markierungen alle verschwinden
		      SearchContext context = new SearchContext();
		      context.setSearchFor("");
		      context.setMatchCase(false);
		      context.setRegularExpression(false);
		      context.setSearchForward(true);
		      context.setWholeWord(false);
		      context.setMarkAll(true);

		      SearchEngine.find(sourceCodeTextArea, context);
// Auf Textanfang positionieren
			  sourceCodeTextArea.setCaretPosition(0);

		  }
	  } 

	  public String getSourceContent() {
		  
		  return sourceCodeTextArea.getText();

	  }
	  
	  public void setSourceContextSuffix(String suffix) {

// Je nach Dateiendung das entsprechende Syntax Highlighting setzen und bei
// unterstützen Sprachen den passenden Outliner einblenden (derzeit nur Java und Javascript)
		  
		  if ( sourceCodeTextArea != null && suffix != null) {
			  
			if (suffix.equals("html")) {
				sourceCodeTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
		 	} else if (suffix.equals("js")) {
		 		sourceCodeTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
		 		int loc = sppFlatFusionEditSplitPane.getDividerLocation();
		 		int loc2 = sppFlatFusionTreeSplitPane.getDividerLocation();
		 		sppFlatFusionTreeSplitPane.setTopComponent(pnJSOutlinePanel);
		 		sppFlatFusionEditSplitPane.setDividerLocation(loc);
				sppFlatFusionTreeSplitPane.setDividerLocation(loc2);
			
		 	} else if (suffix.equals("css")) {
		 		sourceCodeTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSS);
		 	} else if (suffix.equals("java")) {
		 		sourceCodeTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		 		int loc = sppFlatFusionEditSplitPane.getDividerLocation();
		 		int loc2 = sppFlatFusionTreeSplitPane.getDividerLocation();
		 		sppFlatFusionTreeSplitPane.setTopComponent(pnJavaOutlinePanel);
				sppFlatFusionEditSplitPane.setDividerLocation(loc);
				sppFlatFusionTreeSplitPane.setDividerLocation(loc2);

		 	} else {
		 		sourceCodeTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
		 	}
		  
			sourceCodeTextArea.revalidate();
		  }
	  }
	  
	  public void setEditDisplayMode(boolean edit) {
		  
// Setter-Methode für Eingabefähigkeit und der dazu korrespondierenden Hintergrundfarbe
		  
		  if (edit == true) {

// Panel auf eingabebereit schalten - Hintergrundfarbe auf weiß ändern

			  sourceCodeTextArea.setEditable(true);
			  sourceCodeTextArea.setBackground(new Color(255,255,255));
			  
// Toggle Button umschalten
			  Image image = new ImageIcon(this.getClass().getResource("/resources/greyscale_23.gif")).getImage();
		      ImageIcon icon = new ImageIcon(image);
		      jbEditDisplayToggle.setIcon(icon);
		  }
		  else if (edit == false) {

// Panel auf nicht eingabebereit schalten - Hintergrundfarbe auf hellblau ändern
			  sourceCodeTextArea.setEditable(false);
			  sourceCodeTextArea.setBackground(new Color(229,243,255));
  

// Toggle Button umschalten
			  Image image = new ImageIcon(this.getClass().getResource("/resources/greyscale_19.gif")).getImage();
		      ImageIcon icon = new ImageIcon(image);
		      jbEditDisplayToggle.setIcon(icon);
		      
		  }

	  }
	  
	  public JButton getEditDisplayButton() {

// Getter-Methode für Toggle Button Editieren/Anzeigen
		  
		  return jbEditDisplayToggle;
	  }
	  
	  public JButton getSaveButton() {

// Getter-Methode für Speicher-Button
				  
		  return jbSave;
	  }
	  
	  public JButton getRefreshPreviewButton() {

// Getter-Methode für den Button zur Aktualisierung der Vorschau
						  
		  return jbRefreshPreview;
	  }
	  
	  public JButton getNewFileButton() {

// Getter-Methode für den Button zur Anlage einer neuen Datei
								  
		  return jbNewFile;
	  }
	  
	  public JButton getRemoveFileButton() {

// Getter-Methode für den Button zum Löschen einer Datei
										  
		  return jbRemoveFile;
	  }

	  public JButton getChangeDirectoryButton() {

// Getter-Methode für den Button zum Wechseln des Verzeichnisses
												  
		  return jbChangeDirectory;
	  }
	  
	  public JButton getPreferencesButton() {

// Getter-Methode für den Button zur Anzeige des Einstellungen-Dialogs
														  
		  return jbPreferences;
	  }
	  
	  public JButton getSearchNextButton() {

// Getter-Methode für den Button zum Finden des nächsten Suchtreffers
																  
		  return jbSearchNext;
	  }

	  public JButton getSearchPrevButton() {

// Getter-Methode für den Button zum Finden des vorhergehenden Suchtreffers
																		  
		  return jbSearchPrev;
	  }

	  public JTextField getSearchTextField() {

// Getter-Methode für das Such-Textfeld
																				  
		  return jtfSearchField;
	  }

	  public RSyntaxTextArea getEditorTextArea() {

// Getter-Methode für den Editor-Textbereich
		  															  
		  return sourceCodeTextArea;
	  }
	  
	  public JTree getBookmarkTree() {

// Getter-Methode für den Lesezeichen-Baum
		  															  
		  return bookmarktree;
	  }
	  
	  public RTextScrollPane getScrollPaneEditorTextArea() {
		  return scpsourceCodeTextArea;
	  }
	  public void setDividerLocationEditSplitPane(Double percent) {

// Getter-Methode für den Split-Container JTre/Editor und Outliner
																						  
		  sppFlatFusionEditSplitPane.setDividerLocation(percent);
	  }

	  public void setDividerLocationTreeSplitPane(Double percent) {

// Getter-Methode für den Split-Container JTre/Editor und Outliner
																						  
		  sppFlatFusionTreeSplitPane.setDividerLocation(percent);
	  }
	  
// Dialog "Geänderte Datei" anzeigen - Wollen Sie speichern Ja/Nein/Abbrechen
	  
	  public int showDataChangedDialog(File actualFile) {
		  
		 String filename = actualFile.getName().toString();
		 filename = messagebundle.getString("007") + filename + messagebundle.getString("008");
		 int input = JOptionPane.showConfirmDialog(null, filename, messagebundle.getString("009"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		 return input;
	  }
	  
// Dialog zur Eingabe eines neuen Dateinamens anzeigen
	  
	  public String showFileNameDialog() {
		  
		  String userInput = "";
		  
		  while (userInput.equals("")) {
//			  userInput = JOptionPane.showInputDialog(messagebundle.getString("010"));
			  userInput = JOptionPane.showInputDialog(null, messagebundle.getString("010"), messagebundle.getString("001"), JOptionPane.QUESTION_MESSAGE);
			  if ( userInput == null ){
				  userInput = "*";
			  }
		  }
		  
		  if (userInput.equals("*")) {
			  userInput = null;
		  }
		  
		  return userInput;
		  
	  }
	  
// Dialog "Datei bereits vorhanden" anzeigen
	  
	  public void showFileExistsDialog(String filename) {
		  
		  String message = messagebundle.getString("011") + filename + messagebundle.getString("012");
		  
		  JOptionPane.showMessageDialog(null, message, messagebundle.getString("013"), JOptionPane.ERROR_MESSAGE);
	  }
	  
// Dialog "Datei wirklich löschen" Ja/Nein anzeigen
	  
	  public int showFileDeleteDialog(File actualFile) {
		  
			 String filename = actualFile.getName().toString();
			 filename = filename + messagebundle.getString("014");
			 int input = JOptionPane.showConfirmDialog(null, filename, messagebundle.getString("015"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			 return input;
	  }
	  
// Dialog "Bitte Datei auswählen" anzeigen
	  
	  public void showNoDirectoryDialog() {
		 	  
		  JOptionPane.showMessageDialog(null, messagebundle.getString("016"),  messagebundle.getString("017"), JOptionPane.ERROR_MESSAGE);
		  
	  }
	  
	  public String showDirectoryChooser(String startPath) {
		  
		  String directory = null;

		  JFileChooser newDirChooser = new JFileChooser(startPath);
		  newDirChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
		    
		  newDirChooser.setLocale(currentlocale);
		  int filechoice = newDirChooser.showOpenDialog(this);
		    
		  switch(filechoice) {
		    
		    case JFileChooser.APPROVE_OPTION:
		    	if (SpecialDataTypes.isMac() == true) {
		    		if (SpecialDataTypes.isAlias(newDirChooser.getSelectedFile()) == true) {
		    			directory = SpecialDataTypes.resolveAlias(newDirChooser.getSelectedFile()).getAbsolutePath();
		    		}
		    		else {
		    			directory = newDirChooser.getSelectedFile().getAbsolutePath();
		    		}
		    	} else {
		    		directory = newDirChooser.getSelectedFile().getAbsolutePath();
		    	}
		    	
		    	break;

		    case JFileChooser.CANCEL_OPTION:
		    	directory = startPath;
		    	break;
		    }
		  return directory;

	  }
	  
	  public void showBookmarks(HashMap <String, String> bookmarks) {

      		DefaultTreeModel outlinemodel = (DefaultTreeModel)bookmarktree.getModel();
     		DefaultMutableTreeNode outlineroot = (DefaultMutableTreeNode)outlinemodel.getRoot();
    		outlineroot.removeAllChildren();
    		outlinemodel.reload(outlineroot);
    		Gutter gutter = scpsourceCodeTextArea.getGutter();
// Bookmarking mal aus- und wieder einschalten, dadurch werdne die vorhandenen Markierungen gelöscht
    		gutter.setBookmarkingEnabled(false);
    		gutter.setBookmarkingEnabled(true);
/*    		GutterIconInfo[] gutterinfos = gutter.getBookmarks();
    		for (int i = 0; i < gutterinfos.length; i++) {
    			try {
					gutter.toggleBookmark(sourceCodeTextArea.getLineOfOffset(gutterinfos[i].getMarkedOffset()));
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}*/
    		
		  	if (bookmarks != null) {

		  		for (Entry<String, String> bookmark : bookmarks.entrySet()) {

		  			try {
		  				gutter.toggleBookmark(sourceCodeTextArea.getLineOfOffset(Integer.parseInt(bookmark.getKey())));
		  			} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
		  				e.printStackTrace();
		  			} catch (BadLocationException e) {
					// TODO Auto-generated catch block
		  				e.printStackTrace();
		  			}
		  			String[] tokens = { bookmark.getValue(), bookmark.getKey()};
		  			OutlineTreeNode tokenNode = new OutlineTreeNode(tokens);

		  			outlineroot.add(tokenNode);

		  		}
		  		outlinemodel.reload(outlineroot);
		  	}
		  	
// Suche "function", danach wird geprüft, ob nicht " (" oder "(" kommt, damit anonyme Funktionen ausgefiltert werden
// Denn echte (nicht anonyme) Funktionen müssen einen Namen haben, der nicht mit einer Klammer anfangen kann

/*		  String regex    = "(?m)function(?!\\ ?\\()";
		  Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		  Matcher matcher = null;

// Den Wurzelknoten anlegen, dieser ist immer ausgeblendet
		  OutlineTreeNode rootNodeOutline = new OutlineTreeNode(new String[][] {{"Root", Integer.toString(0), "R"}});

	//	        CustomIconRenderer renderer = new CustomIconRenderer();

		  String[] lines = sourcecode.split(System.getProperty("line.separator"));
		  DefaultMutableTreeNode tokenNode = null;

		  for (int x = 0; x < lines.length; x++) {
			  matcher = pattern.matcher(lines[x]);
			  	while (matcher.find()) {
// Prüfen ob vor der Fundstelle ein Kommentar ist, dann diese nicht ausgeben
			  		if (lines[x].substring(0, matcher.start()).contains("//") ||
			  			lines[x].substring(0, matcher.start()).contains("/*")) {
			  			continue;
			  		}
			  		System.out.print("Start index: " + matcher.start());
			  		System.out.print(" End index: " + matcher.end() + " ");
			  		System.out.println(matcher.group());
		            int linenumber = x + 1;
//create the child nodes
		            String[][] tokens = {{ lines[x].replace("function ", ""), Integer.toString(linenumber), "O" }};
		            tokenNode = new OutlineTreeNode(tokens);
		            rootNodeOutline.add(tokenNode);
			  	}

		  }

		  // Model erstellen mit root als Wurzelelement
		  DefaultTreeModel model = new DefaultTreeModel(rootNodeOutline);
		  outlinetree.setModel(model);*/

	  }

/*	  public String fixSourceCode(String sourcecode) {

		  String source = sourcecode;
		  String sourcebegin = null;
		  String sourceend= null;
		  int startpos = 0;
		  int startindex = 0;
		  int endindex = 0;
		  int betweenindex = 0;
		  int tagendindex = 0;
		  
		  do {
			  
			  startindex = source.indexOf("<tr", startpos);
			  
			  if (startindex == -1){
				  break;
			  }
			  endindex = source.indexOf("<tr", startindex + 3);
			  betweenindex = source.indexOf("<td", startpos);
			  
			  if (endindex == -1) {
				  endindex = source.length();
			  }
			  if (betweenindex > startindex && betweenindex < endindex) {
				  startpos = endindex;
				  
			  } else {
				  
				  sourcebegin = source.substring(0, startindex);
				  sourceend = source.substring(startindex);

				  sourceend = sourceend.replaceFirst(">", "><td>");
				  source = sourcebegin + sourceend;
				  startpos = endindex;
			  }
			  
		  } while (source.indexOf("<tr", startpos) != -1);

		  return source;
	  }*/
	  
	  public void selectNodeByPath(String filename) {

	        DefaultMutableTreeNode root =
	            (DefaultMutableTreeNode)projecttree.getModel().getRoot();
	        Enumeration e = root.breadthFirstEnumeration();
	        while(e.hasMoreElements()) {
	            DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.nextElement();
	            File f = (File)node.getUserObject();
	            if(f.getAbsolutePath().equals(filename)) {
	                TreePath path = new TreePath(node.getPath());

	                projecttree.setSelectionPath(path);
	                break;
	            }
	        }
	  }
}



