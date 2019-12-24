import com.cyberbotics.webots.controller.*;
import java.io.File;

public class MainController {
  static Robot robot = new Robot();
  static int timeStep = (int) Math.round(robot.getBasicTimeStep());
  static int[] imageTop, imageBottom;
  //static double[] iu;
  static double[] acc;
  static boolean finish = false;
  /* --- rl1 red links 1 steht für Top --- */
  /* --- damit man die rechten pixel uunabhängig von von den linken prüfen kann --- */
  //RGB TopCamera links
  static int rl1 = 0;
  static int gl1 = 0;
  static int bl1 = 0;
  //RGB TopCamera rechts
  static int rr1 = 0;
  static int gr1 = 0;
  static int br1 = 0;
  //RGB BottomCamera links
  static int rl2 = 0;
  static int gl2 = 0;
  static int bl2 = 0;
  //RGB BottomCamera rechts
  static int rr2 = 0;
  static int gr2 = 0;
  static int br2 = 0;
  //TopCamera RGB eines bestimmten Bereiches
  //Alle werte des Array = 0; ---> loadMotionsFiles()
  static int[][] rgbTop = new int[100][3];
  static int[][] rgbBottom = new int[100][3];
  //Sackgassen zähler
  static int sackGasse = 0;
  
  static int r1 = 0;
  static int g1 = 0;
  static int b1 = 0; 
  //BottomCamera RGB eines bestimmten Bereiches
  static int r2 = 0;
  static int g2 = 0;
  static int b2 = 0;
  //Schrittzähler
  static int stepCounter = 0; 
  
  /*------Später werden sie in ein Array gepackt!-------*/
    
  //static InertialUnit inertialUnit;
  static Camera cameraTop, cameraBottom; //cameras
  static TouchSensor lfoot_lbumper, lfoot_rbumper;  // left foot bumpers
  static TouchSensor rfoot_lbumper, rfoot_rbumper;  // right foot bumpers
  static DistanceSensor[] sonar = {new DistanceSensor("Sonar/Left"), new DistanceSensor("Sonar/Right")}; // ultrasound sensors
  static Accelerometer accelerometer;
  static GPS gps; //gps
  static Gyro gyro; //gyro
  static LED led; //led
  
  // motion file handles
  static Motion forwards, turnLeft40, turnLeft60, turnRight40, turnRight60, turn180, turnHeadLeft, turnHeadRight, turnHeadForward, standToSit, sitToStand, handWave, standUpFromFront;
    
   static void startMotion(Motion motion){
      motion.play();
      do {
      robot.step(timeStep);
      } while(!motion.isOver());
   }
  

   static void findAndEnableDevices() {
    //Accelerometer
    accelerometer = new Accelerometer("accelerometer");
    accelerometer.enable(timeStep);
    // camera
    cameraTop = new Camera("CameraTop");
    cameraBottom = new Camera("CameraBottom");
    cameraTop.enable(4*timeStep);
    cameraBottom.enable(4*timeStep);
    //RGB Array füllen
    for (int i = 0; i < 100; i++){
      for (int j = 0; j < 3; j++){
        rgbTop[i][j] = 0;
        rgbBottom[i][j] = 0;
      }
    }
    // ultrasound sensors
    sonar[0].enable(timeStep);
    sonar[1].enable(timeStep);
   }
  
  static void loadMotionsFiles(){
    File file = new File(System.getProperty("user.dir"));
    file = (file.getParentFile().getParentFile());
    String pfad = file.getPath() + System.getProperty("file.separator") + "motions" + System.getProperty("file.separator");
    

    forwards = new Motion(pfad + "Forwards.motion");
    //gehen = new Motion(pfad + "Gehen50.motion");
    handWave = new Motion(pfad + "HandWave.motion");
    turnLeft40 = new Motion(pfad + "TurnLeft40.motion");
    turnRight40 = new Motion(pfad + "TurnRight40.motion");
    turn180 = new Motion(pfad + "TurnLeft180.motion");
    turnLeft60 = new Motion(pfad + "TurnLeft60.motion");
    turnRight60 = new Motion(pfad + "TurnRight60.motion");
    standToSit = new Motion(pfad + "StandToSit.motion");
    sitToStand = new Motion(pfad + "SitToStand.motion");
    //turnHeadLeft = new Motion(pfad + "TurnHeadLeft.motion");
    //turnHeadRight = new Motion(pfad + "TurnHeadRight.motion");
    //turnHeadForward = new Motion(pfad + "TurnHeadForward.motion");
    //turnLeft10 = new Motion(pfad + "TurnLeft10.motion");
    //turnRight10 = new Motion(pfad + "TurnRight10.motion");
  }
  
