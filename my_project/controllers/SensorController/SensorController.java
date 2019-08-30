import com.cyberbotics.webots.controller.*;
import java.io.File;

public class SensorController {
  static Robot robot = new Robot();
  static Keyboard keyboard = new Keyboard();
  static int timeStep = (int) Math.round(robot.getBasicTimeStep());
  
  // simulated devices
  static Camera cameraTop, cameraBottom; //cameras
  static TouchSensor lfoot_lbumper, lfoot_rbumper;  // left foot bumpers
  static TouchSensor rfoot_lbumper, rfoot_rbumper;  // right foot bumpers
  static DistanceSensor[] us = {new DistanceSensor("Sonar/Left"), new DistanceSensor("Sonar/Right")}; // ultrasound sensors
  static Accelerometer accelerometer;
  static GPS gps; //gps
  static Gyro gyro; //gyro
  static LED led; //led
  
  // motion file handles
  static Motion handWave,forWard, gehen, sideStepRight, turnLeft, turnLeft60, turnLeft180, anfall, up, bauch, shoot;
  static String  pfad = System.getProperty("user.dir") + System.getProperty("file.separator") + "Motions"
      + System.getProperty("file.separator");
  static File file = new File(System.getProperty("user.dir"));
  static String fs = System.getProperty("file.separator");
  static void findAndEnableDevices() {
    // camera
    cameraTop = new Camera("CameraTop");
    cameraBottom = new Camera("CameraBottom");
    cameraTop.enable(4*timeStep);
    cameraBottom.enable(4*timeStep);
    //TouchSensor
    //DistanceSensor
    //Accelerometer
    accelerometer = new Accelerometer("accelerometer");
    accelerometer.enable(timeStep);
    //GPS
    gps = new GPS("gps");
    gps.enable(timeStep);
    // gyro
    gyro = new Gyro("gyro");
    gyro.enable(timeStep);
    // ultrasound sensors
    //us[0] = new DistanceSensor("Sonar/Left");
    //us[1] = new DistanceSensor("Sonar/Right");
    int i;
    for (i = 0; i < 2; i++) {
      us[i].enable(timeStep);
    }
    
    //foot bumpers
    lfoot_lbumper = new TouchSensor("LFoot/Bumper/Left");
    lfoot_rbumper = new TouchSensor("LFoot/Bumper/Right");
    rfoot_lbumper = new TouchSensor("RFoot/Bumper/Left");
    rfoot_rbumper = new TouchSensor("RFoot/Bumper/Right");
    lfoot_lbumper.enable(timeStep);
    lfoot_rbumper.enable(timeStep);
    rfoot_lbumper.enable(timeStep);
    rfoot_rbumper.enable(timeStep);
    //keyboard
    keyboard.enable(10 * timeStep);
  }
  
  // load motion files
  public static void loadMotionFiles() {
  handWave = new Motion(pfad + "HandWave.motion");
  shoot = new Motion(pfad + "Shoot.motion");
  up = new Motion(pfad + "StandUpFromFront.motion");
    turnLeft180 = new Motion(pfad + "TurnLeft180.motion");
    forWard = new Motion(pfad + "Gehen50.motion");
    gehen = new Motion(pfad + "Gehen50Anfang.motion");
    sideStepRight = new Motion(pfad + "SideStepRight.motion");
    turnLeft = new Motion(pfad + "TurnLeft40.motion");
    turnLeft60 = new Motion(pfad + "TurnLeft60.motion");
    
    anfall = new Motion(pfad + "Anfall.motion");
    
    bauch = new Motion(pfad + "bauch.motion");
    
  }

  static void startMotion(Motion motion) {
    //start new motion
    motion.play();
    do {
      robot.step(timeStep);
    } while (!motion.isOver());

  }

  static void printUltrasoundSensors() {
    double dist[] = new double[2];
    int i;
    for (i = 0; i < 2; i++)
      dist[i] = us[i].getValue();
  
    System.out.println("-----ultrasound sensors-----");
    System.out.print("left: " + dist[0] +" m, right " + dist[1] + "m\n");
  }
  
  static void move() {
    double dist[] = new double[2];
    double fs[] = new double[2];
    dist[0] = us[0].getValue();
    dist[1] = us[1].getValue();
    
    boolean hindernis = false; // Prüfung ob Drehung nötig

    for (int sensor = 0; sensor < 2; sensor++) {
      if (dist[sensor] < 0.50) { // Prüft beide Sensoren auf Hindernisse
        hindernis = true;
      }
    }
    
    if (hindernis) {
      //startMotion(turnLeft);
      startMotion(turnLeft60);
    } else {
      startMotion(forWard);
    }
  }

  static void runCommand(int key) {
    switch(key) {
      case 'a': printUltrasoundSensors(); break;
      case 3: printUltrasoundSensors(); break;
    }
  }
  
  static void gefallen() {
    int ll = (int) lfoot_lbumper.getValue();
    int lr = (int) lfoot_rbumper.getValue();
    int rl = (int) rfoot_lbumper.getValue();
    int rr = (int) rfoot_rbumper.getValue();
    
    System.out.println("Links L: " + ll + " R: " + lr);
    System.out.println("Rechts L: " + rl + " R: " + rr);
  }
  
    static void gyro(){
       double[] wert = gyro.getValues();
       System.out.println("X: " + wert[0] + "\nY: " + wert[1] + "\nZ: " + wert[2]);
    }
  
  public static void main(String[] args) {
    // initialize stuff
    findAndEnableDevices();
    loadMotionFiles();
    file = file.getParentFile();
    pfad = file.getParentFile() + fs + "Motions" + fs;
    
    System.out.println("Pfad:  " + pfad);
    
    //startMotion(shoot);
    //until key is pressed
    int key = -1;
    do {
      move();
      
      key = keyboard.getKey();
    } while (key >= 0);
  
    while (robot.step(timeStep) != -1) {
      if(key >= 0) runCommand(key);
      gefallen();
      //printUltrasoundSensors();
      move();
      key = keyboard.getKey();
    };
  }
}