import com.cyberbotics.webots.controller.*;
import java.io.File;

public class StandUpTest {
  static Robot robot = new Robot();
  static int timeStep = (int) Math.round(robot.getBasicTimeStep());
  static double[] acc;
  static Motion currentlyPlaying = null;
  
  // simulated devices
  static Accelerometer accelerometer;
  
  // motion file handles
  static Motion handWave, standUpFromFront, forwards50;

  static void findAndEnableDevices() {
    //Accelerometer
    accelerometer = new Accelerometer("accelerometer");
    accelerometer.enable(timeStep);
  }
  
  // load motion files
  public static void loadMotionFiles() {
    File file = new File(System.getProperty("user.dir"));
    file = (file.getParentFile().getParentFile());
    String pfad = file.getPath() + System.getProperty("file.separator") + "motions" + System.getProperty("file.separator");
    handWave = new Motion(pfad + "HandWave.motion");
    standUpFromFront = new Motion(pfad + "StandUpFromFront.motion");
    forwards50 = new Motion(pfad + "Forwards50.motion");
  }
  
  static void startMotion(Motion motion) {
    currentlyPlaying = motion;
    //start new motion
    currentlyPlaying.play();
    do {
      robot.step(timeStep);
    } while (!currentlyPlaying.isOver());
    currentlyPlaying = null;
  }

  static void move() {
    //
  }
 
  public static void main(String[] args) {
    // initialize stuff
    findAndEnableDevices();
    loadMotionFiles();
    startMotion(handWave);
    startMotion(forwards50);

    while (robot.step(timeStep) != -1) {
      acc = accelerometer.getValues();
      //Bei welchen acc werten ist es fallen?
      if((currentlyPlaying == null || currentlyPlaying.isOver()) && acc[0] > acc[1] && acc[0] > acc[2] && acc[0] < -5){
        System.out.println("currentlyPlaying == null");
        startMotion(standUpFromFront);
      } else {
        //laufe weiter
        startMotion(forwards50);
      }
      //move();
    };
  }
}