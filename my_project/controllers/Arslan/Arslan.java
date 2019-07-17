import com.cyberbotics.webots.controller.*;

public class Arslan {
  public static void main(String[] args) {
    Robot robot = new Robot();
    // get the time step of the current world.
    int timeStep = (int) Math.round(robot.getBasicTimeStep());
 
    // You should insert a getDevice-like function in order to get the
    // instance of a device of the robot. Something like:
    //LED led = robot.getLED("ledname");
    DistanceSensor ds = robot.getDistanceSensor("generic");
    ds.enable(timeStep);

    // Main loop:
    // - perform simulation steps until Webots is stopping the controller
    while (robot.step(timeStep) != -1) {
      // Read the sensors:
      // Enter here functions to read sensor data, like:
      double val = ds.getValue();
      System.out.println("Sensor value is" + val);
      // Process sensor data here.

      // Enter here functions to send actuator commands, like:
      //  led.set(1);
    };

    // Enter here exit cleanup code.
  }
}
