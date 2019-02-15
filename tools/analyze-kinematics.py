"""
A simple script that analyzes velocity data and calculates acceleration and jerk.

@author Arvind
"""

import numpy as np
import math as m

# Main function
def main():
    # File Path
    filePath = 'C:/Users/BUZZ-175/RoboRIOCSVLogs/telemetry.csv'
    # filePath = 'D:/Inbox/telemetry.csv'
    # Import data from CSV
    data = np.genfromtxt(filePath, delimiter=',', names=True)

    # Make arrays from data
    leftVels = data['left_velocity']
    rightVels = data['right_velocity']
    time = data['time']

    # Find max velocity in velocity array
    leftMaxVel = max(leftVels)
    rightMaxVel = max(rightVels)

    # Make acceleration array from dv/dt
    leftAccels = np.diff(leftVels, n=1) / np.diff(time, n=1)
    rightAccels = np.diff(rightVels, n=1) / np.diff(time, n=1)
    print("Left Acceleration Array:", leftAccels)
    print("Right Acceleration Array:", rightAccels)

    # Find max acceleartion from acceleration array
    leftMaxAccel = max(leftAccels)
    rightMaxAccel = max(rightAccels)

    # Make jerk array from da/dt
    leftJerks = np.diff(leftVels, n=2) / np.diff(time[0:(len(time) - 1)]) # Check if time splice makes sense
    rightJerks = np.diff(leftVels, n=2) / np.diff(time[0:(len(time) - 1)]) # Check if time splice makes sense
    print("Left Jerk Array:", leftJerks)
    print("Right Jerk Array:", rightJerks)

    # Find max jerk from jerk array
    leftMaxJerk = max(leftJerks)
    rightMaxJerk = max(rightJerks)

    # Print out values
    print("Left Max Velocity:", convertUnits(leftMaxVel))
    print("Left Max Acceleration:", convertUnits(leftMaxAccel))
    print("Left Max Jerk:", convertUnits(leftMaxJerk))

    print("Right Max Velocity:", convertUnits(rightMaxVel))
    print("Right Max Acceleration:", convertUnits(rightMaxAccel))
    print("Right Max Jerk:", convertUnits(rightMaxJerk))
    
    # print("Left Max Velocity:", leftMaxVel)
    # print("Left Max Acceleration:", leftMaxAccel)
    # print("Left Max Jerk:", leftMaxJerk)

    # print("Right Max Velocity:", rightMaxVel)
    # print("Right Max Acceleration:", rightMaxAccel)
    # print("Right Max Jerk:", rightMaxJerk)

# Convert value from counts/100 ms to in/s
def convertUnits(value):
    return (25 * m.pi * value) / 4096 # 25 = 10 ms (to convert into seconds) * 2.5 in (for cim motor)

if __name__ == '__main__':
    main()
