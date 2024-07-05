#Python file that reads DS18B20 type temp sensors
import os
import glob
import time


os.system('modprobe w1-gpio')
os.system('modprobe w1-therm')


base_dir = '/sys/bus/w1/devices/'


def read_temp_raw(sensor_address):
    device_folder = glob.glob(base_dir + sensor_address)[0]
    device_file = device_folder + '/w1_slave'
    f = open(device_file, 'r')
    lines = f.readlines()
    f.close()
    return lines

#call this one to get the temp
def read_temp(sensor_address):
    print("Trying to read DS18B20 at address: " + sensor_address)
    lines = read_temp_raw(sensor_address)
    while lines[0].strip()[-3:] != 'YES': #retry if the first call didn't make sense
        time.sleep(0.2)
        lines = read_temp_raw(sensor_address)
    equals_pos = lines[1].find('t=')
    if equals_pos != -1:
        temp_string = lines[1][equals_pos+2:]
        temp_c = float(temp_string) / 1000.0
        temp_f = temp_c * 9.0 / 5.0 + 32.0
        #return temp_c, temp_f
        return temp_f
