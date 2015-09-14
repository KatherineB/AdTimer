/* AdTimer:
 * This program is for determining what percentage of the time in
 * a TV show or movie is advertising.  The user clicks on "Show" or
 * "Ads" to have the timer ticking for either show time or ads time.
 */

import java.awt.*; 
import javax.swing.*;
import java.awt.event.*; 
import javax.swing.JOptionPane;
import java.util.Date;
import javax.swing.event.*;
import javax.swing.JTextField;
import java.util.Timer;
import java.util.*;
import java.math.BigDecimal;

class AdTimer extends JFrame implements ActionListener
{
  JLabel thisShowSegment = new JLabel("This Show Segment:");
  JLabel thisAdsSegment = new JLabel("This Ads Segment:");
  JFormattedTextField show_segment = new JFormattedTextField();
  JFormattedTextField ads_segment = new JFormattedTextField();
  JLabel totalForShow = new JLabel("Show Time Total:");
  JLabel totalForAds = new JLabel("Ads Time Total:");
  JFormattedTextField show_time = new JFormattedTextField();
  JFormattedTextField ads_time = new JFormattedTextField();
  JLabel totalViewTime = new JLabel("Viewing Time Total:");
  JLabel percentageAds = new JLabel("Percentage Ads:");
  JFormattedTextField view_time = new JFormattedTextField();
  JFormattedTextField ads_percent = new JFormattedTextField();
 
  JButton show = new JButton("Show");
  JButton ads = new JButton("Ads");
  JButton stop = new JButton("Stop");
  JButton calculate = new JButton("Calculate");
  JButton reset = new JButton("Reset");
  JButton exit = new JButton("Exit");
  
  JPanel showPanel = new JPanel();
  JPanel adsPanel = new JPanel();
  JPanel stopPanel = new JPanel();
  JPanel calculatePanel = new JPanel();
  JPanel resetPanel = new JPanel();
  JPanel exitPanel = new JPanel();
  JFrame exitWarning;
  int exitChoice;
  Object[] options = {"Yes","No"};
  long showStartTime = 0;
  long adsStartTime = 0;
  long showStopTime = 0;
  long adsStopTime = 0;
  long diffShowStop = 0;
  long diffAdsStop = 0;
  int diffShowStopInt = 0;
  int diffAdsStopInt = 0;
  long diffShow = 0;
  long diffAds = 0;
  int diffShowInt = 0;
  int diffAdsInt = 0;
  int showTotalTime = 0;
  int adsTotalTime = 0;
  int viewTotal = 0;
  int percentAds = 0;
  double percentAdsDouble = 0.0;
  double percentAdsDoubleRounded = 0.0;
  boolean showTimerOn = false;
  boolean adsTimerOn = false;
  boolean enableTimer = true;
  Timer timer = new Timer();
  long timeNow = 0;
  int currentTotalShowTime = 0;
  int currentTotalAdsTime = 0;
  
