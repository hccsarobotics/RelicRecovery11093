/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;
/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class HardwareRecoverer
{
    /* Public OpMode members. */
    public DcMotor  ldFront  = null;
    public DcMotor  rdFront  = null;
    public DcMotor  ldBack   = null;
    public DcMotor  rdBack   = null;
    public DcMotor  arm      = null;
    public Servo    leftClaw    = null;
    public Servo    rightClaw   = null;
    public Servo    jewelArm    = null;
    public Servo    boot        = null;
    //public BNO055IMU NineDOF = null;

    public ColorSensor boardColorSensor = null;
    private static Boolean onBlueBoard = Boolean.FALSE;

    public ColorSensor ballColorSensor = null;
    private static Boolean ballIsBlue = Boolean.FALSE;

    public void setBoardBlue(Boolean color) {
        onBlueBoard = color;
    }

    public Boolean isBoardBlue() {
        return onBlueBoard;
    }

    public Boolean isBallBlue() { return ballIsBlue; }

    public void setBallBlue(Boolean color) {
        ballIsBlue = color;
    }



    public static final double MID_SERVO       =  0.6 ;
    public static final double ARM_UP_POWER    =  0.25 ;
    public static final double ARM_DOWN_POWER  = -0.25 ;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareRecoverer(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        //Define and Initialize Motors
        ldFront  = hwMap.get(DcMotor.class, "ldFront");
        rdFront = hwMap.get(DcMotor.class, "rdFront");
        ldBack    = hwMap.get(DcMotor.class, "ldBack");
        rdBack = hwMap.get(DcMotor.class, "rdBack");

        arm = hwMap.get(DcMotor.class, "arm");

        //NineDOF = hwMap.get(BNO055IMU.class, "imu");

        ldFront.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rdFront.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        ldBack.setDirection(DcMotor.Direction.REVERSE);
        rdBack.setDirection(DcMotor.Direction.REVERSE);
        arm.setDirection(DcMotor.Direction.REVERSE);


        // Set all motors to zero power
        ldFront.setPower(0);
        rdFront.setPower(0);
        ldBack.setPower(0);
        rdBack.setPower(0);
        arm.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        ldFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rdFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ldBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rdBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        ldFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rdFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ldBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rdBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Define and initialize ALL installed servos.
        leftClaw  = hwMap.get(Servo.class, "left_hand");
        rightClaw = hwMap.get(Servo.class, "right_hand");
        jewelArm = hwMap.get(Servo.class, "jewel_Arm");
        boot = hwMap.get(Servo.class, "boot");
        leftClaw.setPosition(MID_SERVO);
        rightClaw.setPosition(MID_SERVO);
        jewelArm.setPosition(1);
        boot.setPosition(0);

        boardColorSensor = hwMap.get(ColorSensor.class, "board_cs");
        ballColorSensor = hwMap.get(ColorSensor.class, "jewel_cs");



    }
 }

