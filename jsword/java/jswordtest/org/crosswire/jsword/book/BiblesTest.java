
package org.crosswire.jsword.book;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.crosswire.common.util.Logger;
import org.crosswire.jsword.book.data.BookData;
import org.crosswire.jsword.book.stub.StubBook;
import org.crosswire.jsword.passage.Passage;
import org.crosswire.jsword.passage.PassageFactory;
import org.crosswire.jsword.passage.Verse;

/**
 * JUnit Test.
 * 
 * <p><table border='1' cellPadding='3' cellSpacing='0'>
 * <tr><td bgColor='white' class='TableRowColor'><font size='-7'>
 *
 * Distribution Licence:<br />
 * JSword is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License,
 * version 2 as published by the Free Software Foundation.<br />
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.<br />
 * The License is available on the internet
 * <a href='http://www.gnu.org/copyleft/gpl.html'>here</a>, or by writing to:
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 * MA 02111-1307, USA<br />
 * The copyright to this program is held by it's authors.
 * </font></td></tr></table>
 * @see gnu.gpl.Licence
 * @author Joe Walker [joe at eireneh dot com]
 * @version $Id$
 */
public class BiblesTest extends TestCase
{
    public BiblesTest(String s)
    {
        super(s);
    }

    private static final Logger log = Logger.getLogger(BiblesTest.class);
    protected Passage gen11 = null;
    protected BibleMetaData[] bmds = null;
    protected Bible[] bibles = null;

    protected Class[] ignorebibles = 
    {
        StubBook.class,
    };

    protected void setUp() throws Exception
    {
        gen11 = PassageFactory.createPassage("Gen 1:1");

        List lbmds = Books.getBooks(BookFilters.getBibles());
        bibles = new Bible[lbmds.size()];
        bmds = new BibleMetaData[lbmds.size()];

        int i = 0;
        for (Iterator it = lbmds.iterator(); it.hasNext();)
        {
            bmds[i] = (BibleMetaData) it.next();
            bibles[i] = bmds[i].getBible();
            i++;
        }
    }

    protected void tearDown() throws Exception
    {
    }

    public void testGetBible()
    {
        for (int i=0; i<bibles.length; i++)
        {
            Bible bible = bibles[i];
            log.debug("testing bible: "+bible.getBibleMetaData().getFullName());
            assertTrue(bible != null);
        }
    }

    public void testGetBibleMetaData() throws Exception
    {
        for (int i=0; i<bibles.length; i++)
        {
            Bible bible = bibles[i];
            BibleMetaData bmd = bible.getBibleMetaData();
            assertEquals(bmd, bmds[i]);
        }
    }

    public void testMetaData() throws Exception
    {
        for (int i=0; i<bmds.length; i++)
        {
            BibleMetaData bmd = bmds[i];

            assertTrue(bmd.getEdition() != null);
            assertTrue(!bmd.getEdition().endsWith("Edition"));

            Date pub = bmd.getFirstPublished();
            if (pub != null)
            {
                // the date must be in the past
                assertTrue(pub.before(new Date()));
            }

            assertTrue(bmd.getFullName() != null);
            assertTrue(bmd.getFullName().length() > 0);
            assertTrue(bmd.getInitials() != null);
            assertTrue(bmd.getInitials().length() > 0);
            assertTrue(bmd.getName() != null);
            assertTrue(bmd.getName().length() > 0);
            assertTrue(bmd.getFullName().length() > bmd.getName().length());
            assertTrue(bmd.getName().length() > bmd.getInitials().length());
            assertTrue(bmd.getOpenness() != null);
        }
    }

    public void testGetBookMetaData() throws Exception
    {
        for (int i=0; i<bibles.length; i++)
        {
            Bible bible = bibles[i];
            BookMetaData bmd = bible.getBookMetaData();
            assertEquals(bmd, bmds[i]);
        }
    }

    public void testGetDataKey() throws Exception
    {
        for (int i=0; i<bibles.length; i++)
        {
            Bible bible = bibles[i];
            BookData data = bible.getData(new PassageKey("Gen 1:1"));
            assertNotNull(data);
        }
    }

    public void testGetDataPassage() throws Exception
    {
        for (int i=0; i<bibles.length; i++)
        {
            Bible bible = bibles[i];
            BookData data = bible.getData(gen11);
            assertNotNull(data);
        }
    }

    public void testGetFind() throws Exception
    {
        // This only checks that find() does something vaguely sensible
        // I assume that find() just calls findPassage(), where the real tests are
        for (int i=0; i<bibles.length; i++)
        {
            Bible bible = bibles[i];
            Key key = bible.find(new Search("aaron", false));
            assertNotNull("bible="+bible.getBibleMetaData().getFullName(), key);
        }
    }

