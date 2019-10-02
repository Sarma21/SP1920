import com.cyberbotics.webots.controller.*;
import java.io.File;

public class MotionController {
  static Robot robot = new Robot();
  static int timeStep = (int) Math.round(robot.getBasicTimeStep());
  static double[] iu;
  static double[] acc;
  static Motion currentlyPlaying = null;
  
  // simulated devices
  static DistanceSensor[] us = {new DistanceSensor("Sonar/Left"), new DistanceSensor("Sonar/Right")}; // ultrasound sensors
  static Accelerometer accelerometer;
  
  // motion file handles
  static Motion forWard, gehen, turnLeft40, turnLeft60, turnLeft180, handWave, standUpFromFront, bauch;

  static void findAndEnableDevices() {
    //Accelerometer
    accelerometer = new Accelerometer("accelerometer");
    accelerometer.enable(timeStep);
    
    // ultrasound sensors
    for (int i = 0; i < 2; i++) {
      us[i].enable(timeStep);
    }
  }
  
  // load motion files
  public static void loadMotionFiles() {
    File file = new File(System.getProperty("user.dir"));
    file = (file.getParentFile().getParentFile());
    String pfad = file.getPath() + System.getProperty("file.separator") + "motions" + System.getProperty("file.separator");
    
    handWave = new Motion(pfad + "HandWave.motion");
    forWard = new Motion(pfad + "Gehen50.motion");
    gehen = new Motion(pfad + "Gehen50Anfang.motion");
    turnLeft40 = new Motion(pfad + "TurnLeft40.motion");
    turnLeft60 = new Motion(pfad + "TurnLeft60.motion");
    turnLeft180 = new Motion(pfad + "TurnLeft180.motion");
    standUpFromFront = new Motion(pfad + "StandUpFromFront.motion");
    bauch = new Motion(pfad + "bauch.motion");
  }
  
  static void startMotion(Motion motion) {
    if (!currentlyPlaying.isOver()) currentlyPlaying.stop();
    //start new motion
    motion.play();
    do {
      robot.step(timeStep);
    } while (!motion.isOver());
    
  }
  
   static void printAccelerometer() {
    System.out.println("X:" + acc[0] + "\tY:" + acc[1] + "\tZ:" + acc[2]);
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
      startMotion(turnLeft60);
    } else {
      startMotion(forWard);
    }
  }
  
  
  public static void main(String[] args) {
    // initialize stuff
    findAndEnableDevices();
    loadMotionFiles();
    
    startMotion(handWave);
    
    while (robot.step(timeStep) != -1) {
      acc = accelerometer.getValues();
 
      if (currentlyPlaying == null || currentlyPlaying.isOver()) {
        startMotion(standUpFromFront);
      } else {
        move();
      }
    };
  }
}