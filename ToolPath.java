
/**
 * ToolPath stores motor contol signals (pwm)
 * and motor angles
 * for given drawing and arm configuration.
 * Arm hardware takes sequence of pwm values 
 * to drive the motors
 * @Arthur Roberts 
 * @1000000.0
 */
import ecs100.UI;
import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ToolPath
{
    int n_steps; //straight line segmentt will be broken
    // into that many sections

    // storage for angles and 
    // moto control signals
    ArrayList<Integer> theta1_vector;
    ArrayList<Integer> theta2_vector;
    ArrayList<Integer> pen_vector;
    ArrayList<Integer> pwm1_vector;
    ArrayList<Integer> pwm2_vector;
    ArrayList<Integer> pwm3_vector;

    /**
     * Constructor for objects of class ToolPath
     */
    public ToolPath()
    {
        // initialise instance variables
        n_steps = 50;
        theta1_vector = new ArrayList<Integer>();
        theta2_vector = new ArrayList<Integer>();
        pen_vector = new ArrayList<Integer>();
        pwm1_vector = new ArrayList<Integer>();
        pwm2_vector = new ArrayList<Integer>();
        pwm3_vector = new ArrayList<Integer>();

    }

    /**********CONVERT (X,Y) PATH into angles******************/
    public void convert_drawing_to_angles(Drawing drawing,Arm arm,String fname){

        // for all points of the drawing...        
        for (int i = 1;i < drawing.get_drawing_size();i++){ 
            // take two points
            PointXY p0 = drawing.get_drawing_point(i-1);
            PointXY p1 = drawing.get_drawing_point(i);
            // break line between points into segments: n_steps of them
            for ( int j = 0 ; j< n_steps;j++) { // break segment into n_steps str. lines
                double x = p0.get_x() + j*(p1.get_x()-p0.get_x())/n_steps;
                double y = p0.get_y() + j*(p1.get_y()-p0.get_y())/n_steps;
                arm.inverseKinematic(x, y);
                theta1_vector.add((int)(10.183*((-1)*arm.get_theta1()*180/Math.PI) + 234.85));
                theta2_vector.add((int)(9.92*((-1)*arm.get_theta2()*180/Math.PI) + 870.63));

				//new calibration equations (not original arm)
                //theta1_vector.add((int)(10.17*((-1)*arm.get_theta1()*180/Math.PI) - 35.686));
				//theta2_vector.add((int)(10.02*((-1)*arm.get_theta2()*180/Math.PI) + 1000 - 92.379));

                //theta1_vector.add(arm.get_theta1()*180/Math.PI);
                //theta2_vector.add(arm.get_theta2()*180/Math.PI);

                if (p1.get_pen()){ 
                    pen_vector.add(2000);
                } else {
                    pen_vector.add(1000);
                }
            }
        }
        //convert_angles_to_pwm(fname);
        save_angles(fname);
        UI.print("completed convert_angles_to_pwm(fname)\n");
    }

    public void save_angles(String fname){
        //         for ( int i = 0 ; i < theta1_vector.size(); i++){
        //             UI.printf(" t1=%3.1f t2=%3.1f pen=%d\n",
        //                 theta1_vector.get(i),theta2_vector.get(i),pen_vector.get(i));
        //         }

        try {
            //Whatever the file path is.
            File statText = new File(fname);
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            String str_out;
            for (int i = 1; i < theta1_vector.size() ; i++){
                str_out = String.format("%d,%d,%d\n",
                    theta1_vector.get(i),theta2_vector.get(i),pen_vector.get(i));
                w.write(str_out);
            }
            w.close();
        } catch (IOException e) {
            UI.println("Problem writing to the file statsTest.txt");
        }
        UI.print("completed save_angles\n");
    }

    // takes sequence of angles and converts it 
    // into sequence of motor signals
    public void convert_angles_to_pwm(String fname){
        // for each angle
        for (int i=0 ; i < theta1_vector.size();i++){
            //arm.set_angles(theta1_vector.get(i),theta2_vector.get(i));
            UI.print("in convert_angles_to_pwm\n");
            pwm1_vector.add((int)(10.183*(theta1_vector.get(i)) + 234.85));
            pwm2_vector.add((int)(9.92*(theta2_vector.get(i)) + 870.63));
        }
        save_pwm_file(fname);
        UI.print("completed convert_angles_to_pwm\n");
    }

    // save file with motor control values
    public void save_pwm_file(String fname){
        //         for ( int i = 0 ; i < theta1_vector.size(); i++){
        //          UI.printf(" t1=%3.1f t2=%3.1f pen=%d\n",
        //             theta1_vector.get(i),theta2_vector.get(i),pen_vector.get(i));
        //         }

        try {
            //Whatever the file path is.
            File statText = new File(fname);
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            String str_out;
            for (int i = 1; i < pwm1_vector.size() ; i++){
                str_out = String.format("%d,%d\n",
                    pwm1_vector.get(i),pwm2_vector.get(i)); //check pen vector value later 
                w.write(str_out);
            }
            w.close();
        } catch (IOException e) {
            UI.println("Problem writing to the file statsTest.txt");
        }

    }

}
