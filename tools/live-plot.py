from paramiko import SSHClient
from matplotlib.pylot as plt
from matplotlib.animation as animation
from matplotlib import style

ssh = SSHClient()
usbRIOIP = "172.22.11.2"
wirelessRIOIP = "10.1.75.2"

ssh.load_system_host_keys()

# Automatically connect to right IP
if os.system("ping -c 1 " + usbRIOIP) == 0:
    ssh.connect(wirelessRIOIP, "lvuser")
else:
    ssh.connect(usbRIOIP, "lvuser")

style.use('fivethirtyeight')
#style.use('dark_background')

fig = plt.figure()