  AdTimer()
     {
      setTitle("AdTimer");
      setResizable(false);
      setLayout(new GridLayout(9,2));
 
      show_time.setText("00:00:00");
      ads_time.setText("00:00:00");
      show_segment.setText("00:00:00");
      ads_segment.setText("00:00:00");

      show_segment.setBackground(new Color(200,220,250));
      show_time.setBackground(new Color(200,220,250));
      ads_segment.setBackground(new Color(200,220,250));
      ads_time.setBackground(new Color(200,220,250));

      // These statements prevent the user from changing the FormattedTextFields
      // that the times and calculated results are displayed in.
      show_segment.setFocusable(false);
      ads_segment.setFocusable(false);
      show_time.setFocusable(false);
      ads_time.setFocusable(false);
      view_time.setFocusable(false);
      ads_percent.setFocusable(false);

      show_segment.setHorizontalAlignment(JTextField.CENTER);
      ads_segment.setHorizontalAlignment(JTextField.CENTER);
      show_time.setHorizontalAlignment(JTextField.CENTER);
      ads_time.setHorizontalAlignment(JTextField.CENTER);
      view_time.setHorizontalAlignment(JTextField.CENTER);
      ads_percent.setHorizontalAlignment(JTextField.CENTER);

      show_segment.setFont(new Font("Serif", Font.BOLD, 15));
      ads_segment.setFont(new Font("Serif", Font.BOLD, 15));
      show_time.setFont(new Font("Serif", Font.BOLD, 15));
      ads_time.setFont(new Font("Serif", Font.BOLD, 15));
      view_time.setFont(new Font("Serif", Font.BOLD, 15));
      ads_percent.setFont(new Font("Serif", Font.BOLD, 15));
      
      show.addActionListener(this); 
      ads.addActionListener(this); 
      stop.addActionListener(this); 
      calculate.addActionListener(this);
      reset.addActionListener(this); 
      exit.addActionListener(this);

      show.setPreferredSize(new Dimension(100,30));
      ads.setPreferredSize(new Dimension(100,30));
      stop.setPreferredSize(new Dimension(100,30));
      calculate.setPreferredSize(new Dimension(100,30));
      reset.setPreferredSize(new Dimension(100,30));
      exit.setPreferredSize(new Dimension(100,30));

      show.setToolTipText("Starts timer for show.");
      ads.setToolTipText("Starts timer for ads.");
      stop.setToolTipText("Stops which ever timer is currently running.");
      calculate.setToolTipText("Calculates total time elapsed and what percentage was ads.");
      reset.setToolTipText("Erases all times and totals in preparation for starting over again.");


      add(thisShowSegment);
      add(thisAdsSegment);
      add(show_segment);
      add(ads_segment);
      add(totalForShow);
      add(totalForAds);
      add(show_time);
      add(ads_time);
      add(showPanel);
      add(adsPanel);
      add(totalViewTime);
      add(percentageAds);
      add(view_time);
      add(ads_percent);
      add(stopPanel);
      add(calculatePanel);
      add(resetPanel);
      add(exitPanel);

      showPanel.add(show);
      adsPanel.add(ads);
      stopPanel.add(stop);
      calculatePanel.add(calculate);
      resetPanel.add(reset);
      exitPanel.add(exit);

      setSize(300,400); 
      setVisible(true);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

     // This is the timer that handles the updating of the times on the
     // screen while time is passing for either show time or ads time.
      timer.scheduleAtFixedRate(new TimerTask() 
        {
         public void run() 
           {        
            if (showTimerOn || adsTimerOn)
              {       
               Date timeLine = new Date();
               timeNow = timeLine.getTime();
               updateScreen();
              }
           }
        }, 0, 500);

     }
     // Code for Buttons.
     public void actionPerformed (ActionEvent ae)
     { 
      if (ae.getSource( ).equals(show))
         { 
          if (enableTimer == true)
            {
             showTimerOn = true;
             adsTimerOn = false;
       
             ads_segment.setBackground(new Color(200,220,250));
             ads_time.setBackground(new Color(200,220,250));
             show_segment.setBackground(new Color(250,190,230));
             show_time.setBackground(new Color(250,190,230));
         
             Date startTime = new Date();     
             showStartTime = startTime.getTime(); 
        
             // This updates the total time for ads by adding the
             // just completed ads time segment to the total
             if (adsStartTime != 0)
               diffAds = showStartTime - adsStartTime;
             diffAdsInt = (int)(diffAds/1000);       
             adsTotalTime = adsTotalTime + diffAdsInt;
            }            
         } 

      if (ae.getSource( ).equals(ads))
         { 
          if (enableTimer == true)
            {
             showTimerOn = false;
             adsTimerOn = true;
     
             ads_segment.setBackground(new Color(250,190,230));
             ads_time.setBackground(new Color(250,190,230));
             show_segment.setBackground(new Color(200,220,250));
             show_time.setBackground(new Color(200,220,250));
           
             Date startTime = new Date();     
             adsStartTime = startTime.getTime();
     
             // This updates the total time for show by adding the
             // just completed show time segment to the total
             if (showStartTime != 0)
               diffShow = adsStartTime - showStartTime;
             diffShowInt = (int)(diffShow/1000);        
             showTotalTime = showTotalTime + diffShowInt;
            }
         } 

       if (ae.getSource( ).equals(stop))
         {
          show_segment.setBackground(new Color(200,220,250));
          show_time.setBackground(new Color(200,220,250));
          ads_segment.setBackground(new Color(200,220,250));
          ads_time.setBackground(new Color(200,220,250));

          // These two ifs calculate any time difference between
          // the last start of a show or ads time segment and the
          // time that the stop button was clicked on.
          if(showTimerOn)
            {
             Date stopTime = new Date();     
             showStopTime = stopTime.getTime();
             diffShowStop = showStopTime - showStartTime;
             diffShowStopInt = (int)(diffShowStop/1000);
             showTotalTime = showTotalTime + diffShowStopInt;
             enableTimer = false;
            }
          if(adsTimerOn)
            {
             Date stopTime = new Date();     
             adsStopTime = stopTime.getTime();
             diffAdsStop = adsStopTime - adsStartTime;
             diffAdsStopInt = (int)(diffAdsStop/1000);
             adsTotalTime = adsTotalTime + diffAdsStopInt;
             enableTimer = false;
            }
          showTimerOn = false;
          adsTimerOn = false;
          
         }
       if (ae.getSource( ).equals(calculate))
         {
          if (showTimerOn == false && adsTimerOn == false )
            {
             viewTotal = currentTotalShowTime + currentTotalAdsTime;
             if (viewTotal != 0)
               {
                view_time.setText(convertSecondsToTime(viewTotal));
                percentAdsDouble = ((double)adsTotalTime * 100.0)/((double)viewTotal);
                BigDecimal percentAdsBD = new BigDecimal(Double.toString(percentAdsDouble));
                percentAdsBD = percentAdsBD.setScale(1, BigDecimal.ROUND_HALF_UP);
                percentAdsDoubleRounded = percentAdsBD.doubleValue(); 
                ads_percent.setText(Double.toString(percentAdsDoubleRounded) + " %");
               }         
            }
         }

       if (ae.getSource( ).equals(reset))
         {
          if (showTimerOn == false && adsTimerOn == false)
            {
             show_segment.setBackground(new Color(200,220,250));
             show_time.setBackground(new Color(200,220,250));
             ads_segment.setBackground(new Color(200,220,250));
             ads_time.setBackground(new Color(200,220,250));
             showTimerOn = false;
             adsTimerOn = false;
             showStartTime = 0;
             adsStartTime = 0;
             diffShow = 0;
             diffAds = 0;
      
             showTotalTime = 0;
             adsTotalTime = 0;
             viewTotal = 0;
             currentTotalShowTime = 0;
             currentTotalAdsTime = 0;
             show_time.setText("00:00:00");
             ads_time.setText("00:00:00");
             show_segment.setText("00:00:00");
             ads_segment.setText("00:00:00");
             view_time.setText(" ");
             ads_percent.setText(" ");
            }
          enableTimer = true;
         }   

       if (ae.getSource( ).equals(exit))
         {
          if (showTimerOn || adsTimerOn)
            {
             exitChoice = JOptionPane.showOptionDialog(exitWarning,"Timer is running. Do you really want to exit?", "Really Exit?",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
             if (exitChoice == JOptionPane.YES_OPTION)
               System.exit(0);
            }
          else
             System.exit(0);
         }

    }

    String convertSecondsToTime(int seconds)
          {
           int sec;
           int min;
           int hour;
           String hourS;
           String minS;
           String secS;
           hour = seconds/3600;
           min = (seconds - (hour * 3600))/60;
           sec = seconds - (hour * 3600) - (min * 60);
           hourS = Integer.toString(hour);
           if (hourS.length() < 2)
             hourS = "0" + hourS;
           minS = Integer.toString(min);
           if (minS.length() < 2)
             minS = "0" + minS;
           secS = Integer.toString(sec);
           if (secS.length() < 2)
             secS = "0" + secS;
           return hourS + ":" + minS + ":" + secS;
          }

    void updateScreen()
      {
       if(showTimerOn)
        {
         int currentShowTimeInt;
         long currentShowTime;
         
         currentShowTime = timeNow - showStartTime;
         currentShowTimeInt = (int)(currentShowTime/1000);
         show_segment.setText(convertSecondsToTime(currentShowTimeInt));
         currentTotalShowTime = showTotalTime + currentShowTimeInt;
         show_time.setText(convertSecondsToTime(currentTotalShowTime));
        }
       if(adsTimerOn)
        {
         int currentAdsTimeInt;
         long currentAdsTime;
         
         currentAdsTime = timeNow - adsStartTime;
         currentAdsTimeInt = (int)(currentAdsTime/1000);
         ads_segment.setText(convertSecondsToTime(currentAdsTimeInt));
         currentTotalAdsTime = adsTotalTime + currentAdsTimeInt;
         ads_time.setText(convertSecondsToTime(currentTotalAdsTime));
        }    
      }

  public static void main(String [] args)
   { 
     new AdTimer();  
   } 

}