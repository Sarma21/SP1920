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
  //TopCamera RGB eines bestimmten Bereichs
  static int r1 = 0;
  static int g1 = 0;
  static int b1 = 0; 
  //BottomCamera RGB eines bestimmten Bereichs
  static int r2 = 0;
  static int g2 = 0;
  static int b2 = 0;
  
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
  static Motion forwards, forwards50, gehen, turnLeft40, turnLeft60, turnRight40, turnRight60, handWave, standUpFromFront, bauch;
    
   static void startMotion(Motion motion){
    motion.play();
    do {
    robot.step(timeStep);
    } while(! motion.isOver());
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
  // ultrasound sensors
  sonar[0].enable(timeStep);
  sonar[1].enable(timeStep);

  /*
  // inertialUnit
  inertialUnit = new InertialUnit("InertialUnit");
  inertialUnit.enable(timeStep);
  
  //GPS
  gps = new GPS("gps");
  gps.enable(timeStep);
  
  // gyro
  gyro = new Gyro("gyro");
  gyro.enable(timeStep);
  
  //keyboard
  keyboard.enable(10 * timeStep);
  
  //foot bumpers
  lfoot_lbumper = new TouchSensor("LFoot/Bumper/Left");
  lfoot_rbumper = new TouchSensor("LFoot/Bumper/Right");
  rfoot_lbumper = new TouchSensor("RFoot/Bumper/Left");
  rfoot_rbumper = new TouchSensor("RFoot/Bumper/Right");
  lfoot_lbumper.enable(timeStep);
  lfoot_rbumper.enable(timeStep);
  rfoot_lbumper.enable(timeStep);
  rfoot_rbumper.enable(timeStep);
  */
}
  
  static void loadMotionsFiles(){
    File file = new File(System.getProperty("user.dir"));
    file = (file.getParentFile().getParentFile());
    String pfad = file.getPath() + System.getProperty("file.separator") + "motions" + System.getProperty("file.separator");
    forwards = new Motion(pfad + "Forwards.motion");
    gehen = new Motion(pfad + "Gehen50.motion");
    handWave = new Motion(pfad + "HandWave.motion");
    turnLeft40 = new Motion(pfad + "TurnLeft40");
    turnRight40 = new Motion(pfad + "TurnRight40");
    
  }
  
  // x gibt an wie viele Pixel von der linken seite du betrachten willst
  // x = 160 für Fullscreen
  static void leftPixelTop(int x){
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
  static void rightPixelTop(int x){
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
  static void leftPixelBottom(int x){
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
  
    static void rightPixelBottom(int x){
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
      Mir dieser Funktion lässt sich der durchschnitts Farbwert eines bestimmten Pixelbereich errechnen 
      x1 gibt an, wie viele Pixel auf der linken Seite übersprungen werden sollen.
      x2 gibt an, wie viele Pixel auf der rechten Seite übersprungen werden sollen.
      y1 gibt an, wie viele Pixel auf der oberen Seite übersprungen werden sollen.
      y2 gibt an, wie viele Pixel auf der unteren Seite übersprungen werden sollen.
      
      y1 y1 y1 y1 y1 y1 y1 
      y1 y1 y1 y1 y1 y1 y1 
      x1 x1 P  P  x2 x2 x2
      x1 x1 P  P  x2 x2 x2
      y2 y2 y2 y2 y2 y2 y2 
    */
    static void getPixelTop(int x1, int x2, int y1, int y2){
      int[] image = cameraTop.getImage();
      int p1 = y1*160+x1;
      int p2 = y1*160+160-x2; //y1*160+x1+160-x1-x2;
      int p3 = y1*160;
      int p4 = image.length - y2*160;
      int loop = 0;
      int loopEnd = 120 - y1 - y2; 
      int x = x1*x2*y1*y2;
      for (int i=0; i < image.length; i++){
        int pixel = image[i];
        if (i >= p3 && i <= p4){
          if (loop < loopEnd){
            if (i >= p1 && i <= p2){
              r1 += Camera.pixelGetRed(pixel);
              g1 += Camera.pixelGetGreen(pixel);
              b1 += Camera.pixelGetBlue(pixel);
            }
            p1 += 160;
            p2 += 160;
            loop++;
          }
        }
      }
      // image.length/x;
      r1 /= x;
      g1 /= x;
      b1 /= x;
      
      //System.out.println("TopCamera Pixel Farbwerte mit x1: " + x1 + "  x2: " + x2 + "  y1:" + y1 + "  y2:" + y2);
      //System.out.println("red=" + r1 + "\tgreen=" + g1 + "\tblue="+ b1);
    } 
    //Selbe Methode für die BottomCamera
    static void getPixelBottom(int x1, int x2, int y1, int y2){
      int[] image = cameraBottom.getImage();
      int p1 = y1*160+x1;
      int p2 = y1*160+160-x2; //y1*160+x1+160-x1-x2;
      int p3 = y1*160;
      int p4 = image.length - y2*160;
      int loop = 0;
      int loopEnd = 120 - y1 - y2; 
      int x = x1*x2*y1*y2;
      for (int i=0; i < image.length; i++){
        int pixel = image[i];
        if (i >= p3 && i <= p4){
          if (loop < loopEnd){
            if (i >= p1 && i <= p2){
              r2 += Camera.pixelGetRed(pixel);
              g2 += Camera.pixelGetGreen(pixel);
              b2 += Camera.pixelGetBlue(pixel);
            }
            p1 += 160;
            p2 += 160;
            loop++;
          }
        }
      }
      // image.length/x;
      r2 /= x;
      g2 /= x;
      b2 /= x;

      //System.out.println("BottomCamera Pixel Farbwerte mit x1: " + x1 + "  x2: " + x2 + "  y1:" + y1 + "  y2:" + y2);
      //System.out.println("red=" + r2 + "\tgreen=" + g2 + "\tblue="+ b2);
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
  
    leftPixelBottom(10);
    rightPixelBottom(10);
  
    if(100 < rl2 && rl2 < 120 && 121 < gl2 && gl2 < 241 && 137 < bl2 && bl2 < 157){
         startMotion(turnRight40);
    } 
  }


  static void rightCheck(){
  
    leftPixelBottom(10);
    rightPixelBottom(10);
       
    if(100 < rr2 && rr2 < 120 && 121 < gr2 && gr2 < 241 && 137 < br2 && br2 < 157){
        startMotion(turnLeft40);
    }    
       /*
       int x = 0;
      
      if (111 < rl1 && rl1 < 131 && 109 < gl1 && gl1 < 129 && 104 < bl1 && bl1 < 124){
          if (197 < rr1 && rr1 < 217 && 144 < gr1 && gr1 < 164 && 98 < bl1 && bl1 < 118) {
              x = x + 1;
          }
          x = x + 1;
      }
      
      if (114 < rl2 && rl2 < 134 && 119 < gl2 && gl2 < 139 && 121 < bl2 && bl2 < 141){
          if (89 < rr2 && rr2 < 109 && 106 < gr2 && gr2 < 126 && 121 < bl2 && bl2 < 141) {
              x = x + 1;
          }
          x = x + 1;
      }
      
      System.out.println("X: " + x);
      
      if (x > 3){
          return true;
      }   
      */
  }
  
  static void move(){
  
     if (!finish) {
       //prüft ob ziel erreicht wurde
       if (false){
         finish = true;
       }
       if (sonarCheck()){
        //TurnHead
       }     
       //Weg korrektur nötig?
       /*
       40 Pixel prüfung
       Top     Links r102 g122 137  Rechts r114 g123 b134
       Bottom  Links r124 g129 b131 Rechts r206 g156 b109
       80 Pixel prüfung
       Top     Links r105 g120 b131 Rechts r120 g124 b129
       Bottom  Links r161 g139 b118 Rechts r205 g154 b107 
       
       40 Pixel prüfung
       Top     Links r121 g119 b114 Rechts r99 g116 b131
       Bottom  Links r207 g154 b108 Rechts r99 g116 b131
       80 Pixel prüfung
       Top     Links r116 g118 b118 Rechts r99 g116 b130
       Bottom  Links r200 g151 b108 Rechts r114 g121 b127
       */
       leftCheck();
        //10° nach rechts dreh
       rightCheck();
        //10° nach links drehen 
       startMotion(forwards);
   }
    
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
