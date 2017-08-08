package com.funkdefino.util.smpte;

import com.funkdefino.common.unittest.CTestCase;
import com.funkdefino.common.util.xml.*;
import junit.framework.Test;

/**
 * <p/>
 * <code>$Id: $</code>
 * @author Differitas (David M. Lang)
 * @version $Revision: $
 */
public final class SmpteConvertUnitTest extends CTestCase {

  //** -------------------------------------------------------------------- Data

  private static SmpteConvert s_convert;

  //** ------------------------------------------------------------ Construction

  public SmpteConvertUnitTest(String sMethod) {
    super(sMethod);
  }

  //** -------------------------------------------------------------- Operations

  /**
   * This performs suite initialisation.
   * @throws Exception on error.
   */
  public void load() throws Exception {
    XmlDocument config = XmlDocument.fromResource(getClass(), "Pailah.xml");
    s_convert = new SmpteConvert(config.getRootElement());
  }

  /**
   * Initialises test fixtures.
   * @throws Exception
   */
  protected void setUp() throws Exception {
    super.setUp();
  }

  /**
   * Releases test fixture resources.
   * @throws Exception
   */
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  /**
   * Returns the suite of cases for testing.
   * @return the test suite.
   */
  public static Test suite() {
    return CTestCase.suite(SmpteConvertUnitTest.class,
                           "UnitTest.xml",
                           "test#1");
  }

  //** ------------------------------------------------------------------- Tests

  public void test01() throws Exception {
  }

} // class ConnectionUnitTest
