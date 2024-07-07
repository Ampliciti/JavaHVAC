#!/usr/bin/python3

#listens for a momentary push button switch to change state in order to open an air valve. This can be easily modified for other similar purposes.
#requirements: apt-get install python-requests
#to use: python air_switch_watcher.py $GPIO_PIN $bouncetime
#my current setup is using GPIO pin 40 with bouncetime of 800ms

import RPi.GPIO as GPIO
import time
import datetime
import requests
from requests.auth import HTTPDigestAuth
import json
import sys


GPIO.setmode(GPIO.BOARD)

switch_one_state=0;
global GPIO_PIN
GPIO_PIN=int(sys.argv[1])
#38

def main():
    global GPIO_PIN
    print("Flood switch listener starting up for pin: " + str(GPIO_PIN) + "..." + str(datetime.datetime.now()))
    GPIO.setmode(GPIO.BOARD)
    GPIO.setup(GPIO_PIN, GPIO.IN, pull_up_down=GPIO.PUD_UP)
    global switch_one_state
    switch_one_state=GPIO.input(GPIO_PIN)
    print("Switch one is: " + str(switch_one_state))
    print("Air switch listener started successfully.")
    sys.stdout.flush()
    GPIO.add_event_detect(GPIO_PIN, GPIO.RISING, callback=stateChange, bouncetime=int(sys.argv[2]))
    # while True:
    #     print (GPIO.input(GPIO_PIN))
    #     time.sleep(1)
    while True:
        time.sleep(1)

    GPIO.cleanup()

def stateChange(switch_state):
    print("["+str(datetime.datetime.now())+"]Switch state: " + str(switch_state))
    sys.stdout.flush()
    #make sure we didn't just pick up some random interfearence
    global switch_one_state
    global GPIO_PIN
    counter=0
    while(counter < 5):
        print("Counter: " + str(counter))
    if(switch_state == GPIO_PIN):
        print("GPIO_PIN: " + str(GPIO_PIN))
        print("GPIO.input(switch_state): " + str(GPIO.input(GPIO_PIN)))
        print("switch_state: " + str(switch_state))
    if(GPIO.input(GPIO_PIN) != 1):
        print("Just detected noise on switch one; ignoring.")
        return
    counter+=1
    time.sleep(0.1)

    #ok, we've confirmed it's not noise
    #set the new state
    if(switch_state == GPIO_PIN):
        switch_one_state = GPIO.input(switch_state)
        print("["+str(datetime.datetime.now())+"]Switch one activated.")
        current_state = checkCurrentState()
        print("current state: " + str(current_state))
        new_state = False
        if current_state == False:
            new_state = True
            print("New state: " + str(new_state))
            postNewState(new_state)

    sys.stdout.flush()

def postNewState(new_state):
    this_node_state_url="http://home.lan/zone"
    res = requests.put(this_node_state_url, '{\"name\": \"mainAir\", \"state\": \"'+str(new_state)+'\"}')
    if(res.ok):
        print("successfully turned air: " + str(new_state))
    else:
        print("could not turn air: " + str(new_state))
        print(str(res))


def checkCurrentState():
    this_node_state_url="http://home.lan/status"
    res = requests.get(this_node_state_url)
    if(res.ok):
        current_state = None
        jData = json.loads(res.content)
        #print(str(jData))
        nodes = jData['nodeState']
        #print(str(nodes))
        for node in nodes:
            if node['name'] == "barn-pi":
                for zone in node['zones']:
                    if zone['name'] == "mainAir":
                        current_state = zone['state']
                        break
        if current_state is None:
            print("Cannot find barn control pi")
            return current_state;
    else:
        print("Cannot connect to server or cannot find myself registered.")


if __name__ == "__main__": main()
