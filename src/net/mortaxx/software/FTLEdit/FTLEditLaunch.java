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

--> Rsyntaxtextarea/RSTALanguageSupport/AutoComplete/RSTAUI
Copyright (c) 2012, Robert Futrell - https://github.com/bobbylight

--> Rhino
Copyright (c) 2012, Mozilla Corporation - https://github.com/mozilla/rhino

--> Java Native Access (JNA)
Copyright (c) 2007-2019 by Timothy Wall - https://github.com/java-native-access/jna#readme

*/

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

//import org.cef.CefApp;
//import chrriis.dj.nativeswing.swtimpl.NativeInterface;

//import chrriis.dj.nativeswing.swtimpl.NativeInterface;


public class FTLEditLaunch {

//	CefApp cefApp_;
	static String startDir = null;
	static String startFile = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

// !!!! Perform startup initialization on platforms that require it. !!!!
/*        if (!CefApp.startup()) {
            System.out.println("Startup initialization failed!");
            return;
        }*/
		  FileInputStream fis = null;
		  
	  	if (System.getProperty("os.name").startsWith("Mac OS")) {
	  		System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS"); // Damit reagiert wird die windowClosing.Methode ausgeführt
	  		File startfile = new File("/tmp/filelist.ftl");
	  		if (startfile.exists()) {
	  			try {
					fis = new FileInputStream(startfile);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  DataInputStream dis = new DataInputStream(fis);
				  BufferedReader br = new BufferedReader(new InputStreamReader(dis));
				  
				  try {
					startFile = br.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  		}
	  		File startdir = new File("/tmp/filepath.ftl");

	  		if (startdir.exists()) {
	  			try {
					fis = new FileInputStream(startdir);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  DataInputStream dis = new DataInputStream(fis);
				  BufferedReader br = new BufferedReader(new InputStreamReader(dis));
				  
				  try {
					startDir = br.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  		}
	  		
	  	} else if (System.getProperty("os.name").indexOf("nix") >= 0 || System.getProperty("os.name").indexOf("nux") >= 0 ){
// Unter Linux wird das erste uebergebene Argument als Dateiname gewertet
	  		if (args.length > 0) {
		  		File startfile = new File(args[0]);
		  		if (startfile.exists()) {
		  			startFile = args[0];
		  			startDir = startfile.getParent();
		  		}
	  		}
// Kein Mac-System - DJNative Swing initialisieren
//	  		NativeInterface.open();
	  	}

		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	    SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	if (startDir == null) {
	        		startDir = System.getProperty("user.home");
	        	}
	        	FTLEditController controller = new FTLEditController(startDir, false, startFile);
		
	        }
	    });
// Kein Mac-System - DJNative Swing starten
	  	if (System.getProperty("os.name").startsWith("Mac OS") != true) {

//	  		NativeInterface.runEventPump();
//		    NativeInterface.close();
//		    System.exit(0);
	  	}
	  	

	}

}
