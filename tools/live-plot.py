from paramiko import SSHClient
from matplotlib import style
import matplotlib.pyplot as plt
import matplotlib.animation as animation
import numpy as np
import os

ssh = SSHClient()
usb_rio_ip = "172.22.11.2"
wireless_rio_ip = "10.1.75.2"

ssh.load_system_host_keys()

# Automatically connect to right IP
if os.system("ping -c 1 " + usb_rio_ip) != 0:
    ssh.connect(usb_rio_ip, "lvuser")
else:
    ssh.connect(wireless_rio_ip, "lvuser")

style.use('fivethirtyeight')
# style.use('dark_background')

fig = plt.figure()
ax1 = fig.add_subplot(1,1,1)


def animate(i):
    # For testing purposes
    # graph_data = open('example.txt', 'r').read()
    # data = graph_data.split('\n')

    # Read all lines of CSV
    stdin, stdout, stderr = ssh.exec_command('cat /home/lvuser/csvlog/tuning-data.csv')
    data = stdout.readlines()

    position_arr = []
    wanted_position_arr = []
    time_arr = []

    # Skip headers in processing
    iter_data = iter(data)
    next(iter_data)

    for line in iter_data:
        if len(line) > 1:
            # TODO: Add variables from different headers
            position, wanted_position, time = line.split(',')
            position_arr.append(float(position))
            wanted_position_arr.append(float(wanted_position))
            time_arr.append(float(time))

    ax1.clear()
    ax1.plot(time_arr, position_arr, wanted_position_arr)


ani = animation.FuncAnimation(fig, animate, interval=1000)
plt.xlabel('time (s)')
plt.ylabel('position (counts)')
plt.title('position vs. time')
plt.show()

