import com.cyberbotics.webots.controller.*;

class MyController {
  public static void main(String[] args) {
    Robot robot = new Robot();

    int timeStep = (int) Math.round(robot.getBasicTimeStep());
    LED led = robot.getLED("my_led");
    DistanceSensor distanceSensor = robot.getDistanceSensor("my_distance_sensor");
    distanceSensor.enable(timeStep);

    // main control loop
    while (robot.step(timeStep) != -1) {
      // Read the sensors, like:
      double val = distanceSensor.getValue();

      // Process sensor data here

      // Enter here functions to send actuator commands, like:
      led.set(1);
    }

    // Enter here exit cleanup code
  }
}