  // x gibt an wie viele Pixel von der linken seite du betrachten willst
  // x = 160 für Fullscreen
  static void leftRGBTop(int x){
    int[] image = cameraTop.getImage();
    int l1 = 0;
    int l2 = 1;
    int l3 = 160 - x;
    for (int i=0; i < image.length; i++){
      int pixel = image[i];
      //40 Pixel der Linken Seite 
      if (l1 == i){
        //System.out.println("redTopL1=" + l1);
        rl1 += Camera.pixelGetRed(pixel);
        gl1 += Camera.pixelGetGreen(pixel);
        bl1 += Camera.pixelGetBlue(pixel);
        l1++;
      }
      if (l1 > l2*160-l3){
        l1 = l2 * 160;
        l2++;
      }
    }
    
    rl1 /= 120*x;
    gl1 /= 120*x;
    bl1 /= 120*x;
    
    //System.out.println("TopCamera linke Seite Pixel: " + x);
    //System.out.println("red=" + rl1 + "\tgreen=" + gl1 + "\tblue="+ bl1);
  }
  
    // x gibt an wie viele Pixel von der rechte seite du betrachten willst
  static void rightRGBTop(int x){
    int[] image = cameraTop.getImage();
    int l1 = 159-x;
    int l2 = 1;
    int l3 = 160;
    for (int i=0; i < image.length; i++){
      int pixel = image[i];
      //40 Pixel der Linken Seite 
      if (l1 == i){
        //System.out.println("TopL1=" + l1);
        rr1 += Camera.pixelGetRed(pixel);
        gr1 += Camera.pixelGetGreen(pixel);
        br1 += Camera.pixelGetBlue(pixel);
        l1++;
      }
      if (l1 >= l2*l3){
        l1 = l2 * 160 + l3 - x;
        l2++;
      }
    }
    
    rr1 /= 120*x;
    gr1 /= 120*x;
    br1 /= 120*x;
    
    //System.out.println("TopCamera rechte Seite Pixel: " + x);
    //System.out.println("red=" + rr1 + "\tgreen=" + gr1 + "\tblue="+ br1);
  }
  
  // x gibt an wie viele Pixel du betrachten willst
  // x = 160 für Fullscreen
  static void leftRGBBottom(int x){
    int[] image = cameraBottom.getImage();
    int l1 = 0;
    int l2 = 1;
    int l3 = 160 - x;
    for (int i=0; i < image.length; i++){
      int pixel = image[i];
      //40 Pixel der Linken Seite 
      if (l1 == i){
        rl2 += Camera.pixelGetRed(pixel);
        gl2 += Camera.pixelGetGreen(pixel);
        bl2 += Camera.pixelGetBlue(pixel);
        l1++;
      }
      if (l1 > l2*160-l3){
        l1 = l2 * 160;
        l2++;
      }
    }
    
    rl2 /= 120*x;
    gl2 /= 120*x;
    bl2 /= 120*x;

    //System.out.println("BottomCamera linke Seite Pixel: " + x);
    //System.out.println("red=" + rl2 + "\tgreen=" + gl2 + "\tblue="+ bl2);
  }
  
