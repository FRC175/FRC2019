import numpy as np

import matplotlib.pyplot as plt

filePath = 'C:/Users/BUZZ-175/RoboRIOCSVLogs/telemetry.csv'

data = np.genfromtxt(filePath, delimiter=',', names=True)

plt.plot(data['time'], data['left_position'], data['wanted_position'])
plt.xlabel('time (s)')
plt.ylabel('position (counts)')
plt.title('position vs. time')

plt.show()
