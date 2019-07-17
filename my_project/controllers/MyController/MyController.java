// File:          MyController.java
// Date:
// Description:
// Author:
// Modifications:

// You may need to add other webots classes such as
//  import com.cyberbotics.webots.controller.DistanceSensor;
//  import com.cyberbotics.webots.controller.LED;
import com.cyberbotics.webots.controller.*;


// Here is the main class of your controller.
// This class defines how to initialize and how to run your controller.
public class MyController {
  
  static Camera topCamera = new Camera("CameraTop");
  static Camera bottomCamera = new Camera("CameraBottom");
  
  //Accelerometer accelerometer;
  // This is the main function of your controller.
  // It creates an instance of your Robot instance and
  // it uses its function(s).
  // Note that only one instance of Robot should be created in
  // a controller program.
  // The arguments of the main function can be specified by the
  // "controllerArgs" field of the Robot node
  
  //Kamera erkennen
  static void print_camera_image(Camera camera) {
    
    int width = camera.getWidth();
    int height = camera.getHeight();
    
    // read rgb pixel values from the camera
    int[] image = camera.getImage();
  }
  
  public static void main(String[] args) {

    // create the Robot instance.
    Robot robot = new Robot();
     
    
    
    Motion motin = new Motion("/home/pc/Downloads/webots/projects/robots/softbank/nao/motions/Anfall.motion");
    

    // get the time step of the current world.
    int timeStep = (int) Math.round(robot.getBasicTimeStep());
    topCamera.enable(4*timeStep);
    topCamera.getImage();
    // You should insert a getDevice-like function in order to get the
    // instance of a device of the robot. Something like:
    //  LED led = robot.getLED("ledname");
    //  DistanceSensor ds = robot.getDistanceSensor("dsname");
    //  ds.enable(timeStep);

    // Main loop:
    // - perform simulation steps until Webots is stopping the controller
    while (robot.step(timeStep) != -1) {
      // Read the sensors:
      // Enter here functions to read sensor data, like:
      //  double val = ds.getValue();
        motin.play();
      // Process sensor data here.

      // Enter here functions to send actuator commands, like:
      //  led.set(1);
    };

    // Enter here exit cleanup code.
  }
}
