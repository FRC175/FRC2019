import numpy as np

filePath = 'C:/Users/BUZZ-175/RoboRIOCSVLogs/velocity-data.csv'
data = np.genfromtxt(filePath, delimiter=',', names=True)

vel = data['velocity']
time = data['time']
maxVel = max(vel)

# Use polynomial regression to generate v(t) equation
velFit = np.polyfit(time, vel, 3)
smoothTime = np.polyfit(time[0], time[-1], 100) # smoothened time values for plot

# Generate acceleration from derivative of velocity fit
velPoly1D = np.poly1d(velFit)
accel = (velPoly1D.deriv())(time)

# Graph everything
plt.scatter(time, vel, color='red') # vel vs. time points
plt.plot(smoothTime, np.polyval(velFit, smoothTime), color='blue') # v(t) fit
plt.plot(smoothTime, accel, color='magenta') # a(t) -> derivative of v(t) fit
plt.show()







