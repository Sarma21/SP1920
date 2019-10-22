import com.cyberbotics.webots.controller.*;
import java.io.File;

public class StandUpTest {
  static Robot robot = new Robot();
  static int timeStep = (int) Math.round(robot.getBasicTimeStep());
  static double[] acc;
  static Motion currentlyPlaying = null;
  
  // simulated devices
  static Accelerometer accelerometer;
  static TouchSensor[] fsr = {new TouchSensor("LFsr"), new TouchSensor("RFsr")};                        // force sensitive resistors
  
  // motion file handles
  static Motion handWave, standUpFromFront, forwards50;

  static void findAndEnableDevices() {
    //Accelerometer
    accelerometer = new Accelerometer("accelerometer");
    accelerometer.enable(timeStep);
    
    // foot sensors
    fsr[0].enable(timeStep);
    fsr[1].enable(timeStep);
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
static double clamp(double value, double min, double max) {
  if (min > max) {
    //assert(0);
    return value;
  }
  return value < min ? min : value > max ? max : value;
}

static void print_foot_sensors() {
// force sensor values
  double[][] fsv = {
    fsr[0].getValues(),
    fsr[1].getValues()
  };
  
  double newtonLeft = 0, newtonRight = 0;

  // The coefficients were calibrated against the real
  // robot so as to obtain realistic sensor values.
  double l[] = {
    fsv[0][2] / 3.4 + 1.5 * fsv[0][0] + 1.15 * fsv[0][1],  // Left Foot Front Left
    fsv[0][2] / 3.4 + 1.5 * fsv[0][0] - 1.15 * fsv[0][1],  // Left Foot Front Right
    fsv[0][2] / 3.4 - 1.5 * fsv[0][0] - 1.15 * fsv[0][1],  // Left Foot Rear Right
    fsv[0][2] / 3.4 - 1.5 * fsv[0][0] + 1.15 * fsv[0][1],  // Left Foot Rear Left
  };
  double r[] = {
    fsv[1][2] / 3.4 + 1.5 * fsv[1][0] + 1.15 * fsv[1][1],  // Right Foot Front Left
    fsv[1][2] / 3.4 + 1.5 * fsv[1][0] - 1.15 * fsv[1][1],  // Right Foot Front Right
    fsv[1][2] / 3.4 - 1.5 * fsv[1][0] - 1.15 * fsv[1][1],  // Right Foot Rear Right
    fsv[1][2] / 3.4 - 1.5 * fsv[1][0] + 1.15 * fsv[1][1],  // Right Foot Rear Left
  };

  int i;
  for (i = 0; i < 4; ++i) {
    l[i] = clamp(l[i], 0, 25);
    r[i] = clamp(r[i], 0, 25);
    newtonLeft += l[i];
    newtonRight += r[i];
  }

  System.out.println("----------foot sensors----------\n");
  System.out.println("   left       right\n");
  System.out.println("+--------+ +--------+\n");
  System.out.println("|" + (float) l[0] +" "+ (float) l[1] + "| |" + (float) r[0] +" "+ (float) r[1] + "| front\n");
  System.out.println("|        | |        |\n");
  System.out.println("|" + (float) l[3] +" "+ (float) l[2] + "| |" + (float) r[3] +" "+ (float) r[2] + "| back\n");
  System.out.println("+--------+ +--------+\n");
  System.out.println("total: " + (float) (newtonLeft + newtonRight) + " Newtons, " + (float) ((newtonLeft + newtonRight) / 9.81) + " kilograms\n");
}

 
  public static void main(String[] args) {
    // initialize stuff
    findAndEnableDevices();
    loadMotionFiles();
    //startMotion(handWave);
    
    while (robot.step(timeStep) != -1) {
      acc = accelerometer.getValues();
      print_foot_sensors();
      //Bei einem "Sturz" von von 0.05 m steht der Roboter erst auf.
      //if((currentlyPlaying == null || currentlyPlaying.isOver()) && Math.abs(acc[0]) > Math.abs(acc[1]) && Math.abs(acc[0]) > Math.abs(acc[2]) && Math.abs(acc[0]) < -1){
      if((currentlyPlaying == null || currentlyPlaying.isOver()) && acc[0] > acc[1] && acc[0] > acc[2] && acc[0] < -5){
        System.out.println("currentlyPlaying == null");
        
        startMotion(standUpFromFront);
      } else {
        //laufe weiter
        //startMotion(forwards50);
      }
      //move();
    };
  }
}
