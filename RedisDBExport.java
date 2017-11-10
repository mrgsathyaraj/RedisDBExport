import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import javax.swing.*;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class RedisDBExport extends JPanel implements ActionListener
{
    private JLabel jcomp1;
    private JLabel jcomp2;
    private JTextField jcomp3;
    private JButton jcomp4;
    private JTextArea  jcomp5;
    private JLabel jcomp6;
    private JComboBox jcomp7;
    private JButton jcomp8;
    private JButton jcomp9;


    public RedisDBExport() {
        //construct preComponents
        String[] jcomp7Items = {"       Env1", "       Env2" ,"       Env3", "       Env4",};

        //construct components
        jcomp1 = new JLabel ("Redis DB Export Tool V1.1");
        jcomp2 = new JLabel ("HASH");
        jcomp3 = new JTextField (5);
        jcomp4 = new JButton ("Submit");
        jcomp5 = new JTextArea  ();
        jcomp6 = new JLabel ("  Environment");
        jcomp7 = new JComboBox (jcomp7Items);
        jcomp8 = new JButton ("Reset");
        jcomp9 = new JButton ("Delete");
 
 
jcomp3.addActionListener(this);
jcomp4.addActionListener(this);
jcomp7.addActionListener(this);
jcomp8.addActionListener(this);
jcomp9.addActionListener(this);

        //adjust size and set layout
        setPreferredSize (new Dimension (667, 485));
        setLayout (null);

        //add components
        add (jcomp1);
        add (jcomp2);
        add (jcomp3);
        add (jcomp4);
        add (jcomp5);
        add (jcomp6);
        add (jcomp7);
        add (jcomp8);
        add (jcomp9);
        
        //set component bounds (only needed by Absolute Positioning)
        jcomp1.setBounds (215, 5, 165, 30);
        jcomp2.setBounds (40, 75, 85, 25);
        jcomp3.setBounds (85, 70, 520, 30);
        jcomp4.setBounds (220, 125, 100, 25);
        jcomp5.setBounds (35, 180, 595, 300);
        jcomp6.setBounds (455, 5, 80, 25);
        jcomp7.setBounds (555, 5, 100, 25);
        jcomp8.setBounds (345, 125, 100, 25);
        jcomp9.setBounds (470, 125, 100, 25);
        
        //excel sheet implementations
       
    }
    
    
public void actionPerformed(ActionEvent e) 
{
try
 
{
 
 
 
System.out.println("Connected to Redis0");
if(e.getSource() == jcomp4 && jcomp7.getSelectedIndex() == 0 && jcomp3.getText()!= null && jcomp3.getText().length()  >0) 
{
 
 
try 
{
 
Jedis jedis = new Jedis("host", port);
jedis.auth("opsredis");
System.out.println("Connected to Redis1");
jedis.select(1);
 
Boolean s =jedis.exists(jcomp3.getText());
System.out.println(s);
String HashType=jedis.type(jcomp3.getText());
System.out.println(HashType);
if(s.equals(true) && HashType.equals("hash"))
{
 
 Long TTL=jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties");
 TimeZone tz = TimeZone.getTimeZone("UTC");
 SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
 df.setTimeZone(tz);
 String time = df.format(new Date(TTL*1000L));
 System.out.println("TTL in hrs:"+time);
 //System.out.println("TTL Value:"+jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties"));
 
 jcomp5.append("\n"+"Hash Name:"+"\n"+jcomp3.getText()+"\n"+"TTL in hrs:"+"\n"+time+"\n");
 
   
 
Map<String, String> response=jedis.hgetAll(jcomp3.getText());
for (Entry<String, String> entry : response.entrySet())
{
//System.out.println("Test Json Format Start ");
//String Test=entry.getValue();
 
//JSONObject json = (JSONObject) JSONSerializer.toJSON(entry.getKey());
 
//System.out.println(json);
 
 
//String indented = (new JSONObject(entry.getValue())).toString(4);
//System.out.println(indented);
 
//System.out.println("Test Json Format End ");
 
jcomp5.append("\n"+"Keys:"+"\n"+entry.getKey()+"\n"+"Values:"+"\n"+entry.getValue()+"\n"+"\n");
 
System.out.println("welcome2");
 
DateFormat df1 = new SimpleDateFormat("dd_MM_HH_mm_ss");
      Date dateobj = new Date();
      String df2=df1.format(dateobj);
      System.out.println(df2);
  HSSFWorkbook  workbook = new HSSFWorkbook ();
       
       // Create a blank sheet
       HSSFSheet string1 = workbook.createSheet("HASH_"+df2+"");
       System.out.println(string1);
       Row titleRow = string1.createRow( 0 );
       System.out.println("Row ends");
       System.out.println("cel; begins");
       Cell titleCell = titleRow.createCell( 0 );
       System.out.println("Row ends");
       System.out.println("Row getsvalue");
       titleCell.setCellValue(jcomp3.getText());
       System.out.println("Row endvalues");
       Row r;
       Cell c;
       int i = 1;
       r = string1.createRow( i );
           c = r.createCell( 0 );
           c.setCellValue(entry.getKey( ) + ": " + entry.getValue( ) );
           i++;
        
           // this Writes the workbook gfgcontribute
            String username = System.getProperty("user.name");
            System.out.println(username);
               FileOutputStream out = new FileOutputStream(new File("C:/Users/"+username+"/Desktop/Tests.xls"));
               workbook.write(out);
               System.out.println(out);
               //out.close();
               System.out.println("Tests .xls written successfully on v8devap10 env.");
               
 
jedis.close();
 
}
 
System.out.println("Final");
}
 
else if(s.equals(true) && HashType.equals("set"))
{
 
 Long TTL=jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties");
 TimeZone tz = TimeZone.getTimeZone("UTC");
 SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
 df.setTimeZone(tz);
 String time = df.format(new Date(TTL*1000L));
 System.out.println("TTL in hrs:"+time);
 //System.out.println("TTL Value:"+jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties"));
 jcomp5.append("\n"+"Hash Name:"+"\n"+jcomp3.getText()+"\n"+"TTL in hrs:"+"\n"+time+"\n");
 
 System.out.println("Languages: " + jedis.smembers("cacheKey:2017-09-30:wwf:fleet"));
 
System.out.println("welcome1");
 
 
 
Set<String> response=jedis.smembers(jcomp3.getText());
for (String o: response)
{
 
System.out.println(o);
jcomp5.append("\n"+"Values:"+"\n"+o+"\n");
 
System.out.println("welcome2");
 
DateFormat df1 = new SimpleDateFormat("dd_MM_HH_mm_ss");
      Date dateobj = new Date();
      String df2=df1.format(dateobj);
      System.out.println(df2);
  
  

HSSFWorkbook  workbooksa = new HSSFWorkbook ();
       // Create a blank sheet
       HSSFSheet string1 = workbooksa.createSheet("SET_"+df2+"");
       string1.setActive(true);
       System.out.println(string1);
       Row titleRow = string1.createRow( 0 );
       System.out.println("Row ends");
       System.out.println("cel; begins");
       Cell titleCell = titleRow.createCell( 0 );
       System.out.println("Row ends");
       System.out.println("Row getsvalue");
       titleCell.setCellValue(jcomp3.getText());
       System.out.println("Row endvalues");
       Row r;
       Cell c;
       int i = 1;
       r = string1.createRow( i );
           c = r.createCell( 0 );
           c.setCellValue( o );
           i++;
           // this Writes the workbook set
            String username = System.getProperty("user.name");
            System.out.println(username);
               FileOutputStream outsa = new FileOutputStream(new File("C:/Users/"+username+"/Desktop/Tests.xls"));
               workbooksa.write(outsa);
               System.out.println(outsa);
               //outsa.close();
               System.out.println("Tests .xls written successfully on v8devap10 env.");
               jedis.close();
 
 
}
 
System.out.println("Final");
}
 
else if(s.equals(true) && HashType.equals("string"))
{
 
 Long TTL=jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties");
 TimeZone tz = TimeZone.getTimeZone("UTC");
 SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
 df.setTimeZone(tz);
 String time = df.format(new Date(TTL*1000L));
 System.out.println("TTL in hrs:"+time);
 //System.out.println("TTL Value:"+jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties"));
 jcomp5.append("\n"+"Hash Name:"+"\n"+jcomp3.getText()+"\n"+"TTL in hrs:"+"\n"+time+"\n");
 
 System.out.println("Languages: " + jedis.smembers("cacheKey:2017-09-30:wwf:fleet"));
 
 System.out.println("welcome1");
 
 String response=jedis.get(jcomp3.getText());
 
 System.out.println(response);
 jcomp5.append("\n"+"Values:"+"\n"+response+"\n");
 
 System.out.println("welcome2");
 
 
 
 
  System.out.println("Final");
  DateFormat df1 = new SimpleDateFormat("dd_MM_HH_mm_ss");
      Date dateobj = new Date();
      String df2=df1.format(dateobj);
      System.out.println(df2);
  
HSSFWorkbook  workbooksa = new HSSFWorkbook ();
       // Create a blank sheet
       HSSFSheet string2 = workbooksa.createSheet("String_"+df2+"");
                   
       System.out.println(string2);
       Row titleRow = string2.createRow( 0 );
       System.out.println("Row ends");
       System.out.println("cel; begins");
       Cell titleCell = titleRow.createCell( 0 );
       System.out.println("Row ends");
       System.out.println("Row getsvalue");
       titleCell.setCellValue(jcomp3.getText());
       System.out.println("Row endvalues");
       Row r;
       Cell c;
       int i = 1;
       r = string2.createRow( i );
           c = r.createCell( 0 );
           c.setCellValue( response );
           i++;
           // this Writes the workbook gfgcontribute
            String username = System.getProperty("user.name");
            System.out.println(username);
               FileOutputStream outth = new FileOutputStream(new File("C:/Users/"+username+"/Desktop/Tests.xls"));
               workbooksa.write(outth);
               System.out.println(outth);
               outth.close();
               System.out.println("Tests String.xls written successfully on v8devap10 env.");
               jedis.close();
}
 
else if(s.equals(true) && HashType.equals("zset"))
{
 
 Long TTL=jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties");
 TimeZone tz = TimeZone.getTimeZone("UTC");
 SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
 df.setTimeZone(tz);
 String time = df.format(new Date(TTL*1000L));
 System.out.println("TTL in hrs:"+time);
 //System.out.println("TTL Value:"+jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties"));
 jcomp5.append("\n"+"Hash Name:"+"\n"+jcomp3.getText()+"\n"+"TTL in hrs:"+"\n"+time+"\n");
 
 System.out.println("Zset length: " + jedis.zcard(jcomp3.getText()));
 Long zset=jedis.zcard(jcomp3.getText());
 System.out.println("ZRANGE: " +zset);
 
 String keya = jcomp3.getText();
 
 System.out.println(jedis.zrevrange(keya, 0, -1));
 
 System.out.println("welcome2");
 
 Set<Tuple> elements = jedis.zrevrangeWithScores(keya, 0, -1);
 for(Tuple tuple: elements){
 System.out.printf("Value:"+tuple.getElement() + "-" + "Score: %.0f\n",tuple.getScore());
 
 jcomp5.append("\n"+"Value:"+"\n"+tuple.getElement() + "\n"+"Score" + "\n"+String.format("%.0f", tuple.getScore())+"\n");
 
 DateFormat df1 = new SimpleDateFormat("dd_MM_HH_mm_ss");
      Date dateobj = new Date();
      String df2=df1.format(dateobj);
      System.out.println(df2);
  HSSFWorkbook  workbook = new HSSFWorkbook ();
       
       // Create a blank sheet
       HSSFSheet string1 = workbook.createSheet("ZSET_"+df2+"");
       System.out.println(string1);
       Row titleRow = string1.createRow( 0 );
       System.out.println("Row ends");
       System.out.println("cel; begins");
       Cell titleCell = titleRow.createCell( 0 );
       System.out.println("Row ends");
       System.out.println("Row getsvalue");
       titleCell.setCellValue(jcomp3.getText());
       System.out.println("Row endvalues");
       Row r;
       Cell c;
       int i = 1;
       r = string1.createRow( i );
           c = r.createCell( 0 );
           c.setCellValue(tuple.getElement() +":"+String.format("%.0f", tuple.getScore())+"\n");
           i++;
        
           // this Writes the workbook gfgcontribute
            String username = System.getProperty("user.name");
            System.out.println(username);
               FileOutputStream out = new FileOutputStream(new File("C:/Users/"+username+"/Desktop/Tests.xls"));
               workbook.write(out);
               System.out.println(out);
               //out.close();
               System.out.println("Tests .xls written successfully on v8devap10 env.");
             
 
 }
 
 jedis.close();
 
 
System.out.println("Final");
}
 
else if(s.equals(false))
 
{
JOptionPane.showMessageDialog(null, "Your hash is invalid please check once");
jedis.close();
}
 
else
{
JOptionPane.showMessageDialog(null, "Enter Valind Input");
jedis.close();
}
}
 
catch(Exception ep)
 
{
 
//JOptionPane.showMessageDialog(null, "Unable to handle this case. Check your operations");
 
}
}
 
else if(e.getSource() == jcomp4 && jcomp7.getSelectedIndex() == 1 && jcomp3.getText()!= null && jcomp3.getText().length()  >0) 
{
 
 
try 
{
 
Jedis jedis = new Jedis("host", port);
jedis.auth("opsredis");
System.out.println("Connected to Redis1");
jedis.select(1);
 
Boolean s =jedis.exists(jcomp3.getText());
System.out.println(s);
String HashType=jedis.type(jcomp3.getText());
System.out.println(HashType);
if(s.equals(true) && HashType.equals("hash"))
{
 
 Long TTL=jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties");
 TimeZone tz = TimeZone.getTimeZone("UTC");
 SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
 df.setTimeZone(tz);
 String time = df.format(new Date(TTL*1000L));
 System.out.println("TTL in hrs:"+time);
 //System.out.println("TTL Value:"+jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties"));
 
jcomp5.append("\n"+"Hash Name:"+"\n"+jcomp3.getText()+"\n"+"TTL in hrs:"+"\n"+time+"\n");
Map<String, String> response=jedis.hgetAll(jcomp3.getText());
for (Entry<String, String> entry : response.entrySet())
{
//System.out.println("Test Json Format Start ");
//String Test=entry.getValue();
 
//JSONObject json = (JSONObject) JSONSerializer.toJSON(entry.getKey());
 
//System.out.println(json);
 
 
//String indented = (new JSONObject(entry.getValue())).toString(4);
//System.out.println(indented);
 
//System.out.println("Test Json Format End ");
 
jcomp5.append("\n"+"Keys:"+"\n"+entry.getKey()+"\n"+"Values:"+"\n"+entry.getValue()+"\n"+"\n");
 
jedis.close();
 
}
 
System.out.println("Final");
}
 
else if(s.equals(true) && HashType.equals("set"))
{
 
 Long TTL=jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties");
 TimeZone tz = TimeZone.getTimeZone("UTC");
 SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
 df.setTimeZone(tz);
 String time = df.format(new Date(TTL*1000L));
 System.out.println("TTL in hrs:"+time);
 //System.out.println("TTL Value:"+jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties"));
 jcomp5.append("\n"+"Hash Name:"+"\n"+jcomp3.getText()+"\n"+"TTL in hrs:"+"\n"+time+"\n");
 
 System.out.println("Languages: " + jedis.smembers("cacheKey:2017-09-30:wwf:fleet"));
 
System.out.println("welcome1");
 
 
 
Set<String> response=jedis.smembers(jcomp3.getText());
for (String o: response)
{
 
System.out.println(o);
jcomp5.append("\n"+"Values:"+"\n"+o+"\n");
 
System.out.println("welcome2");
 
jedis.close();
}
 
System.out.println("Final");
}
 
else if(s.equals(true) && HashType.equals("string"))
{
 
 Long TTL=jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties");
 TimeZone tz = TimeZone.getTimeZone("UTC");
 SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
 df.setTimeZone(tz);
 String time = df.format(new Date(TTL*1000L));
 System.out.println("TTL in hrs:"+time);
 //System.out.println("TTL Value:"+jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties"));
 jcomp5.append("\n"+"Hash Name:"+"\n"+jcomp3.getText()+"\n"+"TTL in hrs:"+"\n"+time+"\n");
 
 System.out.println("Languages: " + jedis.smembers("cacheKey:2017-09-30:wwf:fleet"));
 
 System.out.println("welcome1");
 
 String response=jedis.get(jcomp3.getText());
 
 System.out.println(response);
 jcomp5.append("\n"+"Values:"+"\n"+response+"\n");
 
 System.out.println("welcome2");
 
 jedis.close();
 
 
System.out.println("Final");
}
 
else if(s.equals(true) && HashType.equals("zset"))
{
 
 Long TTL=jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties");
 TimeZone tz = TimeZone.getTimeZone("UTC");
 SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
 df.setTimeZone(tz);
 String time = df.format(new Date(TTL*1000L));
 System.out.println("TTL in hrs:"+time);
 //System.out.println("TTL Value:"+jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties"));
 jcomp5.append("\n"+"Hash Name:"+"\n"+jcomp3.getText()+"\n"+"TTL in hrs:"+"\n"+time+"\n");
 
 System.out.println("Zset length: " + jedis.zcard(jcomp3.getText()));
 Long zset=jedis.zcard(jcomp3.getText());
 System.out.println("ZRANGE: " +zset);
 
 String keya = jcomp3.getText();
 
 System.out.println(jedis.zrevrange(keya, 0, -1));
 
 System.out.println("welcome2");
 
 Set<Tuple> elements = jedis.zrevrangeWithScores(keya, 0, -1);
 for(Tuple tuple: elements){
 System.out.printf("Value:"+tuple.getElement() + "-" + "Score: %.0f\n",tuple.getScore());
 
 jcomp5.append("\n"+"Value:"+"\n"+tuple.getElement() + "\n"+"Score" + "\n"+String.format("%.0f", tuple.getScore())+"\n");
 
 }
 
 jedis.close();
 
 
System.out.println("Final");
}
 
else if(s.equals(false))
 
{
JOptionPane.showMessageDialog(null, "Your hash is invalid please check once");
jedis.close();
}
 
else
{
JOptionPane.showMessageDialog(null, "Enter Valind Input");
jedis.close();
}
}
 
catch(Exception ep)
 
{
 
//JOptionPane.showMessageDialog(null, "Unable to handle this case. Check your operations");
 
}
}
 
else if(e.getSource() == jcomp4 && jcomp7.getSelectedIndex() == 2 && jcomp3.getText()!= null && jcomp3.getText().length()  >0) 
{
 
 
try 
{
 
Jedis jedis = new Jedis("host", port);
jedis.auth("opsredis");
System.out.println("Connected to Redis3");
jedis.select(1);
 
Boolean s =jedis.exists(jcomp3.getText());
System.out.println(s);
String HashType=jedis.type(jcomp3.getText());
System.out.println(HashType);
if(s.equals(true) && HashType.equals("hash"))
{
 
 Long TTL=jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties");
 TimeZone tz = TimeZone.getTimeZone("UTC");
 SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
 df.setTimeZone(tz);
 String time = df.format(new Date(TTL*1000L));
 System.out.println("TTL in hrs:"+time);
 //System.out.println("TTL Value:"+jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties"));
 jcomp5.append("\n"+"Hash Name:"+"\n"+jcomp3.getText()+"\n"+"TTL in hrs:"+"\n"+time+"\n");
 
Map<String, String> response=jedis.hgetAll(jcomp3.getText());
for (Entry<String, String> entry : response.entrySet())
{
//System.out.println("Test Json Format Start ");
//String Test=entry.getValue();
 
//JSONObject json = (JSONObject) JSONSerializer.toJSON(entry.getKey());
 
//System.out.println(json);
 
 
//String indented = (new JSONObject(entry.getValue())).toString(4);
//System.out.println(indented);
 
//System.out.println("Test Json Format End ");
 
jcomp5.append("\n"+"Keys:"+"\n"+entry.getKey()+"\n"+"Values:"+"\n"+entry.getValue()+"\n"+"\n");
 
jedis.close();
 
}
 
System.out.println("Final");
}
else if(s.equals(true) && HashType.equals("set"))
{
 
 Long TTL=jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties");
 TimeZone tz = TimeZone.getTimeZone("UTC");
 SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
 df.setTimeZone(tz);
 String time = df.format(new Date(TTL*1000L));
 System.out.println("TTL in hrs:"+time);
 //System.out.println("TTL Value:"+jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties"));
 jcomp5.append("\n"+"Hash Name:"+"\n"+jcomp3.getText()+"\n"+"TTL in hrs:"+"\n"+time+"\n");
 
 System.out.println("Languages: " + jedis.smembers("cacheKey:2017-09-30:wwf:fleet"));
 
System.out.println("welcome1");
 
 
 
Set<String> response=jedis.smembers(jcomp3.getText());
for (String o: response)
{
 
System.out.println(o);
jcomp5.append("\n"+"Values:"+"\n"+o+"\n");
 
System.out.println("welcome2");
 
jedis.close();
}
 
System.out.println("Final");
}
 
else if(s.equals(true) && HashType.equals("string"))
{
 
 Long TTL=jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties");
 TimeZone tz = TimeZone.getTimeZone("UTC");
 SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
 df.setTimeZone(tz);
 String time = df.format(new Date(TTL*1000L));
 System.out.println("TTL in hrs:"+time);
 //System.out.println("TTL Value:"+jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties"));
 jcomp5.append("\n"+"Hash Name:"+"\n"+jcomp3.getText()+"\n"+"TTL in hrs:"+"\n"+time+"\n");
 
 System.out.println("Languages: " + jedis.smembers("cacheKey:2017-09-30:wwf:fleet"));
 
 System.out.println("welcome1");
 
 String response=jedis.get(jcomp3.getText());
 
 System.out.println(response);
 jcomp5.append("\n"+"Values:"+"\n"+response+"\n");
 
 System.out.println("welcome2");
 
 jedis.close();
 
 
System.out.println("Final");
}
 
else if(s.equals(true) && HashType.equals("zset"))
{
 
 Long TTL=jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties");
 TimeZone tz = TimeZone.getTimeZone("UTC");
 SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
 df.setTimeZone(tz);
 String time = df.format(new Date(TTL*1000L));
 System.out.println("TTL in hrs:"+time);
 //System.out.println("TTL Value:"+jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties"));
 jcomp5.append("\n"+"Hash Name:"+"\n"+jcomp3.getText()+"\n"+"TTL in hrs:"+"\n"+time+"\n");
 
 System.out.println("Zset length: " + jedis.zcard(jcomp3.getText()));
 Long zset=jedis.zcard(jcomp3.getText());
 System.out.println("ZRANGE: " +zset);
 
 String keya = jcomp3.getText();
 
 System.out.println(jedis.zrevrange(keya, 0, -1));
 
 System.out.println("welcome2");
 
 Set<Tuple> elements = jedis.zrevrangeWithScores(keya, 0, -1);
 for(Tuple tuple: elements){
 System.out.printf("Value:"+tuple.getElement() + "-" + "Score: %.0f\n",tuple.getScore());
 
 jcomp5.append("\n"+"Value:"+"\n"+tuple.getElement() + "\n"+"Score" + "\n"+String.format("%.0f", tuple.getScore())+"\n");
 
 }
 
 jedis.close();
 
 
System.out.println("Final");
}
 
else if(s.equals(false))
 
{
JOptionPane.showMessageDialog(null, "Your hash is invalid please check once");
jedis.close();
}
 
else
{
JOptionPane.showMessageDialog(null, "Enter Valind Input");
jedis.close();
}
}
 
catch(Exception ep)
 
{
 
//JOptionPane.showMessageDialog(null, "Unable to handle this case. Check your operations");
 
}
}
 
else if(e.getSource() == jcomp4 && jcomp7.getSelectedIndex() == 2 && jcomp3.getText()!= null && jcomp3.getText().length()  >0) 
{
 
 
try 
{
 
Jedis jedis = new Jedis("host", port);
jedis.auth("opsredis");
System.out.println("Connected to Redis3");
jedis.select(1);
 
Boolean s =jedis.exists(jcomp3.getText());
System.out.println(s);
String HashType=jedis.type(jcomp3.getText());
System.out.println(HashType);
if(s.equals(true) && HashType.equals("hash"))
{
 
 Long TTL=jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties");
 TimeZone tz = TimeZone.getTimeZone("UTC");
 SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
 df.setTimeZone(tz);
 String time = df.format(new Date(TTL*1000L));
 System.out.println("TTL in hrs:"+time);
 //System.out.println("TTL Value:"+jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties"));
 jcomp5.append("\n"+"Hash Name:"+"\n"+jcomp3.getText()+"\n"+"TTL in hrs:"+"\n"+time+"\n");
 
Map<String, String> response=jedis.hgetAll(jcomp3.getText());
for (Entry<String, String> entry : response.entrySet())
{
//System.out.println("Test Json Format Start ");
//String Test=entry.getValue();
 
//JSONObject json = (JSONObject) JSONSerializer.toJSON(entry.getKey());
 
//System.out.println(json);
 
 
//String indented = (new JSONObject(entry.getValue())).toString(4);
//System.out.println(indented);
 
//System.out.println("Test Json Format End ");
 
jcomp5.append("\n"+"Keys:"+"\n"+entry.getKey()+"\n"+"Values:"+"\n"+entry.getValue()+"\n"+"\n");
 
jedis.close();
 
}
 
System.out.println("Final");
}
else if(s.equals(true) && HashType.equals("set"))
{
 
 Long TTL=jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties");
 TimeZone tz = TimeZone.getTimeZone("UTC");
 SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
 df.setTimeZone(tz);
 String time = df.format(new Date(TTL*1000L));
 System.out.println("TTL in hrs:"+time);
 //System.out.println("TTL Value:"+jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties"));
 jcomp5.append("\n"+"Hash Name:"+"\n"+jcomp3.getText()+"\n"+"TTL in hrs:"+"\n"+time+"\n");
 
 System.out.println("Languages: " + jedis.smembers("cacheKey:2017-09-30:wwf:fleet"));
 
System.out.println("welcome1");
 
 
 
Set<String> response=jedis.smembers(jcomp3.getText());
for (String o: response)
{
 
System.out.println(o);
jcomp5.append("\n"+"Values:"+"\n"+o+"\n");
 
System.out.println("welcome2");
 
jedis.close();
}
 
System.out.println("Final");
}
 
else if(s.equals(true) && HashType.equals("string"))
{
 
 Long TTL=jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties");
 TimeZone tz = TimeZone.getTimeZone("UTC");
 SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
 df.setTimeZone(tz);
 String time = df.format(new Date(TTL*1000L));
 System.out.println("TTL in hrs:"+time);
 //System.out.println("TTL Value:"+jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties"));
 jcomp5.append("\n"+"Hash Name:"+"\n"+jcomp3.getText()+"\n"+"TTL in hrs:"+"\n"+time+"\n");
 
 System.out.println("Languages: " + jedis.smembers("cacheKey:2017-09-30:wwf:fleet"));
 
 System.out.println("welcome1");
 
 String response=jedis.get(jcomp3.getText());
 
 System.out.println(response);
 jcomp5.append("\n"+"Values:"+"\n"+response+"\n");
 
 System.out.println("welcome2");
 
 jedis.close();
 
 
System.out.println("Final");
}
 
else if(s.equals(true) && HashType.equals("zset"))
{
 
 Long TTL=jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties");
 TimeZone tz = TimeZone.getTimeZone("UTC");
 SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
 df.setTimeZone(tz);
 String time = df.format(new Date(TTL*1000L));
 System.out.println("TTL in hrs:"+time);
 //System.out.println("TTL Value:"+jedis.ttl("crewstandbys:cabincrew:LHR:2017-08-18:wwf:duties"));
 jcomp5.append("\n"+"Hash Name:"+"\n"+jcomp3.getText()+"\n"+"TTL in hrs:"+"\n"+time+"\n");
 
 System.out.println("Zset length: " + jedis.zcard(jcomp3.getText()));
 Long zset=jedis.zcard(jcomp3.getText());
 System.out.println("ZRANGE: " +zset);
 
 String keya = jcomp3.getText();
 
 System.out.println(jedis.zrevrange(keya, 0, -1));
 
 System.out.println("welcome2");
 
 Set<Tuple> elements = jedis.zrevrangeWithScores(keya, 0, -1);
 for(Tuple tuple: elements){
 System.out.printf("Value:"+tuple.getElement() + "-" + "Score: %.0f\n",tuple.getScore());
 
 jcomp5.append("\n"+"Value:"+"\n"+tuple.getElement() + "\n"+"Score" + "\n"+String.format("%.0f", tuple.getScore())+"\n");
 
 }
 
 jedis.close();
 
 
System.out.println("Final");
}
 
else if(s.equals(false))
 
{
JOptionPane.showMessageDialog(null, "Your hash is invalid please check once");
jedis.close();
}
 
else
{
JOptionPane.showMessageDialog(null, "Enter Valind Input");
jedis.close();
}
}
 
catch(Exception ep)
 
{
 
//JOptionPane.showMessageDialog(null, "Unable to handle this case. Check your operations");
 
}
}
 
else if(e.getSource() == jcomp9 && jcomp3.getText()!= null && jcomp3.getText().length()  >0) 
{
if(e.getSource() == jcomp9 && jcomp3.getText()!= null && jcomp3.getText().length()  >0 && jcomp7.getSelectedIndex() == 0)
{
 
 

try
 
{
 
Jedis jedis = new Jedis("host", port);
jedis.auth("opsredis");
System.out.println("Connected to Redis1");
jedis.select(1);
 
Boolean s =jedis.exists(jcomp3.getText());
System.out.println(s);
 
if(s.equals(true))
{
 
System.out.println(jcomp3.getText());   
jedis.del(jcomp3.getText());
String username = System.getenv("USERNAME");
jcomp5.append("\n"+"Redis Hash Deleted:"+" "+jcomp3.getText()+" "+"By"+" "+username);
System.out.println("exit");
}
 
 
else
{
 
JOptionPane.showMessageDialog(null, "Invalid Input Check Your Hash Exist");
}
   
 
   
jedis.close();
     
 
 
}
 
catch(Exception ep)
 
{
 
//JOptionPane.showMessageDialog(null, "Unable to handle this case. Check your operations");
 
}
}
 
else if(e.getSource() == jcomp9 && jcomp3.getText()!= null && jcomp3.getText().length()  >0 && jcomp7.getSelectedIndex() == 1)
{
 
 

try
 
{
 
Jedis jedis = new Jedis("host", port);
jedis.auth("opsredis");
System.out.println("Connected to Redis1");
jedis.select(1);
 
Boolean s =jedis.exists(jcomp3.getText());
System.out.println(s);
 
if(s.equals(true))
{
 
System.out.println(jcomp3.getText());   
jedis.del(jcomp3.getText());
String username = System.getenv("USERNAME");
jcomp5.append("\n"+"Redis Hash Deleted:"+" "+jcomp3.getText()+" "+"By"+" "+username);
System.out.println("exit");
}
 
 
else
{
 
JOptionPane.showMessageDialog(null, "Invalid Input Check Your Hash Exist");
}
   
 
   
jedis.close();
     
 
 
}
 
catch(Exception ep)
 
{
 
//JOptionPane.showMessageDialog(null, "Unable to handle this case. Check your operations");
 
}
}
 
if(e.getSource() == jcomp9 && jcomp3.getText()!= null && jcomp3.getText().length()  >0 && jcomp7.getSelectedIndex() == 2)
{
 
 

try
 
{
 
Jedis jedis = new Jedis("host", port);
jedis.auth("opsredis");
System.out.println("Connected to Redis1");
jedis.select(1);
 
Boolean s =jedis.exists(jcomp3.getText());
System.out.println(s);
 
if(s.equals(true))
{
 
System.out.println(jcomp3.getText());   
jedis.del(jcomp3.getText());
String username = System.getenv("USERNAME");
jcomp5.append("\n"+"Redis Hash Deleted:"+" "+jcomp3.getText()+" "+"By"+" "+username);
System.out.println("exit");
}
 
 
else
{
 
JOptionPane.showMessageDialog(null, "Invalid Input Check Your Hash Exist");
}
   
 
   
jedis.close();
     
 
 
}
 
catch(Exception ep)
 
{
 
//JOptionPane.showMessageDialog(null, "Unable to handle this case. Check your operations");
 
}
}
 
if(e.getSource() == jcomp9 && jcomp3.getText()!= null && jcomp3.getText().length()  >0 && jcomp7.getSelectedIndex() == 3)
{
 
 

try
 
{
 
Jedis jedis = new Jedis("host", port);
jedis.auth("opsredis");
System.out.println("Connected to Redis1");
jedis.select(1);
 
Boolean s =jedis.exists(jcomp3.getText());
System.out.println(s);
 
if(s.equals(true))
{
 
System.out.println(jcomp3.getText());   
jedis.del(jcomp3.getText());
String username = System.getenv("USERNAME");
jcomp5.append("\n"+"Redis Hash Deleted:"+" "+jcomp3.getText()+" "+"By"+" "+username);
System.out.println("exit");
}
 
 
else
{
 
JOptionPane.showMessageDialog(null, "Invalid Input Check Your Hash Exist");
}
   
 
   
jedis.close();
     
 
 
}
 
catch(Exception ep)
 
{
 
//JOptionPane.showMessageDialog(null, "Unable to handle this case. Check your operations");
 
}
}
}
 
 
else if(e.getSource() == jcomp8) 
 
 
{
 
jcomp3.setText("");
jcomp5.setText("");
}
 
else if(e.getSource() == jcomp9) 
 
 
{
 
jcomp3.setText("");
jcomp5.setText("");
}
 
else  
 
 
{
//JOptionPane.showMessageDialog(null, "Unable to handle this case. Check your operations");

}
 
}
 
catch(Exception ep)
 
{
 
//JOptionPane.showMessageDialog(null, "Unable to handle this case. Check your operations");
 
}
 
}


public static void main (String[] args) {
        JFrame frame = new JFrame ("Redis DB Export");
     
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add (new RedisDBExport());
        frame.pack();
        frame.setVisible (true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        
}
}