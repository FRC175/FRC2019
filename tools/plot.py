import numpy as np

import matplotlib.pyplot as plt

filePath = 'C:/Users/BUZZ-175/RoboRIOCSVLogs/telemetry.csv'

data = np.genfromtxt(filePath, delimiter=',', names=True)

plt.plot(data['time'], data['position'], data['wanted_position'])
plt.xlabel('time')
plt.ylabel('position')
plt.title('position vs. time')

plt.show()