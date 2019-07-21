import com.cyberbotics.webots.controller.*;

public class SensorController {
  static Robot robot = new Robot();
  static Keyboard keyboard = new Keyboard();
  static int timeStep = (int) Math.round(robot.getBasicTimeStep());
  
  // simulated devices
  static Camera cameraTop, cameraBottom; //cameras
  static DistanceSensor[] us = {new DistanceSensor("Sonar/Left"), new DistanceSensor("Sonar/Right")}; // ultrasound sensors
  
  // motion file handles
  static Motion forWard, gehen, sideStepRight, turnLeft, turnLeft60, turnLeft180, handWave, anfall, up, bauch;
  static String pfad = System.getProperty("user.dir") + System.getProperty("file.separator") + "Motions"
      + System.getProperty("file.separator");
      
  static void findAndEnableDevices() {
    // camera
    cameraTop = new Camera("CameraTop");
    cameraBottom = new Camera("CameraBottom");
    // gyro
    
    // ultrasound sensors
    //us[0] = new DistanceSensor("Sonar/Left");
    //us[1] = new DistanceSensor("Sonar/Right");
    int i;
    for (i = 0; i < 2; i++) us[i].enable(timeStep);
      
    //keyboard
    keyboard.enable(10 * timeStep);
  }
  
  // load motion files
  public static void loadMotionFiles() {
    //handWave = new Motion("../../motions/HandWave.motion");
    handWave = new Motion(pfad + "HandWave.motion");
    forWard = new Motion(pfad + "Gehen50.motion");
    gehen = new Motion(pfad + "Gehen50Anfang.motion");
    sideStepRight = new Motion(pfad + "SideStepRight.motion");
    turnLeft = new Motion(pfad + "TurnLeft40.motion");
    turnLeft60 = new Motion(pfad + "TurnLeft60.motion");
    turnLeft180 = new Motion(pfad + "TurnLeft180.motion");
    anfall = new Motion(pfad + "Anfall.motion");
    up = new Motion(pfad + "StandUpFromFront.motion");
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
  
    System.out.print("-----ultrasound sensors-----\n");
    System.out.print("left: " + dist[0] +" m, right " + dist[1] + "m\n");
  }
  
  static void move() {
    double dist[] = new double[2];
    dist[0] = us[0].getValue();
    dist[1] = us[1].getValue();
   
    boolean check = false; // Prüfung ob Drehung nötig

    // wenn hindernis, dann
    for (int sensor = 0; sensor < 2; sensor++) {
      if (dist[sensor] < 0.40) { // Prüft beide Sensoren auf Hindernisse
        check = true;
      }
    }
    if (check) {
      System.out.println("turnLeft");
      startMotion(turnLeft);
      System.out.println("turnLeft60");
      startMotion(turnLeft60);
    } else {
      System.out.println("forWard");
      // startMotion(gehen);
      startMotion(forWard); // geradeaus
    }
  }
  
  static void runCommand(int key) {
    switch(key) {
      case 'U': printUltrasoundSensors(); break;
    }
  }
  public static void main(String[] args) {
    // initialize stuff
    findAndEnableDevices();
    loadMotionFiles();
    
    startMotion(handWave);
    //until key is pressed
    int key = -1;
    do {
      move();
      key = keyboard.getKey();
    } while (key >= 0);
  
    while (robot.step(timeStep) != -1) {
      if(key >= 0) runCommand(key);

      move();
      key = keyboard.getKey();
    };
  }
}