try:
    import RPi.GPIO as GPIO
except:
    print "Unable to load GPIO lib; this is probaly not a RaspberryPi. Continuing without GPIO support."


#consturctor
#pins = list of ints for pins that we'll be using
def __init__(self, pins):
    #use board numbering, because it's less confusing
    GPIO.setmode(GPIO.BOARD)
    #only supporting output at this time (ie, relays or other binary outputs); default to off
    GPIO.setup(pins, GPIO.OUT, initial=GPIO.LOW)

def getPinState(pin):
    print "Getting state for GPIO pin: " + pin
    state = GPIO.input(pin)
    print "State for pin: " + pin + " is: " + state
    return state
