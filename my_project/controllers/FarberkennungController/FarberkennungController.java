import com.cyberbotics.webots.controller.*;
import java.io.File;

public class FarberkennungController {
    static Robot robot = new Robot();
    static int timeStep = (int) Math.round(robot.getBasicTimeStep());
    static int[] imageTop, imageBottom;
    static Camera cameraTop, cameraBottom;
    static Motion forward,gehen,gehen50,gehen50anf,gehen50ende;
    static Motion currentlyPlaying = null;
    //rgb init
    static int r = 0;
    static int g = 0;
    static int b = 0;
    
    static void startMotion(Motion motion) {
      currentlyPlaying = motion;
      currentlyPlaying.play();
      do {
        robot.step(timeStep);
      } while (!currentlyPlaying.isOver());
    }
    
    static void loadMotionsFiles(){
      File file = new File(System.getProperty("user.dir"));
      file = (file.getParentFile().getParentFile());
      String pfad = file.getPath() + System.getProperty("file.separator") + "motions" + System.getProperty("file.separator");
      forward = new Motion(pfad + "Forwards.motion");
      gehen = new Motion(pfad + "Gehen.motion");
      gehen50 = new Motion(pfad + "Gehen50.motion");
      gehen50anf = new Motion(pfad + "Gehen50Anfang.motion");
      gehen50ende = new Motion(pfad + "Gehen50Ende.motion");
    }
    //RGB Bild auswertung
    static void getColour(){
      int[] image = cameraBottom.getImage();
      for (int i=0; i < image.length; i++){
        int pixel = image[i];
        r = r + Camera.pixelGetRed(pixel);
        g = g + Camera.pixelGetGreen(pixel);
        b = b + Camera.pixelGetBlue(pixel);
      }
      r = r/image.length;
      g = g/image.length;
      b = b/image.length;
      System.out.println("red=" + r + "   green=" + g + "   blue="+ b);
    }

  public static void main(String[] args) {
    
   loadMotionsFiles();
    
    cameraTop = new Camera("CameraTop");
    cameraBottom = new Camera("CameraBottom");
    cameraTop.enable(4*timeStep);
    cameraBottom.enable(4*timeStep);
    while (robot.step(timeStep) != -1) {
        while(r < 225){
          startMotion(gehen50);
          getColour();
        }
        
    };
  }
}