package com.acmutv.ontoqa.core.lexicon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
* This class realizes JUnit tests for {@link LexiconUsage}.
* @author Antonella Botte {@literal <abotte@acm.org>}
* @author Giacomo Marciani {@literal <gmarciani@acm.org>}
* @author Debora Partigianoni {@literal <dpartigianoni@acm.org>}
* @since 1.0
* @see Lexicon
*/
public class LexiconUsageTest {
	
	@Test
	public void test_getAllLexiconElement() throws IOException{
		List<LEntry> actual=new ArrayList();
		List<LEntry>expected= new ArrayList();
		
//	    actual = LexiconUsage.getAllLexiconElement();
//	    
//	    expected = LexiconUsage.getAllLexiconElement();

	    Assert.assertEquals(expected, actual);
	}

}
