try:
    import RPi.GPIO as GPIO
except:
    print("Unable to load GPIO lib; this is probaly not a RaspberryPi. Continuing without GPIO support.")


#consturctor
#pins = list of ints for pins that we'll be using
def setup(pins):
    try:
        #use board numbering, because it's less confusing
        GPIO.setmode(GPIO.BOARD)
        #only supporting output at this time (ie, relays or other binary outputs); default to off
        for pin in pins:
            print("Trying to setup pin " + str(pin) + "...")
            if pin["invert"]:
                GPIO.setup(pin["pin"], GPIO.OUT, initial=GPIO.HIGH)
            else:
                GPIO.setup(pin["pin"], GPIO.OUT, initial=GPIO.LOW)

    except NameError:
        print("Unable to setup GPIO; this is probaly not a RaspberryPi. Continuing without GPIO support.")

def getPinState(pin):
    print("Getting state for GPIO pin: " + str(pin))
    if GPIO.input(int(pin)):
        state = True
    else:
        state = False
    print("State for pin: " + str(pin) + " is: " + str(state))
    return state

def setPinState(pin, value):
    print("Setting state for GPIO pin: " + str(pin) + ", value: " + str(value))
    if value:
        GPIO.output(int(pin), GPIO.HIGH)
    else:
        GPIO.output(int(pin), GPIO.LOW)
