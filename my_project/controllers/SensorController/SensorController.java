import com.cyberbotics.webots.controller.*;
import java.io.File;

public class SensorController {
  static Robot robot = new Robot();
  static CameraRecognitionObject cameraRecognitionObject = new CameraRecognitionObject();
  static Keyboard keyboard = new Keyboard();
  static int timeStep = (int) Math.round(robot.getBasicTimeStep());
  static int[] imageTop, imageBottom;
  static double[] iu;
  static double[] acc;
  static Motion currentlyPlaying = null;
  
  // simulated devices
  static InertialUnit inertialUnit;
  static Camera cameraTop, cameraBottom; //cameras
  static TouchSensor lfoot_lbumper, lfoot_rbumper;  // left foot bumpers
  static TouchSensor rfoot_lbumper, rfoot_rbumper;  // right foot bumpers
  static DistanceSensor[] sonar = {new DistanceSensor("Sonar/Left"), new DistanceSensor("Sonar/Right")}; // ultrasound sensors
  static Accelerometer accelerometer;
  static GPS gps; //gps
  static Gyro gyro; //gyro
  static LED led; //led
  
  // motion file handles
  static Motion forwards, forwards50, gehen50, gehen50Anfang, turnLeft40, turnLeft60, turnLeft90, turnLeft180, handWave, standUpFromFront, bauch;

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
  
  // load motion files
  public static void loadMotionFiles() {
    File file = new File(System.getProperty("user.dir"));
    file = (file.getParentFile().getParentFile());
    String pfad = file.getPath() + System.getProperty("file.separator") + "motions" + System.getProperty("file.separator");
    
    handWave = new Motion(pfad + "HandWave.motion");
    gehen50 = new Motion(pfad + "Gehen50.motion");
    gehen50Anfang = new Motion(pfad + "Gehen50Anfang.motion");
    turnLeft40 = new Motion(pfad + "TurnLeft40.motion");
    turnLeft60 = new Motion(pfad + "TurnLeft60.motion");
    turnLeft90 = new Motion(pfad + "TurnLeft90.motion");
    turnLeft180 = new Motion(pfad + "TurnLeft180.motion");
    standUpFromFront = new Motion(pfad + "StandUpFromFront.motion");
    bauch = new Motion(pfad + "bauch.motion");
  }

   
  static void camCheck(){
    imageTop = cameraTop.getImage();
    imageBottom = cameraBottom.getImage();
    System.out.println("imgTop: " + imageTop.length + "\nimgBottom: " + imageBottom.length);
  }
  /* Für Debug Zwecke 
  
    static void gefallen() {
    int ll = (int) lfoot_lbumper.getValue();
    int lr = (int) lfoot_rbumper.getValue();
    int rl = (int) rfoot_lbumper.getValue();
    int rr = (int) rfoot_rbumper.getValue();
    System.out.println("Links L: " + ll + " R: " + lr + "\nRechts L: " + rl + " R: " + rr);
  }
  */
  
  static void startMotion(Motion motion) {
    currentlyPlaying = motion;
    //start new motion
    currentlyPlaying.play();
    do {
      robot.step(timeStep);
    } while (!currentlyPlaying.isOver());
  }

  static void move() {
    boolean hindernis = false;
    double[] dist = {sonar[0].getValue(), sonar[1].getValue()};
    //double fs[] = new double[2];
    
    for (int sensor = 0; sensor < 2; sensor++) {
      if (dist[sensor] < 0.6) hindernis = true; // Prüft beide Sensoren auf Hindernisse
    }
    
    //wenn Roboter vor einer Wand ist, dann stoppe momentane Motion
    if (hindernis) {
      currentlyPlaying.stop();
      /* @TODO prüfe ob links frei ist
      ...
      */
      }
    else startMotion(gehen50);
  }
 
  public static void main(String[] args) {
    // initialize stuff
    findAndEnableDevices();
    loadMotionFiles();
    startMotion(handWave);
    startMotion(gehen50Anfang);
    camCheck();

    while (robot.step(timeStep) != -1) {
      //gefallen();
      move();
      //iu = inertialUnit.getRollPitchYaw();
    };
  }
}