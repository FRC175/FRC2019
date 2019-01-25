"""
Ripped from the Cheesy Poofs.

@author Arvind
"""

import numpy as np

import matplotlib as mpl
import matplotlib.pyplot as plt
import matplotlib.cbook as cbook

data = np.genfromtxt('telemetry.csv', delimiter=',', names=True)

fig = plt.figure()

outputPlot = fig.add_subplot(1, 1, 1)
outputPlot.plot(data['time'], data['position'], label='Position vs Time')

plt.show()