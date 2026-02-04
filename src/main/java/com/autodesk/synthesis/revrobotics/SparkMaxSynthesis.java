package com.autodesk.synthesis.revrobotics;

import java.util.ArrayList;

import com.autodesk.synthesis.CANEncoder;
import com.autodesk.synthesis.CANMotor;
// import com.revrobotics.CANSparkBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.REVLibError;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkAbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase;
// import com.revrobotics.RelativeEncoder;


/**
 * CANSparkMax wrapper to add proper WPILib HALSim support.
 */
public class SparkMaxSynthesis extends SparkMax {

    private CANMotor m_motor;
    public CANEncoder m_encoder;
    private ArrayList<SparkMax> followers;

    /**
     * Creates a new CANSparkMax, wrapped with simulation support.
     * 
     * @param deviceId  CAN Device ID.
     * @param motorType Motor type. For Simulation purposes, this is discarded at the
     *                  moment.
     *
     * See original documentation for more information https://codedocs.revrobotics.com/java/com/revrobotics/cansparkmax
     */
    public SparkMaxSynthesis(int deviceId, MotorType motorType) {
        super(deviceId, motorType);

        this.m_motor = new CANMotor("SYN CANSparkMax", deviceId, 0.0, false, 0.3);
        this.m_encoder = new CANEncoder("SYN CANSparkMax", deviceId);
        this.followers = new ArrayList<SparkMax>();
    }

    /**
     * Sets the percent output of the real and simulated motors
     * Setting a follower doesn't break the simulated follower - leader relationship, which it does for exclusively non-simulated motors
     *
     * @param percent The new percent output of the motor
     *
     * See the original documentation for more information 
     */
    @Override
    public void set(double percent) {
        super.set(percent);
        this.m_motor.setPercentOutput(percent);
        for (SparkMax follower : this.followers) {
            follower.set(percent);
        }
    }

    /**
     * Sets the neutralDeadband of the real and simulated motors
     *
     * @param n The new neutral deadband
     */
    void setNeutralDeadband(double n) {
        this.m_motor.setNeutralDeadband(n);
    }

    /**
     * Sets the real and simulated motors to an idle mode
     *
     * @param mode The specific idle mode (Brake, Coast)
     *
     * @return A library error indicating failure or success
     */

    // removed by aadi for now
    // @Override
    // public REVLibError setIdleMode(IdleMode mode) {
    //     if (mode != null)
    //         this.m_motor.setBrakeMode(mode.equals(IdleMode.kBrake));

    //     return super.setIdleMode(mode);
    // }

    /** 
     * Gets a simulation-supported SparkAbsoluteEncoder containing the position and velocity of the motor in fission.
     * All information returned by this class besides position and velocity is from the real motor.
     * Use instead of getAbsoluteEncoder(), everything except for the name of the method works exactly the same.

     * @return The simulation-supported SparkAbsoluteEncoder.
     */
    public SparkAbsoluteEncoder getAbsoluteEncoderSim() {
        // The REV library does not provide a constructor that accepts a real encoder and a CANEncoder,
        // so return the real absolute encoder instance for now.
        return super.getAbsoluteEncoder();
    }

    public RelativeEncoder getEncoderSim() {
        return super.getEncoder();
    }

    /**
     * Adds a follower to this motor controller.
     *
     * @param f The new follower
     */
    void newFollower(SparkMax f) {
        this.followers.add(f);
    }

    /** 
     * Causes a simulation-supported leader to follow another simulation-supported leader.
     * The real versions of these motors will also follow each other.
     *
     * @param leader The motor for this robot to follow
     *
     * @return A library error indicating failure or success
     */

    // removed by aadi for now
    // @Override
    // public REVLibError follow(SparkBase leader) {
    //     REVLibError err = super.follow(leader);
    //     if (leader instanceof SparkMaxSynthesis) {
    //         ((SparkMaxSynthesis) leader).newFollower(this);
    //     }
    //     return err;
    // }
}