    static void rightRGBBottom(int x){
    int[] image = cameraBottom.getImage();
    int l1 = 159-x;
    int l2 = 1;
    int l3 = 160;
    for (int i=0; i < image.length; i++){
      int pixel = image[i];
      //40 Pixel der Linken Seite 
      if (l1 == i){
        //System.out.println("TopL1=" + l1);
        rr2 += Camera.pixelGetRed(pixel);
        gr2 += Camera.pixelGetGreen(pixel);
        br2 += Camera.pixelGetBlue(pixel);
        l1++;
      }
      if (l1 >= l2*l3){
        l1 = l2 * 160 + l3 - x;
        l2++;
      }
    }
    
    rr2 /= 120*x;
    gr2 /= 120*x;
    br2 /= 120*x;
    
    //System.out.println("BottomCamera rechte Seite Pixel: " + x);
    //System.out.println("red=" + rr2 + "\tgreen=" + gr2 + "\tblue="+ br2);
  }
    /*
      Mit dieser Funktion lässt sich der durchschnittliche Farbwert eines bestimmten Pixelbereich errechnen 
      x1 gibt an, wie viele Pixel auf der linken Seite übersprungen werden sollen.
      x2 gibt an, wie viele Pixel auf der rechten Seite übersprungen werden sollen.
      y1 gibt an, wie viele Pixel auf der oberen Seite übersprungen werden sollen.
      y2 gibt an, wie viele Pixel auf der unteren Seite übersprungen werden sollen.
      a gibt an, an welcher stelle die Werte im Array gespeichert werden.
      
      y1 y1 y1 y1 y1 y1 y1 
      y1 y1 y1 y1 y1 y1 y1 
      x1 x1 P  P  x2 x2 x2
      x1 x1 P  P  x2 x2 x2
      y2 y2 y2 y2 y2 y2 y2 
    */
    static void getColorsTop(int x1, int x2, int y1, int y2, int a){
      int[] image = cameraTop.getImage();
      int p1 = y1*160+x1;
      int p2 = y1*160+160-x2; //y1*160+x1+160-x1-x2;
      int p3 = y1*160;
      int p4 = image.length-y2*160;
      int x = (120-y1-y2)*160-(x1*(120-y1-y2))-(x2*(120-y1-y2));
      int loop = 1;
      int loopEnd = (y1+loop)*160;
      for (int i=0; i < image.length; i++){
        int pixel = image[i];
        if (i >= p3 && i <= p4){
          if (i >= p1 && i < p2){
            rgbTop[a][0] += Camera.pixelGetRed(pixel);
            rgbTop[a][1] += Camera.pixelGetGreen(pixel);
            rgbTop[a][2] += Camera.pixelGetBlue(pixel);
            loopEnd = (y1+loop)*160;
            if(i > loopEnd){  
              p1 += 160;
              p2 += 160;
              loop++;  
            }
          }
        }
      }
      // image.length/x;
      rgbTop[a][0] /= x;
      rgbTop[a][1] /= x;
      rgbTop[a][2] /= x;
      
      System.out.println(a + ": TopCamera Pixel Farbwerte mit x1: " + x1 + "  x2: " + x2 + "  y1:" + y1 + "  y2:" + y2);
      System.out.println("red=" + rgbTop[a][0] + "    green=" + rgbTop[a][1] + "    blue="+ rgbTop[a][2]);
    } 
    //Selbe Methode für die BottomCamera
    static void getColorsBottom(int x1, int x2, int y1, int y2, int a){
      int[] image = cameraBottom.getImage();
      int p1 = y1*160+x1;
      int p2 = y1*160+160-x2; //y1*160+x1+160-x1-x2;
      int p3 = y1*160;
      int p4 = image.length-y2*160;
      int x = (120-y1-y2)*160-(x1*(120-y1-y2))-(x2*(120-y1-y2));
      int loop = 1;
      int loopEnd = (y1+loop)*160;
      for (int i=0; i < image.length; i++){
        int pixel = image[i];
        if (i >= p3 && i <= p4){
          if (i >= p1 && i < p2){
            rgbBottom[a][0] += Camera.pixelGetRed(pixel);
            rgbBottom[a][1] += Camera.pixelGetGreen(pixel);
            rgbBottom[a][2] += Camera.pixelGetBlue(pixel);
            loopEnd = (y1+loop)*160;
            if(i > loopEnd){  
              p1 += 160;
              p2 += 160;
              loop++;  
            }
          }
        }
      }
      // image.length/x;
      rgbBottom[a][0] /= x;
      rgbBottom[a][1] /= x;
      rgbBottom[a][2] /= x;

      System.out.println(a + ": BottomCamera Pixel Farbwerte mit x1: " + x1 + "  x2: " + x2 + "  y1:" + y1 + "  y2:" + y2);
      System.out.println("red=" + rgbBottom[a][0] + "    green=" + rgbBottom[a][1] + "    blue="+ rgbBottom[a][2]);
    }
    
    //Befüllt die RGB Arrays
    //Das Bild der Camera wird in 100 kleine Bilder zerteilt und im rgbTop und rgbBottom Array gespeichert
    static void scan() {
         int x1 = 0;
         int x2 = 144;
         int y1 = 0;
         int y2 = 108;
         int z = 0;
         
         for (int i = 0; i < 10; i++){
           for (int j = 0; j < 10; j++){
             getColorsTop(x1, x2, y1, y2, z);
             getColorsBottom(x1, x2, y1, y2, z);
             z++;
             x1 += 16;
             x2 -= 16;
           }
           x1 = 0;
           x2 = 144;
           y1 += 12;
           y2 -= 12;
         }
         /*
         getRGBTop(0, 150, 0, 110, 0); 
         getRGBTop(150, 0, 0, 110, 1);
         getRGBTop(0, 150, 110, 0, 2); 
         getRGBTop(150, 0, 110, 0, 3);
         getRGBTop(0, 150, 0, 110, 4); 
         getRGBTop(150, 0, 0, 110, 5);
         getRGBTop(0, 150, 110, 0, 6); 
         getRGBTop(150, 0, 110, 0, 7);
         getRGBTop(0, 150, 0, 110, 8); 
         getRGBTop(150, 0, 0, 110, 9);
         getRGBBottom(0, 150, 0, 110, 0); 
         getRGBBottom(150, 0, 0, 110, 1);          
         getRGBBottom(0, 150, 110, 0, 2); 
         getRGBBottom(150, 0, 110, 0, 3);
         getRGBBottom(0, 150, 0, 110, 4); 
         getRGBBottom(150, 0, 0, 110, 5);          
         getRGBBottom(0, 150, 110, 0, 6); 
         getRGBBottom(150, 0, 110, 0, 7);
         getRGBBottom(0, 150, 0, 110, 8); 
         getRGBBottom(150, 0, 0, 110, 9);   
         */
    } 
  
