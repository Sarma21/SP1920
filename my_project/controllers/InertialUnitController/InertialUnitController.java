import com.cyberbotics.webots.controller.*;

public class InertialUnitController {
    static Robot robot = new Robot();
    static int timeStep = (int) Math.round(robot.getBasicTimeStep());
    static double[] val;
    // simulated devices
    static InertialUnit iu;  // inertialunit
    
    public static void main(String[] args) {
        iu = new InertialUnit("inertial unit");

        iu.enable(timeStep);

        while (robot.step(timeStep) != -1) {
            val = iu.getRollPitchYaw();
            //Auslesen
            System.out.println("Roll: " + val[0] + "\tPitch: " + val[1] + "\tYaw: " + val[2]);
        };
    }
}
