
/* Code for Assignment ?? 
 * Name:
 * Usercode:
 * ID:
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.*;

/** <description of class Main>
 */
public class Main{

    private Arm arm;
    private Drawing drawing;
    private ToolPath tool_path;
    // state of the GUI
    private int state; // 0 - nothing
    // 1 - inverse point kinematics - point
    // 2 - enter path. Each click adds point  
    // 3 - enter path pause. Click does not add the point to the path

    /**      */
    public Main(){
        UI.initialise();
        UI.addButton("xy to angles", this::inverse);
        UI.addButton("Enter path XY", this::enter_path_xy);
        UI.addButton("Save path XY", this::save_xy);
        UI.addButton("Load path XY", this::load_xy);
        UI.addButton("Save path Ang", this::save_ang);
        UI.addButton("Save path PWM", this::save_pwm);
        UI.addButton("Load path Ang:Play", this::load_ang);
        UI.addButton("", null);

        UI.addButton("Draw Line", this::drawLine);
        UI.addButton("Draw Square", this::drawSquare);    
        UI.addButton("Draw Circle", this::drawCircle);

        UI.addButton("Send to Pi", this::sendToPi);

        // UI.addButton("Quit", UI::quit);
        UI.setMouseMotionListener(this::doMouse);
        UI.setKeyListener(this::doKeys);

        this.tool_path = new ToolPath();

        //ServerSocket serverSocket = new ServerSocket(22); 
        this.arm = new Arm();
        this.drawing = new Drawing();
        this.run();
        arm.draw();
    }

    public void doKeys(String action){
        UI.printf("Key :%s \n", action);
        if (action.equals("b")) {
            // break - stop entering the lines
            state = 3;
            //

        }

    }

    public void doMouse(String action, double x, double y) {
        //UI.printf("Mouse Click:%s, state:%d  x:%3.1f  y:%3.1f\n",
        //   action,state,x,y);
        UI.clearGraphics();
        String out_str=String.format("%3.1f %3.1f",x,y);
        UI.drawString(out_str, x+10,y+10);
        // 
        if ((state == 1)&&(action.equals("clicked"))){
            // draw as 

            arm.inverseKinematic(x,y);
            arm.draw();
            return;
        }

        if ( ((state == 2)||(state == 3))&&action.equals("moved") ){
            // draw arm and path
            arm.inverseKinematic(x,y);
            arm.draw();

            // draw segment from last entered point to current mouse position
            if ((state == 2)&&(drawing.get_path_size()>0)){
                PointXY lp = new PointXY();
                lp = drawing.get_path_last_point();
                //if (lp.get_pen()){
                UI.setColor(Color.GRAY);
                UI.drawLine(lp.get_x(),lp.get_y(),x,y);
                // }
            }
            drawing.draw();
        }

        // add point
        if (   (state == 2) &&(action.equals("clicked"))){
            // add point(pen down) and draw
            UI.printf("Adding point x=%f y=%f\n",x,y);
            drawing.add_point_to_path(x,y,true); // add point with pen down

            arm.inverseKinematic(x,y);
            arm.draw();
            drawing.draw();
            drawing.print_path();
        }

        if (   (state == 3) &&(action.equals("clicked"))){
            // add point and draw
            //UI.printf("Adding point x=%f y=%f\n",x,y);
            drawing.add_point_to_path(x,y,false); // add point wit pen up

            arm.inverseKinematic(x,y);
            arm.draw();
            drawing.draw();
            drawing.print_path();
            state = 2;
        }

    }

    public void save_xy(){
        state = 0;
        String fname = UIFileChooser.save();
        drawing.save_path(fname);
    }

    public void enter_path_xy(){
        state = 2;
    }

    public void inverse(){
        state = 1;
        arm.draw();
    }

    public void load_xy(){
        state = 0;
        String fname = UIFileChooser.open();
        drawing.load_path(fname);
        drawing.draw();

        arm.draw();
    }

