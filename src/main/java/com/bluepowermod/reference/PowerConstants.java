package com.bluepowermod.reference;

public class PowerConstants {
    public static final double POWER_PER_ACTION       = 1; //Value used by Sorting Machine, Manager, Retriever. when a stack is pulled or inserted this amount of energy will be used.
    public static final double SOLAR_PANEL_MULTIPLIER = 0.001D;//Value multiplied by the amount of sunlight that the solar panel generates per tick.
    public static final double THERMOPILE_MULTIPLIER  = 0.000001D;//Value multiplied by the amount of temperature difference that the thermopile
    // generates per tick

    public static final double CHARGINGBENCH_CHARGING_TRANSFER  = 2.0; //The rate at which the charging bench fills up IRechargeable.
    public static final double CHARGINGBENCH_POWERTRANSFER      = 2.0; //The rate at which the internal buffer of the charging bench fills up

    public static final double BATTERYBOX_CHARGING_TRANSFER         = 1.0; //The rate at which the batterybox fills up a battery.
    public static final double BATTERYBOX_DISCHARGING_TRANSFER      = 1.0; //The rate at which the batterybox drains a battery.
    public static final double BATTERYBOX_POWERTRANSFER_CHARGING    = 1.0; //The rate at which the batterybox fills up it's internal buffer.
    public static final double BATTERYBOX_POWERTRANSFER_DISCHARGING = 1.0; //The rate at which the batterybox gives his power back to the network.
}