    public void testFindPassage() throws Exception
    {
        for (int i=0; i<bibles.length; i++)
        {
            Bible ver = bibles[i];

            Passage ref = ver.findPassage(new Search("aaron", false));
            assertTrue(ref != null);
        }
    }

    public void testFindPassage2() throws Exception
    {
        for (int i=0; i<bibles.length; i++)
        {
            // Check that this is a type that we expect to return real Bible data
            Bible ver = bibles[i];
            boolean skip = false; 
            for (int j=0; j<ignorebibles.length; j++)
            {
                // if (ver instanceof fullbibles[j])
                if (ignorebibles[j].isAssignableFrom(ver.getClass()))
                    skip = true;
            }
            if (skip) continue;
            log.debug("thorough testing bible: "+ver.getBibleMetaData().getFullName());

            Passage ref = ver.findPassage(new Search("aaron", false));
            assertTrue(ref.countVerses() > 10);
            ref = ver.findPassage(new Search("jerusalem", false));
            assertTrue(ref.countVerses() > 10);
            ref = ver.findPassage(new Search("god", false));
            assertTrue(ref.countVerses() > 10);
            ref = ver.findPassage(new Search("GOD", false));
            assertTrue(ref.countVerses() > 10);
            ref = ver.findPassage(new Search("brother's", false));
            assertTrue(ref.countVerses() > 2);
            ref = ver.findPassage(new Search("BROTHER'S", false));
            assertTrue(ref.countVerses() > 2);

            ref = ver.findPassage(new Search("maher-shalal-hash-baz", false));
            if (ref.isEmpty())
                ref = ver.findPassage(new Search("mahershalalhashbaz", false));
            assertEquals(ref.countVerses(), 2);
            assertEquals(ref.getVerseAt(0), new Verse("Isa 8:1"));
            assertEquals(ref.getVerseAt(1), new Verse("Isa 8:3"));
            ref = ver.findPassage(new Search("MAHER-SHALAL-HASH-BAZ", false));
            if (ref.isEmpty())
                ref = ver.findPassage(new Search("MAHERSHALALHASHBAZ", false));
            assertEquals(ref.countVerses(), 2);
            assertEquals(ref.getVerseAt(0), new Verse("Isa 8:1"));
            assertEquals(ref.getVerseAt(1), new Verse("Isa 8:3"));
        }
    }

    /*
    public void testGetStartsWith() throws Exception
    {
        for (int i=0; i<bibles.length; i++)
        {
            Bible ver = bibles[i];
            
            if (ver instanceof SearchableBible)
            {
                String[] sa = BookUtil.toStringArray(((SearchableBible) ver).getSearcher().getStartsWith("a"));
                assertTrue(sa != null);
            }
        }
    }

    public void testGetStartsWith2() throws Exception
    {
        for (int i=0; i<bibles.length; i++)
        {
            // Check that this is a type that we expect to return real Bible data
            Bible origver = bibles[i];
            boolean skip = false;
            for (int j=0; j<ignorebibles.length; j++)
            {
                // if (ver instanceof fullbibles[j])
                if (origver.getClass().isAssignableFrom(ignorebibles[j]))
                    skip = true;
            }
            if (skip) continue;
            log.debug("thorough testing bible: "+origver.getBibleMetaData().getFullName());

            if (origver instanceof SearchableBible)
            {
                SearchableBible ver = (SearchableBible) origver;
                String[] sa = BookUtil.toStringArray(ver.getSearcher().getStartsWith("jos"));
                assertTrue(sa.length > 5);
                sa = BookUtil.toStringArray(ver.getSearcher().getStartsWith("jerusale"));
                assertEquals(sa[0], "jerusalem");
                sa = BookUtil.toStringArray(ver.getSearcher().getStartsWith("maher-shalal"));
                if (sa.length == 0)
                {
                    sa = BookUtil.toStringArray(ver.getSearcher().getStartsWith("mahershalal"));
                    assertEquals(sa[0], "mahershalalhashbaz");
                }
                else
                {
                    assertEquals(sa[0], "maher-shalal-hash-baz");
                }
                assertEquals(sa.length, 1);
                sa = BookUtil.toStringArray(ver.getSearcher().getStartsWith("MAHER-SHALAL"));
                if (sa.length == 0)
                {
                    sa = BookUtil.toStringArray(ver.getSearcher().getStartsWith("MAHERSHALAL"));
                    assertEquals(sa[0], "mahershalalhashbaz");
                }
                else
                {
                    assertEquals(sa[0], "maher-shalal-hash-baz");
                }
                assertEquals(sa.length, 1);
                sa = BookUtil.toStringArray(ver.getSearcher().getStartsWith("XXX"));
                assertEquals(sa.length, 0);
            }
        }
    }
    */
}