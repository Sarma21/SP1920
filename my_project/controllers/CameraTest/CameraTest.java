import com.cyberbotics.webots.controller.*;

public class CameraTest {
  static Robot robot = new Robot();
  static Keyboard keyboard = new Keyboard();
  static int timeStep = (int) Math.round(robot.getBasicTimeStep());
  static int[] imageTop, imageBottom;
  
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
//  static Motion handWave,forWard, gehen, sideStepRight, turnLeft, turnLeft60, turnLeft180, anfall, up, bauch, shoot;
  static String pfad = System.getProperty("user.dir") + System.getProperty("file.separator") + "Motions"
      + System.getProperty("file.separator");
      
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

  static void camCheck(){
    System.out.println("Array: 222" );
    imageTop = cameraTop.getImage();    //int[] imageTop
    System.out.println("Array: 3333" );
    imageBottom = cameraBottom.getImage(); //int[] imageBottom
    System.out.println("Array: 44444" );
    
    System.out.println("Array: " + imageTop.length);
  }

  public static void main(String[] args) {
    // initialize stuff
    findAndEnableDevices();
//    loadMotionFiles();
    System.out.println("Array: 11" );
    camCheck();
    //startMotion(shoot);
    //until key is pressed

  
    while (robot.step(timeStep) != -1) {

    };
  }
}