    //drawing methods
    public void drawLine(){
        state = 2;
        //double y = 242.0;
        //double x1 = 288.0;
        //         double x2 = 300.0;
        //         double x3 = 315.0;
        //         double x4 = 331.0;
        //         this.doMouse("clicked", x1, y);
        //         this.doMouse("clicked", x2, y);
        //         this.doMouse("clicked", x3, y);
        //         this.doMouse("clicked", x4, y);

        double y = 194.0;
        double x1 = 286.0;
        double x2 = 348.0;

        this.doMouse("clicked", x1, y);
        this.doMouse("clicked", x2, y);
    }

    public void drawSquare(){
        state = 2;
        double x1 = 296.0;
        double y1 = 234.0;
        double x2 = 343.0;
        double y2 = 234.0;
        double x3 = 343.0;
        double y3 = 275.0;
        double x4 = 294.0;
        double y4 = 275.0;
        double x5 = 294.0;
        double y5 = 234.0;
        this.doMouse("clicked", x1, y1);
        this.doMouse("clicked", x2, y2);
        this.doMouse("clicked", x2, y2); //double up
        this.doMouse("clicked", x3, y3);
        this.doMouse("clicked", x3, y3); //double up
        this.doMouse("clicked", x4, y4);
        this.doMouse("clicked", x4, y4); //double up
        this.doMouse("clicked", x5, y5);        
        this.doMouse("clicked", x5, y5); //double up
    }

    public void drawCircle(){
        state = 2;

        //         double r = 55.5;
        //         double x1 = 277.0;
        //         double y1 = 266.0;
        //         double x2 = 388.0;
        //         double y2 = 266.0;
        // 
        //         double topHalfX1 = Math.sqrt((Math.pow(r,2) - Math.pow(x1,2))); //first x ordinate
        //         double topHalfY1 = Math.sqrt(( Math.pow(r,2) - Math.pow(y1,2))); //first y ordinate
        //         double topHalfX2 = Math.sqrt((Math.pow(r,2) - Math.pow(x2,2))); //second x ordinate
        //         double topHalfY2 = Math.sqrt(( Math.pow(r,2) - Math.pow(y2,2))); //second y ordinate

        //this.doMouse("clicked", topHalfX1, topHalfY1); //draws from first ordinates
        //this.doMouse("clicked", topHalfX2, topHalfY2); //draws to second ordinates

        double y = 200;
        //double yNegative = 200;
        double x0 = 330;
        double y0 = 200;
        double r = 34;

        //         for(int i = 296; i < 365; i += 2){ //draws bottom half
        //             y = Math.sqrt( Math.pow(r,2) - Math.pow((i-x0),2) + y0) + 200;
        //             this.doMouse("clicked", i, y);
        //         }

        for(int i = 364; i > 295; i -= 6){ //draws top half
            y = -1*(Math.sqrt( Math.pow(r,2) - Math.pow((i-x0),2))) + y0;
            this.doMouse("clicked", i, y);
        }

        for(int i = 296; i < 365; i += 6){ //draws over bottom half
            y = Math.sqrt(Math.pow(r,2) - Math.pow((i-x0),2)) + 200;
            this.doMouse("clicked", i, y);
        }
    }
    // save angles into the file
    public void save_ang(){
        String fname = UIFileChooser.open();
        tool_path.convert_drawing_to_angles(drawing,arm,fname);
    }

    public void save_pwm(){
        String fname = UIFileChooser.open();
        tool_path.convert_angles_to_pwm(fname);
    }

    public void load_ang(){
    }

    public void run() {
        while(true) {
            arm.draw();
            UI.sleep(20);
        }
    }

    public void sendToPi() {
        try{
            Process p = Runtime.getRuntime().exec("pscp -l pi -pw pi pathLine.txt pi@10.140.153.83:/home/pi/Arm/");
            UI.println("Sent");
			//10.140.153.16
        }catch(Exception e){
            UI.println(e);
        }
    }

    public static void main(String[] args){
        Main obj = new Main();
    }    

}