  static boolean sonarCheck(){
      double dist[] = new double[2];
      dist[0] = sonar[0].getValue();
      dist[1] = sonar[1].getValue();
      
      boolean hindernis = false; // Prüfung ob Drehung nötig
  
      for (int sensor = 0; sensor < 2; sensor++) {
        if (dist[sensor] < 0.40) { // Prüft beide Sensoren auf Hindernisse, entfernung 40cm
          hindernis = true;
        }
      }
      return hindernis;
  }
  
  static void leftCheck(){
  
    leftRGBBottom(10);
    rightRGBBottom(10);
  
    if(100 < rl2 && rl2 < 120 && 121 < gl2 && gl2 < 241 && 137 < bl2 && bl2 < 157){
         startMotion(turnRight40);
    } 
  }


  static void rightCheck(){
  
    leftRGBBottom(10);
    rightRGBBottom(10);
       
    if(100 < rr2 && rr2 < 120 && 121 < gr2 && gr2 < 241 && 137 < br2 && br2 < 157){
        startMotion(turnLeft40);
    }    
  }
  
  static void move(){
     boolean leftPathAvailable = false;
     boolean rightPathAvailable = false;

     scan();

     if (false){   // 
         finish = true;
     }
     //prüft ob ziel erreicht wurde
     if (!finish) {
        //alle 4 schritte werden die seiten geprüft
        if (stepCounter == 4){
          startMotion(turnLeft40); //turnHeadLeft ergänzen #73
          scan();
          startMotion(turnRight40); //turnHeadForward ergänzen #73
          if (5 < rgbTop[34][0] && rgbTop[34][0] < 15 && 4 < rgbTop[34][1] && rgbTop[34][1] < 13 && 1 < rgbTop[34][2] && rgbTop[34][2] < 11){
            if (5 < rgbTop[35][0] && rgbTop[35][0] < 15 && 4 < rgbTop[35][1] && rgbTop[35][1] < 13 && 1 < rgbTop[35][2] && rgbTop[35][2] < 11){
              leftPathAvailable = true;
            }
          } 
          stepCounter = 0;
       }
       
       if (sonarCheck()){
          startMotion(turnLeft40);  //turnHeadLeft ergänzen #73
          scan();
          startMotion(turnRight40);  //turnHeadForward ergänzen #73
          if (5 < rgbTop[34][0] && rgbTop[34][0] < 15 && 4 < rgbTop[34][1] && rgbTop[34][1] < 13 && 1 < rgbTop[34][2] && rgbTop[34][2] < 11){
            if (5 < rgbTop[35][0] && rgbTop[35][0] < 15 && 4 < rgbTop[35][1] && rgbTop[35][1] < 13 && 1 < rgbTop[35][2] && rgbTop[35][2] < 11){
              leftPathAvailable = true;
            }
          } else {
              startMotion(turnLeft40);  //turnHeadLeft ergänzen #73
              scan();
              startMotion(turnRight40);  //turnHeadForward ergänzen #73
              if (5 < rgbTop[34][0] && rgbTop[34][0] < 15 && 4 < rgbTop[34][1] && rgbTop[34][1] < 13 && 1 < rgbTop[34][2] && rgbTop[34][2] < 11){
                if (5 < rgbTop[35][0] && rgbTop[35][0] < 15 && 4 < rgbTop[35][1] && rgbTop[35][1] < 13 && 1 < rgbTop[35][2] && rgbTop[35][2] < 11){
                  rightPathAvailable = true;
                }
            }
           }
           if (leftPathAvailable == true) {
               startMotion(turnLeft40);
               leftPathAvailable = false;
           } else if (rightPathAvailable == true) {
               startMotion(turnRight40);
               rightPathAvailable = false;
           } else {

          }
         startMotion(forwards); 
         stepCounter++;
       } 
     
     //leftCheck();
     //10° nach rechts dreh
     //rightCheck();
     //10° nach links drehen         
 
  }

  public static void main(String[] args) {
    loadMotionsFiles();
    findAndEnableDevices();
    
    startMotion(handWave);

    while (robot.step(timeStep) != -1) {
      move();
    };
  }
}
