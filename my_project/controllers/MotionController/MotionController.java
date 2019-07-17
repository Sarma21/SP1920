// File:          MotionController.java
// Date:
// Description:
// Author:
// Modifications:

// You may need to add other webots classes such as
//  import com.cyberbotics.webots.controller.DistanceSensor;
//  import com.cyberbotics.webots.controller.LED;
//import java.util.Date;
//import java.util.concurrent.TimeUnit;
import com.cyberbotics.webots.controller.*;
import java.nio.file.Path;

// Here is the main class of your controller.
// This class defines how to initialize and how to run your controller.
public class MotionController {
  static String pfad = "/home/pc/Downloads/webots/projects/robots/softbank/nao/motions/";
  //static String path = FileSystems.getDefault().getPath("webots","projects","robots","softbank","nao","motions");
  //static String pfad = "../../";
  static Robot robot = new Robot();
  static int timeStep = (int) Math.round(robot.getBasicTimeStep());
  static Motion forWard, sideStepRight, turnLeft, handWave, anfall, up, bauch;
  // This is the main function of your controller.
  // It creates an instance of your Robot instance and
  // it uses its function(s).
  // Note that only one instance of Robot should be created in
  // a controller program.
  // The arguments of the main function can be specified by the
  // "controllerArgs" field of the Robot node
  
  /*
  static void start_motion(WbMotionRef motion) {
  // interrupt current motion
  if (currently_playing)
    wbu_motion_stop(currently_playing);

  // start new motion
  wbu_motion_play(motion);
  currently_playing = motion;
}
  */
   static void startMotion(Motion motion){
    motion.play();
    do {
    robot.step(timeStep);
    } while(! motion.isOver());
    
  }
  public static void loadMotionFiles(){
    forWard = new Motion(pfad + "Forwards.motion");
    sideStepRight = new Motion(pfad + "SideStepRight.motion");
    turnLeft = new Motion(pfad + "TurnLeft180.motion");
    handWave = new Motion(pfad + "HandWave.motion");
    anfall = new Motion(pfad + "Anfall.motion");
    up = new Motion(pfad + "StandUpFromFront.motion");
    bauch = new Motion(pfad + "bauch.motion");
  }
 
  
  
  public static void main(String[] args) {

    // create the Robot instance.
    
    loadMotionFiles();

    // get the time step of the current world.
    

    // You should insert a getDevice-like function in order to get the
    // instance of a device of the robot. Something like:
    //  LED led = robot.getLED("ledname");
    //  DistanceSensor ds = robot.getDistanceSensor("dsname");
    //  ds.enable(timeStep);
    
    
    
    startMotion(handWave);          
    startMotion(forWard);
    startMotion(forWard);
    startMotion(sideStepRight);
    startMotion(turnLeft); 
    startMotion(turnLeft);    
    startMotion(bauch);
    startMotion(bauch);
    startMotion(up);
    startMotion(handWave);

    

    // Main loop:
    // - perform simulation steps until Webots is stopping the controller
    while (robot.step(timeStep) != -1) {
      // Read the sensors:
      // Enter here functions to read sensor data, like:
      //  double val = ds.getValue();
      //startMotion(forWard);
      // Process sensor data here.
      // Enter here functions to send actuator commands, like:
      //  led.set(1);
    };

    // Enter here exit cleanup code.
  }
}
