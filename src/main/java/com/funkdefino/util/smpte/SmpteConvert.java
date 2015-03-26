package com.funkdefino.util.smpte;

import com.differitas.common.util.UtilException;
import com.differitas.common.util.xml.*;

import java.io.File;
import java.util.*;

/**
 * <p/>
 * <code>$Id: $</code>
 * @author Differitas (David M. Lang)
 * @version $Revision: $
 */
public final class SmpteConvert {

  //** --------------------------------------------------------------- Constants

  private final static String s_sElmntStartOffset = "StartOffset";
  private final static String s_sElmntSignature   = "TimeSignature";
  private final static String s_sElmntSampleRate  = "SampleRate";
  private final static String s_sElmntDelay       = "Delay";
  private final static String s_sElmntSMPTE       = "SMPTE";
  private final static String s_sElmntBPM         = "BPM";
  private final static String s_sElmntBars        = "Bars";
  private final static String s_sAttrbHours       = "hours";
  private final static String s_sAttrbMinutes     = "minutes";
  private final static String s_sAttrbSeconds     = "seconds";
  private final static String s_sAttrbFrames      = "frames";
  private final static String s_sAttrbValue       = "value";
  private final static String s_sAttrbName        = "name";

  //** -------------------------------------------------------------------- Data

  private String m_sName;
  private int m_nStartOffset;
  private int m_nSignature;
  private int m_nSampleRate;
  private int m_nDelay;
  private int m_nFrames;
  private int m_nBPM;
  private Map<Integer, String> m_mpBars;

  //** ------------------------------------------------- Application entry point

  /**
   * Application entry point.
   * @param args command line arguments
   */
  public static void main(String[] args) {

    if(args == null || args.length == 0) {
       System.out.println("Usage : SmpteConvert [configuration]");
       return;
    }
    try {
      XmlDocument doc = new XmlDocument(new File(args[0]), false);
      new SmpteConvert(doc.getRootElement());
    }
    catch(Exception excp) {
      excp.printStackTrace();
    }
  }

  //** ------------------------------------------------------------ Construction

  /**
   * Ctor.
   * @param eConfig a configuration element.
   * @throws UtilException on error.
   */
  public SmpteConvert(XmlElement eConfig) throws UtilException{
    initialise(eConfig);
    for(Map.Entry<Integer, String> entry : m_mpBars.entrySet()) {
      double smpte = calculateSmpte(entry.getKey());
      String s = String.format("%10s (%03d) - %s", entry.getValue(), entry.getKey(), formatSmpte(smpte));
      System.out.println(s);
    }
  }

  //** ---------------------------------------------------------- Implementation

  private double calculateSmpte(int nBar) {
    double offset = m_nStartOffset;
    offset += ((((double)m_nSignature * 60) / m_nBPM) * (nBar - 1));
    offset += ((double)m_nDelay) / m_nSampleRate;
    return offset;
  }

  private String formatSmpte(double smpte) {
    int total     = (int)Math.floor(smpte);
    double sub    = (smpte - total) * m_nFrames;
    int frames    = (int)Math.floor(sub);
    int subFrames = (int)((sub - frames) * 100);
    int hours     = total / 3600;
    int minutes   = (total -= (hours * 3600)) / 60;
    int seconds   = (total -= (minutes * 60));
    return String.format("%02d:%02d:%02d:%02d:%02d", hours, minutes, seconds, frames, subFrames);
  }

  /**
   * This performs startup initialisation.
   * @param eConfig a configuration element.
   * @throws UtilException on error.
   */
  private void initialise(XmlElement eConfig) throws UtilException {

    XmlElement eStartOffset = XmlValidate.getElement(eConfig, s_sElmntStartOffset);
    XmlElement eSignature   = XmlValidate.getElement(eConfig, s_sElmntSignature  );
    XmlElement eSampleRate  = XmlValidate.getElement(eConfig, s_sElmntSampleRate );
    XmlElement eDelay       = XmlValidate.getElement(eConfig, s_sElmntDelay      );
    XmlElement eSMPTE       = XmlValidate.getElement(eConfig, s_sElmntSMPTE      );
    XmlElement eBPM         = XmlValidate.getElement(eConfig, s_sElmntBPM        );
    XmlElement eBars        = XmlValidate.getElement(eConfig, s_sElmntBars       );

    int nHours   = Integer.parseInt(XmlValidate.getAttribute(eStartOffset, s_sAttrbHours  ));
    int nMinutes = Integer.parseInt(XmlValidate.getAttribute(eStartOffset, s_sAttrbMinutes));
    int nSeconds = Integer.parseInt(XmlValidate.getAttribute(eStartOffset, s_sAttrbSeconds));

    m_sName        = XmlValidate.getAttribute(eConfig, s_sAttrbName);
    m_nStartOffset = (((nHours * 60) + nMinutes) * 60) + nSeconds;
    m_nSignature   = Integer.parseInt(eSignature.getContent());
    m_nSampleRate  = Integer.parseInt(eSampleRate.getContent());
    m_nDelay       = Integer.parseInt(eDelay.getContent());
    m_nFrames      = Integer.parseInt(XmlValidate.getAttribute(eSMPTE, s_sAttrbFrames));
    m_nBPM         = Integer.parseInt(eBPM.getContent());

    m_mpBars = new TreeMap<Integer, String>();
    for(XmlElement eBar : eBars.getChildren()) {
      String sValue = XmlValidate.getAttribute(eBar, s_sAttrbValue);
      m_mpBars.put(Integer.parseInt(sValue), eBar.getContent());
    }

  } // initialise()
  
} // SmpteConvert
