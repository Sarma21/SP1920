import com.cyberbotics.webots.controller.*;
import java.io.File;

public class SensorController {
  static Robot robot = new Robot();
  static CameraRecognitionObject cameraRecognitionObject = new CameraRecognitionObject();
  static Keyboard keyboard = new Keyboard();
  static int timeStep = (int) Math.round(robot.getBasicTimeStep());
  static int[] imageTop, imageBottom;
  static double[] iu;
  
  // simulated devices
  static InertialUnit inertialUnit;
  static Camera cameraTop, cameraBottom; //cameras
  static TouchSensor lfoot_lbumper, lfoot_rbumper;  // left foot bumpers
  static TouchSensor rfoot_lbumper, rfoot_rbumper;  // right foot bumpers
  static DistanceSensor[] us = {new DistanceSensor("Sonar/Left"), new DistanceSensor("Sonar/Right")}; // ultrasound sensors
  static Accelerometer accelerometer;
  static GPS gps; //gps
  static Gyro gyro; //gyro
  static LED led; //led
  
  // motion file handles
  static Motion forWard, gehen, turnLeft40, turnLeft60, turnLeft180, handWave, standUpFromFront, bauch;

  static void findAndEnableDevices() {
    // inertialUnit
    inertialUnit = new InertialUnit("InertialUnit");
    inertialUnit.enable(timeStep);
    
    // camera
    cameraTop = new Camera("CameraTop");
    cameraBottom = new Camera("CameraBottom");
    cameraTop.enable(4*timeStep);
    cameraBottom.enable(4*timeStep);
    
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
    for (int i = 0; i < 2; i++) {
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
  
  static void camCheck(){
    imageTop = cameraTop.getImage();
    imageBottom = cameraBottom.getImage();
    System.out.println("imgTop: " + imageTop.length + "\nimgBottom: " + imageBottom.length);
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

  static void printUltrasoundSensors() {
    double dist[] = new double[2];
    for (int i = 0; i < 2; i++)
      dist[i] = us[i].getValue();
  
    System.out.println("-----ultrasound sensors-----");
    System.out.print("left: " + dist[0] +" m, right " + dist[1] + "m\n");
  }
  
  static void startMotion(Motion motion) {
    //start new motion
    motion.play();
    do {
      robot.step(timeStep);
    } while (!motion.isOver());
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
      //startMotion(turnLeft40);
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
    System.out.println("Links L: " + ll + " R: " + lr + "\nRechts L: " + rl + " R: " + rr);
  }
  
  public static void main(String[] args) {
    //robot.setMode(Robot.MODE_REMOTE_CONTROL, s);
    // initialize stuff
    findAndEnableDevices();
    loadMotionFiles();
    
    startMotion(handWave);
    //camCheck();

    while (robot.step(timeStep) != -1) {
      //gefallen();
      //printUltrasoundSensors();
      move();
      //iu = inertialUnit.getRollPitchYaw();
    };
  }
}
