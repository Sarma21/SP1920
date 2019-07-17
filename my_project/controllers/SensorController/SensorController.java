// File:          SensorController.java
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
public class SensorController {

  static Robot robot = new Robot();
  static Camera topCamera = new Camera("CameraTop");
  static Camera bottomCamera = new Camera("CameraBottom");
  
  static DistanceSensor sonar[] = {new DistanceSensor("Sonar/Left"), new DistanceSensor("Sonar/Right")};
 // sonar[0] = new DistanceSensor("Sonar/Left"); 
 // sonar[1] = new DistanceSensor("Sonar/Right");
  
  
  static int timeStep = (int) Math.round(robot.getBasicTimeStep());
 
 
  
  
  static void printDistanceSensor(){
    double dist[] = new double[2];
    
    
    dist[0] = sonar[0].getValue();
    dist[1] = sonar[1].getValue();
    
    System.out.println("-------Sonar--------");
    System.out.println("left: " + dist[0]+ " m, right: " + dist[1] + " m\n");
    
  }
  /*
  static void print_ultrasound_sensors() {
  double dist[2];
  int i;
  for (i = 0; i < 2; i++)
    dist[i] = wb_distance_sensor_get_value(us[i]);

  printf("-----ultrasound sensors-----\n");
  printf("left: %f m, right %f m\n", dist[0], dist[1]);
}
  */
  // This is the main function of your controller.
  // It creates an instance of your Robot instance and
  // it uses its function(s).
  // Note that only one instance of Robot should be created in
  // a controller program.
  // The arguments of the main function can be specified by the
  // "controllerArgs" field of the Robot node
  public static void main(String[] args) {

    // create the Robot instance.
    sonar[0].enable(timeStep);
    sonar[1].enable(timeStep);
    
    
    printDistanceSensor();
    // get the time step of the current world.
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

      // Process sensor data here.

      // Enter here functions to send actuator commands, like:
      //  led.set(1);
    };

    // Enter here exit cleanup code.
  }
}
