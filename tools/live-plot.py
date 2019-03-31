from paramiko import SSHClient
from matplotlib import style
import matplotlib.pylot as plt
import matplotlib.animation as animation
import os

ssh = SSHClient()
usb_rio_ip = "172.22.11.2"
wireless_rio_ip = "10.1.75.2"

ssh.load_system_host_keys()

# Automatically connect to right IP
if os.system("ping -c 1 " + usb_rio_ip) == 0:
    ssh.connect(wireless_rio_ip, "lvuser")
else:
    ssh.connect(usb_rio_ip, "lvuser")

style.use('fivethirtyeight')
# style.use('dark_background')

fig = plt.figure()
ax1 = fig.add_subplot(1,1,1)


def animate(i):
    # graph_data = open('example.txt', 'r').read()
    # lines = graph_data.split('\n')
    stdin, stdout, stderr = ssh.exec_command('cat /home/lvuser/csvlog/telemetry.csv')
    lines = stdout.readlines()
    xs = []
    ys = []
    for line in lines:
        if len(line) > 1:
            x, y = line.split(',')
            xs.append(float(x))
            ys.append(float(y))
    ax1.clear()
    ax1.plot(xs, ys)


ani = animation.FuncAnimation(fig, animate, interval=1000)
plt.show()

