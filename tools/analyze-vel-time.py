import numpy as np
import matplotlib.pyplot as plt

vel = np.array([2, 5, 8, 12, 18, 25])
time = np.array([0, 2, 4, 6, 8, 10])
accel = np.diff(vel, 1) / np.diff(time, 1)

poly = np.polyfit(time, vel, 4)
time_smooth = np.linspace(0, 10, 100)

vel_fit = np.poly1d(poly)
vel_deriv = vel_fit.deriv()

time_poly = np.linspace(0, 10, 100)
accel_poly = vel_deriv(time_poly)

plt.scatter(time, vel, color='red')
plt.plot(time_smooth, np.polyval(poly, time_smooth))
plt.plot(accel, color='magenta')
plt.plot(time_poly, accel_poly, color='brown')
plt.show()
