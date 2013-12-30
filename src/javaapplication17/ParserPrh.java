/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication17;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;




/**
 *
 * @author ulit6
 */
public class ParserPrh extends DefaultHandler implements Runnable{
    private Prh prh;
    private String temp;
    private JednostkaMiary jm;
    private ArrayList<JednostkaMiary> jmList = new ArrayList<JednostkaMiary>();
    private GrupaSubstancji gs;
    private ArrayList<GrupaSubstancji> gsList = new ArrayList<GrupaSubstancji>();
    private Taryfa taryfa;
    private ArrayList<Taryfa> taList = new ArrayList<Taryfa>();
    private Katalog katalog;
    private ArrayList<Katalog> kaList = new ArrayList<Katalog>();
    private PozycjaPrh pozycjaprh;
    private ArrayList<PozycjaPrh> poList = new ArrayList<PozycjaPrh>();
    private String filename;
    private static final String OUTPUT_FOLDER = "/tmp";

     public static void main(String[] args) throws IOException, SAXException,
                     ParserConfigurationException {
              /*
              //Create a "parser factory" for creating SAX parsers
              SAXParserFactory spfac = SAXParserFactory.newInstance();

              //Now use the parser factory to create a SAXParser object
              SAXParser sp = spfac.newSAXParser();

              //Create an instance of this class; it defines all the handler methods
              ParserPrh handler = new ParserPrh("D:\\CWL_PRH_190_20121220_SW.xml");
             
              //Finally, tell the parser to parse the input and notify the handler
            //  File f = new File("D:\\CWL_PRH_190_20121220_SW.xml");
              sp.parse(handler.filename,handler);
              handler.readList();
              System.exit(0);
              
               */
         ParserPrh handler = new ParserPrh("/home/pawel/Downloads/CWL_PRH_195_20130521_SW.PRH");
         handler.unzip();
         Thread thread = new Thread(handler,"import");
         thread.start();
        

       }
   public  ParserPrh(String afilename)
   {
      filename=afilename;
      //this.unzip(filename);
   }
   public void unzip()
   {
       byte[] buffer = new byte[1024];
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(filename));
            ZipEntry ze = zis.getNextEntry();
            while(ze!=null)
            {
                String fileName = ze.getName();
                File newFile = new File(this.OUTPUT_FOLDER+File.separator+fileName);
                filename = newFile.getAbsolutePath();
                System.out.println(newFile.getAbsolutePath());
                FileOutputStream fos = new FileOutputStream(newFile);   
                int len;
                

                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
 
                fos.close();   
                
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
    	    zis.close();
        } catch (IOException ex ) {
            Logger.getLogger(ParserPrh.class.getName()).log(Level.SEVERE, null, ex);
        }
       
  
       
       
   }
   public void deleteFile()
   {
       File file = new File(this.filename);
       if(file.delete())
       {
           System.out.println(file.getAbsolutePath() + " is deleted!");
       }
   }
   public void characters(char[] buffer, int start, int length) {
              temp = new String(buffer, start, length);
       }

   public void startElement(String uri, String localName,
                     String qName, Attributes attributes) throws SAXException {
              temp = "";
              if (qName.equalsIgnoreCase("komunikat")) {
                     prh = new Prh(attributes.getValue("typ"),attributes.getValue("wersja"),
                             attributes.getValue("czas-gen"),Integer.parseInt(attributes.getValue("wer-tech")),
                             Integer.parseInt(attributes.getValue("oddz-nfz")));
              }
              if (qName.equalsIgnoreCase("jednostka-miary")) {
                  jm = new JednostkaMiary(Integer.parseInt(attributes.getValue("kod")),attributes.getValue("nazwa"),attributes.getValue("status"));
                  jmList.add(jm);
              }
              if (qName.equalsIgnoreCase("grupa-substancji")) {
                  gs = new GrupaSubstancji(Integer.parseInt(attributes.getValue("kod")),attributes.getValue("nazwa"),
                          attributes.getValue("status"),Integer.parseInt(attributes.getValue("jednostka")));
                  gsList.add(gs);
              }
              if (qName.equalsIgnoreCase("taryfa")) {
                  taryfa = new Taryfa(attributes.getValue("data-od"),attributes.getValue("data-do"),Double.parseDouble(attributes.getValue("taryfa-bazowa")),
                          Double.parseDouble(attributes.getValue("lb-roz-jednostek")));
                  
                  taList.add(taryfa);
                  katalog.addTaryfa(taryfa);
              }
              if (qName.equalsIgnoreCase("katalog")) {
                  katalog = new Katalog(attributes.getValue("kod-katalogu"));
                  
              }
              if (qName.equalsIgnoreCase("pozycja")) {
                  pozycjaprh = new PozycjaPrh(Integer.parseInt(attributes.getValue("kod")),attributes.getValue("ean"),attributes.getValue("symbol"),
                          Integer.parseInt(attributes.getValue("grupa-substancji-kod")),
                          attributes.getValue("nazwa"),attributes.getValue("postac"),attributes.getValue("dawka"),attributes.getValue("opak"),
                          attributes.getValue("podm-odp"),
                          Double.parseDouble(attributes.getValue("ilosc-subst")),Double.parseDouble(attributes.getValue("lb-sztuk")),
                          attributes.getValue("status"));
                  
       }

       }


   public void endElement(String uri, String localName, String qName)
                     throws SAXException {
       if (qName.equalsIgnoreCase("jednostka-miary")) {

           jm = null;
       }
       if (qName.equalsIgnoreCase("grupa-substancji")) {

           gs = null;
       }
       if (qName.equalsIgnoreCase("taryfa")) {

           taryfa = null;
       }
       if (qName.equalsIgnoreCase("katalog")) {
           kaList.add(katalog);
           pozycjaprh.setKatalog(katalog);
           
       }
       if (qName.equalsIgnoreCase("pozycja")) {
           poList.add(pozycjaprh);
           pozycjaprh = null;
           katalog = null;
       }
    }

     private void readList() {
         System.out.println(prh);
         Iterator<JednostkaMiary> jmit = jmList.iterator();
         Iterator<GrupaSubstancji> gsit = gsList.iterator();
         Iterator<Taryfa> tait = taList.iterator();
         Iterator<Katalog> kait = kaList.iterator();
         Iterator<PozycjaPrh> poit = poList.iterator();
         while(jmit.hasNext())
         {
             System.out.println(jmit.next().toString());
         }

         while(gsit.hasNext())
         {
             System.out.println(gsit.next().toString());
         }
         while(tait.hasNext())
         {
             System.out.println(tait.next().toString());
         }
         while(kait.hasNext())
         {
             System.out.println(kait.next().toString());
         }
         while(poit.hasNext())
         {
             System.out.println(poit.next().toString());
         }
     }

    @Override
    public void run() {
        SAXParser sp = null;
        SAXParserFactory spfac = SAXParserFactory.newInstance();
        try {
            //Now use the parser factory to create a SAXParser object
             sp = spfac.newSAXParser();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ParserPrh.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(ParserPrh.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            sp.parse(this.filename, this);
        } catch (SAXException ex) {
            Logger.getLogger(ParserPrh.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ParserPrh.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.readList();
       // this.dbInsert();
        this.deleteFile();
    }
    private void dbInsert() 
    {
    
            Statement stmt;
            CallableStatement cs = null;
            Connection con=null;
            PreparedStatement ps = null;     
            String sql;
            Integer k=1;
            Iterator<JednostkaMiary> ijm = jmList.iterator();
            Iterator<GrupaSubstancji> igs = gsList.iterator();
            Iterator<PozycjaPrh> poit = poList.iterator();
            Integer idpr = 0; 
            CallableStatement cs2 = null;
            
      
               try {
                   Class.forName("com.mysql.jdbc.Driver");
               } catch (ClassNotFoundException ex) {
                   Logger.getLogger(ParserPrh.class.getName()).log(Level.SEVERE, null, ex);
               }
          
               String url = "jdbc:mysql://localhost:3306/IMPORTER";
               try {            
                   con = DriverManager.getConnection(url,"root", "mlody6");
               } catch (SQLException ex) {
                   Logger.getLogger(ParserPrh.class.getName()).log(Level.SEVERE, null, ex);
               }
                   
                   
              
                  
                    sql = "CALL LNPRH_ADD(?,?,?,?)";
           try {
               cs=con.prepareCall(sql);
           } catch (SQLException ex) {
               Logger.getLogger(ParserPrh.class.getName()).log(Level.SEVERE, null, ex);
           }
           try{
                    cs.setInt(1,prh.getWerTech());
                    cs.setDouble(2, Double.parseDouble(prh.getWersja()));
                    cs.setInt(3, prh.getOddzNfz());
                    cs.setString(4,prh.getCzasGen());
                    cs.executeQuery();
           }catch(SQLException ex)
           {
               Logger.getLogger(ParserPrh.class.getName()).log(Level.SEVERE, null, ex);
           }
         sql = "CALL LJDMR_ADD(?,?,?)";
         try {
              cs=con.prepareCall(sql);
         } catch (SQLException ex) {
            Logger.getLogger(ParserPrh.class.getName()).log(Level.SEVERE, null, ex);
         }
         
         while(ijm.hasNext())
         {
             JednostkaMiary jm = ijm.next();
             try{
                cs.setInt(1, jm.getKod());
                cs.setString(2,jm.getNazwa() );
                cs.setString(3, jm.getStatus());
                cs.executeQuery();
             }
             catch(SQLException ex) {
             Logger.getLogger(ParserPrh.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         
         sql = "CALL LGRSU_ADD(?,?,?,?,?)";
         try {
              cs=con.prepareCall(sql);
         } catch (SQLException ex) {
            Logger.getLogger(ParserPrh.class.getName()).log(Level.SEVERE, null, ex);
         }
         while(igs.hasNext())
         {
             GrupaSubstancji gs = igs.next();
             try{
                 cs.setInt(1, prh.getWerTech());
                 cs.setInt(2, gs.getKod());
                 cs.setString(3,gs.getNazwa().trim() );
                 cs.setInt(4, gs.getJednostka());
                 cs.setString(5, gs.getStatus());
                 System.out.println(gs);
                 cs.executeQuery();
             }
             catch(SQLException ex) {
             Logger.getLogger(ParserPrh.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         sql = "CALL LPRHA_ADD(?,?,?,?,?,?,?,?,?,?,?,?,?)";
         try {
              cs=con.prepareCall(sql);
         } catch (SQLException ex) {
            Logger.getLogger(ParserPrh.class.getName()).log(Level.SEVERE, null, ex);
         }
         while(poit.hasNext())
         {
             PozycjaPrh po = poit.next();
             try{
                 cs.setInt(1, prh.getWerTech());
                 cs.setString(2, po.getEan());
                 cs.setString(3,po.getSymbol() );
                 cs.setInt(4, po.getGsl());
                 cs.setString(5, po.getNazwa());
                 cs.setString(6, po.getPostac());
                 cs.setString(7, po.getDawka());
                 cs.setString(8, po.getOpak());
                 cs.setString(9, po.getPodmOdp());
                 cs.setDouble(10, po.getIlosc());
                 cs.setDouble(11, po.getSztuki());
                 cs.setString(12,po.getStatus());
                 cs.setString(13, po.getKod().toString());
                 System.out.println(po.getKod()+"|"+po.getIlosc());
                 cs.executeQuery();
                 Katalog ka = po.getKatalog();
                 if( ka!=null)
                 {
                    // sql = "SELECT LAST_INSERT_ID()";
                     ps = con.prepareStatement("SELECT LAST_INSERT_ID()");
                     ResultSet rs = ps.executeQuery();
                     while(rs.next())
                     {
                         idpr=rs.getInt(1);                     
                     }
                     System.out.println("Null: "+po);
                     String sql2 = "CALL TRPRH_ADD(?,?,?,?,?,?)";
                     cs2=con.prepareCall(sql2);
                     ArrayList<Taryfa> t = ka.getTaryfa();
                     Iterator<Taryfa> tait=t.iterator();
                     while(tait.hasNext())
                     {
                         Taryfa ta=tait.next();
                         cs2.setInt(1, idpr);
                         cs2.setString(2, ka.getKodKatalogu());
                         cs2.setString(3, ta.getDataOd());
                         cs2.setString(4, ta.getDataDo());
                         cs2.setDouble(5, ta.getTaryfaBazowa())   ;
                         cs2.setDouble(6, ta.getLbRoz());
                         cs2.executeQuery();
                                 
                         
                     }
                 }
             }
             catch(SQLException ex) {
             Logger.getLogger(ParserPrh.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         
         
         
         sql = "CALL LNPRH_EDT(?)";
         try {
               cs=con.prepareCall(sql);
           } catch (SQLException ex) {
               Logger.getLogger(ParserPrh.class.getName()).log(Level.SEVERE, null, ex);
           }
        try {
            cs.setInt(1,prh.getWerTech());
             cs.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(ParserPrh.class.getName()).log(Level.SEVERE, null, ex);
        }
                   
        
         
}



    }


