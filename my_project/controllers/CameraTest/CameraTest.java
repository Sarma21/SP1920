import com.cyberbotics.webots.controller.*;

public class CameraTest {
  static Robot robot = new Robot();
  static int timeStep = (int) Math.round(robot.getBasicTimeStep());
  
  // simulated devices
  static Camera cameraTop, cameraBottom; //cameras
      
  static void findAndEnableDevices() {
    // camera
    cameraTop = new Camera("CameraTop");
    cameraBottom = new Camera("CameraBottom");
    cameraTop.enable(4*timeStep);
    cameraBottom.enable(4*timeStep);
  }

  static void camCheck(){
    double imageWidth = cameraTop.getNear();
    System.out.println("imageWidth: " + imageWidth);
    int[] imageTop = cameraTop.getImage();    //int[] imageTop
    int[] imageBottom = cameraBottom.getImage(); //int[] imageBottom
    System.out.println("Array: " + imageTop.length);
  }

  public static void main(String[] args) {
    findAndEnableDevices();
    startMotion(handWave);
    camCheck();
    while (robot.step(timeStep) != -1) {

    };
  }
}