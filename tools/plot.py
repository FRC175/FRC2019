import numpy as np

import matplotlib.pyplot as plt

filePath = 'C:/Users/BUZZ-175/RoboRIOCSVLogs/tuning-data.csv'

data = np.genfromtxt(filePath, delimiter=',', names=True)

plt.plot(data['time'], data['elevator_position'], data['elevator_wanted_position'])
plt.xlabel('time (s)')
plt.ylabel('position (counts)')
plt.title('position vs. time')

plt.show()
