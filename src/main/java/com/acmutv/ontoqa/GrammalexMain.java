/*
  The MIT License (MIT)

  Copyright (c) 2016 Antonella Botte, Giacomo Marciani and Debora Partigianoni

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:


  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.


  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.
 */

package com.acmutv.ontoqa;

import com.acmutv.ontoqa.config.AppConfigurationService;
import com.acmutv.ontoqa.core.grammar.Grammar;
import com.acmutv.ontoqa.core.lemon.LexicalEntry;
import com.acmutv.ontoqa.core.lexicon.LexiconFormat;
import com.acmutv.ontoqa.core.lexicon.LexiconUsage;
import com.acmutv.ontoqa.core.semantics.sltag.SerializeSltag;
import com.acmutv.ontoqa.tool.runtime.RuntimeManager;
import com.acmutv.ontoqa.tool.runtime.ShutdownHook;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import javax.swing.JFileChooser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Grammalex application entry-point.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see AppConfigurationService
 * @see RuntimeManager
 */
class GrammalexMain {

  private static final Logger LOGGER = LogManager.getLogger(GrammalexMain.class);
  
  private static File fileJson = new File("data/grammar/sltag.json");

  /**
   * The app main method, executed when the program is launched.
   * @param args The command line arguments.
 * @throws IllegalAccessException 
 * @throws InstantiationException 
   */
  public static void main(String[] args) throws InstantiationException, IllegalAccessException {
    //CliService.handleArguments(args);
    RuntimeManager.registerShutdownHooks(new ShutdownHook());
    try {
		
		Path path = FileSystems.getDefault().getPath("data/lexicon").toAbsolutePath();
		String currentDirectory = path.toString();
		final JFileChooser fc = new JFileChooser(currentDirectory);

		int returnVal = fc.showOpenDialog(null);
	    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (returnVal == JFileChooser.OPEN_DIALOG) {
		    File file = fc.getSelectedFile();
			System.out.println("File Select: "+ file.getName()+ "\n\n");
	    	List<LexicalEntry> lEntries= LexiconUsage.getLexicalEntries(file.getAbsolutePath(),"", LexiconFormat.RDFXML);
	    	Grammar grammar = SerializeSltag.getAllElementarySltag(lEntries);
			SerializeSltag.writeGrammarOnFile(grammar, fileJson);
		}

    	  
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    System.exit(0);
  }
}