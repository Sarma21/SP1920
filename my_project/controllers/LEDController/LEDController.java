import com.cyberbotics.webots.controller.*;

public class LEDController {
    static Robot robot = new Robot();
    static int timeStep = (int) Math.round(robot.getBasicTimeStep());
    // simulated devices
    static LED[] leds = new LED[7];
    
    static void setAllLedsColor(int rgb) {
      // these leds take RGB values
      for (int i = 0; i < 5; i++)
        leds[i].set(rgb);
    
      // ear leds are single color (blue)
      // and take values between 0 - 255
      leds[5].set(rgb & 0xff);
      leds[6].set(rgb & 0xff);
    }
    
    public static void main(String[] args) {
        // There are 7 controlable LED groups in Webots
        leds[0] = new LED("ChestBoard/Led");
        leds[1] = new LED("RFoot/Led");
        leds[2] = new LED("LFoot/Led");
        leds[3] = new LED("Face/Led/Right");
        leds[4] = new LED("Face/Led/Left");
        leds[5] = new LED("Ears/Led/Right");
        leds[6] = new LED("Ears/Led/Left");
        
        setAllLedsColor(0xff0000);  // red
        //setAllLedsColor(0x00ff00);  // green
        //setAllLedsColor(0x0000ff);  // blue
        //setAllLedsColor(0x000000);  // off
        
        while (robot.step(timeStep) != -1) {
        
        };
    }
}