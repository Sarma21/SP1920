// File:          GyroController.java
// Date:
// Description:
// Author:
// Modifications:

// You may need to add other webots classes such as
//  import com.cyberbotics.webots.controller.DistanceSensor;
//  import com.cyberbotics.webots.controller.Motor;
import com.cyberbotics.webots.controller.*;

// Here is the main class of your controller.
// This class defines how to initialize and how to run your controller.
public class GyroController {
    static Robot robot = new Robot();
    static int timeStep = (int) Math.round(robot.getBasicTimeStep());
    static Gyro gyro = new Gyro("gyro");
    
  // This is the main function of your controller.
  // It creates an instance of your Robot instance and
  // it uses its function(s).
  // Note that only one instance of Robot should be created in
  // a controller program.
  // The arguments of the main function can be specified by the
  // "controllerArgs" field of the Robot node
  
    static void gyro(){
      double[] wert = gyro.getValues();
       System.out.println("X: " + wert[0] + "\nY: " + wert[1] + "\nZ: " + wert[2]);
    }
  public static void main(String[] args) {
    
    gyro.enable(timeStep);
    // create the Robot instance.


    // You should insert a getDevice-like function in order to get the
    // instance of a device of the robot. Something like:
    //  Motor motor = robot.getMotor("motorname");
    //  DistanceSensor ds = robot.getDistanceSensor("dsname");
    //  ds.enable(timeStep);

    // Main loop:
    // - perform simulation steps until Webots is stopping the controller
    while (robot.step(timeStep) != -1) {
      // Read the sensors:
      // Enter here functions to read sensor data, like:
      //  double val = ds.getValue();
      gyro();
      // Process sensor data here.

      // Enter here functions to send actuator commands, like:
      //  motor.setPosition(10.0);
    };

    // Enter here exit cleanup code.
  }
}
