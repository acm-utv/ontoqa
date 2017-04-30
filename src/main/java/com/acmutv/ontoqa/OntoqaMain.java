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
import com.acmutv.ontoqa.core.CoreController;
import com.acmutv.ontoqa.core.exception.OntoqaFatalException;
import com.acmutv.ontoqa.core.exception.OntoqaParsingException;
import com.acmutv.ontoqa.core.exception.QueryException;
import com.acmutv.ontoqa.core.exception.QuestionException;
import com.acmutv.ontoqa.core.knowledge.answer.Answer;
import com.acmutv.ontoqa.tool.runtime.RuntimeManager;
import com.acmutv.ontoqa.ui.CliService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;

/**
 * The Ontoqa application entry-point.
 * @author Antonella Botte {@literal <abotte@acm.org>}
 * @author Giacomo Marciani {@literal <gmarciani@acm.org>}
 * @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
 * @since 1.0
 * @see AppConfigurationService
 * @see RuntimeManager
 */
class OntoqaMain {

  private static final Logger LOGGER = LoggerFactory.getLogger(OntoqaMain.class);

  /**
   * The app main method, executed when the program is launched.
   * @param args The command line arguments.
   */
  public static void main(String[] args) {
    CliService.handleArguments(args);

    try {
      AppConfigurationService.configureApp();

      while (true) {
        String question = CliService.getInput("Insert your question (empty to shutdown)");
        if (question.isEmpty()) break;
        CliService.print("Your question is: %s", question);
        Answer answer = CoreController.process(question);
        CliService.print("My %s: %s",
            (answer.size() > 1) ? "answers are" : "answer is", answer.toPrettyString());
      }
    } catch (NoSuchElementException exc) {
      LOGGER.error("Cannot read question. Shutting down...");
      System.exit(-1);
    } catch (QuestionException exc) {
      LOGGER.error("Your question contains error. Shutting down...");
    } catch (OntoqaParsingException exc) {
      LOGGER.error("Your question cannot be parsed. {}", exc.getMessage());
    } catch (QueryException exc) {
      LOGGER.warn(exc.getMessage());
    } catch (OntoqaFatalException exc) {
      LOGGER.error(exc.getMessage());
      System.exit(-1);
    } catch (Exception e) {
      e.printStackTrace();
    }

    System.exit(0);
  }
}
