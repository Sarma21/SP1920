import com.cyberbotics.webots.controller.*;

public class PositionSensorController {
    static Robot robot = new Robot();
    static int timeStep = (int) Math.round(robot.getBasicTimeStep());
    static double pos;
    static int type;

    // simulated devices
    /* alle verfuegbaren Gelenke des NAO */
    static PositionSensor posHeadYawS, posHeadPitchS;
    static PositionSensor RShoulderPitchS, LShoulderPitchS;
    static PositionSensor RShoulderRollS, LShoulderRollS;
    static PositionSensor RElbowYawS, LElbowYawS;
    static PositionSensor RElbowRollS, LElbowRollS;
    static PositionSensor RHipYawPitchS, LHipYawPitchS;
    static PositionSensor RHipRollS, LHipRollS;
    static PositionSensor RHipPitchS, LHipPitchS;
    static PositionSensor RKneePitchS, LKneePitchS;
    static PositionSensor RAnklePitchS, LAnklePitchS;
    static PositionSensor RAnkleRollS, LAnkleRollS;

    public static void main(String[] args) {
        posHeadPitchS = new PositionSensor("HeadPitchS");
      
        posHeadPitchS.enable(timeStep);
        //int rotational = PositionSensor.ROTATIONAL; //80
        //int linear = PositionSensor.LINEAR; //1
        
        while (robot.step(timeStep) != -1) {
            pos = posHeadPitchS.getValue();
            type = posHeadPitchS.getType();
       
            System.out.println("Typ: " + type + "\tPosition HeadPitchS: " + pos);
        };
    }
}