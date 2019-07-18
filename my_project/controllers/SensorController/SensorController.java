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

  static String pfad = "/home/pc/Downloads/webots/projects/robots/softbank/nao/motions/"; 
  static Motion forWard, gehen, sideStepRight, turnLeft, turnLeft60, turnLeft180, handWave, anfall, up, bauch;
  static Robot robot = new Robot();
  static int timeStep = (int) Math.round(robot.getBasicTimeStep());
  static Camera topCamera = new Camera("CameraTop");
  static Camera bottomCamera = new Camera("CameraBottom");
  static double dist[] = new double[2];
  static DistanceSensor sonar[] = {new DistanceSensor("Sonar/Left"), new DistanceSensor("Sonar/Right")};
 // sonar[0] = new DistanceSensor("Sonar/Left"); 
 // sonar[1] = new DistanceSensor("Sonar/Right");

 public static void loadMotionFiles(){
    forWard = new Motion(pfad + "Gehen50.motion");
    gehen = new Motion(pfad + "Gehen50Anfang.motion");
    sideStepRight = new Motion(pfad + "SideStepRight.motion");
    turnLeft = new Motion(pfad + "TurnLeft40.motion");
    turnLeft60 = new Motion(pfad + "TurnLeft60.motion");
    turnLeft180 = new Motion(pfad + "TurnLeft180.motion");
    handWave = new Motion(pfad + "HandWave.motion");
    anfall = new Motion(pfad + "Anfall.motion");
    up = new Motion(pfad + "StandUpFromFront.motion");
    bauch = new Motion(pfad + "bauch.motion");
  }
  
   static void startMotion(Motion motion){
    motion.play();
    do {
    robot.step(timeStep);
    } while(! motion.isOver());
    
  }
  
  static void printDistanceSensor(){
    dist[0] = sonar[0].getValue();
    dist[1] = sonar[1].getValue();
    
    //System.out.println("-------Sonar--------");
    //System.out.println("left: " + dist[0]+ " m, right: " + dist[1] + " m\n");
    
  }
  /*
  static void move(){
    printDistanceSensor();
    boolean check = false; // Falls der weg nach einer linksdrehung immer noch verspärt sein sollte. Eine 180 grad drehung 
    int sensor;
    if (check) {
            startMotion(turnLeft180); 
            check = false;
          }
    for (sensor = 0; sensor < 2; sensor++){
      if (dist[sensor] < 0.55){  //ENTFERNUNG IN METER
            startMotion(turnLeft);
            startMotion(turnLeft60);
            check = true;
      }
      else {
        startMotion(forWard);
        check = false;
      }
    }
   }
   */
   
   static void move() {
     printDistanceSensor();
     int sensor;
     boolean check = false; //Prüfung ob drehung nötig

     
     //wenn hindernis, dann
     for (sensor = 0; sensor < 2; sensor++) {
       if(dist[sensor] < 0.40) {   //Prüft beide Sensoren auf Hindernisse
         check = true;
       } 
     }
       if (check) {             
         System.out.println("turnLeft");
         startMotion(turnLeft);
         System.out.println("turnLeft60");
         startMotion(turnLeft60);
       } else {
         System.out.println("forWard");
         //startMotion(gehen);
         startMotion(forWard); //geradeaus;       //
       }    
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
    loadMotionFiles();
    
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
      move();
      // Process sensor data here.

      // Enter here functions to send actuator commands, like:
      //  led.set(1);
    };

    // Enter here exit cleanup code.
  }
}
