#This is the entry point for our RaspberryPi based Python API for communicating sensor data and taking action changing relays/switches/valves/pumps/etc
#Also, this may be the best file name I've ever had in an application.

from flask import Flask
from flask_restful import Api, Resource, request
import json
import ReadDS18B20
import GPIOHelper

app = Flask(__name__)
api = Api(app)

#this probably doesn't follow python best practices, this is my first python rest app like this.

def loadJsonFile(json_file):
    with open(json_file) as json_file:
        return json.load(json_file)

def loadSensorConfig():
    return loadJsonFile('sensor-config.json')['sensors']

def loadServiceConfig():
    return loadJsonFile('service-config.json')

def loadRelayConfig():
    return loadJsonFile('relay-config.json')['relays']

def setupGPIO():
    pins = []
    for relay in relay_config:
        pins.append(int(relay['GPIO']))
    GPIOHelper.setup(pins)

sensor_config = loadSensorConfig()
service_config = loadServiceConfig()
relay_config = loadRelayConfig();

#general server info
name = service_config['name']
address = service_config['address'].split(":")[0]
port = service_config['address'].split(":")[1]

setupGPIO()

#end setup

#helper method for combining two data objects in a list with the same key -- there's probably a better way to do this
def smartAppend(listToAppendTo, key, toAppend):
    #print "list: " + str(listToAppendTo)
    set = False
    for item in listToAppendTo:
        #print item[key] + ":" + toAppend[key]
        if item[key] == toAppend[key]:
            set = True
            item = item.update(toAppend)

    if not set:
        listToAppendTo.append(toAppend)
    return listToAppendTo

def getRelayByName(name):
    for relay in relay_config:
        if relay['name'] == name:
            print str(relay)
            try:
                relay['state'] = GPIOHelper.getPinState(relay['GPIO'])
            except Exception as ex:
                print "Could not read current GPIO state: " + str(relay) + ", " + str(ex)
                relay['state'] = "null"
            return relay

class Sensor(Resource):
    def get(self, name):
        for sensor in sensor_config:
            if(name == sensor["name"]):
                #see if we can actually get a reading on this
                if sensor["model"] == "DS18B20":
                    try:
                        sensor["temp"] = ReadDS18B20.read_temp(sensor["address"])
                    except Exception as ex:
                        print "Could not read sensor temp: " + str(sensor) + ", " + str(ex)
                return sensor, 200
        return "Sensor not found", 404

    def post(self, name):
        return "Mutators are not supported on sensor routes", 405

    def put(self, name):
        return self.post(name)

    def delete(self, name):
        return self.post(name)

class Actor(Resource):
    def get(self, name):
        relay = getRelayByName(name)
        if relay is not None:
            return relay, 200
        return name + " is not a valid actor", 404

    def post(self, name):
        return "Mutators are not supported on actor route. Use /action route.", 405

    def put(self, name):
        return self.post(name)

    def delete(self, name):
        return "Delete is not supported.", 405

class Action(Resource):
    def get(self, name):
        return "GET are not supported on action route. Use /actor route.", 405

    def post(self):
        body = request.get_json(force=True)
        for relay in relay_config:
            exists = False
            print str(relay)
            print body['name']
            if(relay['name'] == body['name']):
                exists = True
                #Actually change the actors state, then return a 201
                try:
                    GPIOHelper.setPinState(relay['GPIO'], body['state']) #probably slightly unsafe
                    return getRelayByName(body['name']), 201
                except Exception as exception:
                    print str(exception)
                    return "Could not complete the action: " + str(exception), 500

        if not exists:
            return "A relay (GPIO output) does not exist with that name.", 404


    def put(self, name):
        return self.post(name)

    def delete(self, name):
        return "Delete is not supported.", 405


class Info(Resource):
    def get(self):
        #start with an empty response that we can build up
        response = {}
        #add our top level stuff
        response['name'] = name
        response['address'] = address + ":" + port
        #start with an empty source, zone, and misc objects
        sources = []
        zones = []
        misc = [] #relays and sensors with nothing associated with them
        for sensor in sensor_config:
            #common to all sensors
            sensorR = {}
            sensorR['name'] = sensor['name']
            try:
                sensorR['temp'] = ReadDS18B20.read_temp(sensor["address"])
            except Exception as ex:
                print "Could not read sensor temp: " + str(sensor) + ", " + str(ex)
                sensorR['temp'] = "null"
            #end common to all sensors
            #source type sensors
            if sensor.get('source') != None:
                sensorR['source'] = sensor['source']
                sources.append(sensorR)
            #zone type sensors
            elif sensor.get('zone') != None:
                sensorR['zone'] = sensor['zone']
                zones.append(sensorR)
            else: #unmanaged sensors
                misc.append(sensorR)

        for relay in relay_config:
            relayR = {}
            print str(relay)
            relayR['name'] = relay['name']
            try:
                relayR['state'] = GPIOHelper.getPinState(relay['GPIO'])
            except Exception as ex:
                print "Could not read current GPIO state: " + str(relay) + ", " + str(ex)
                relayR['state'] = "null"
            #source type relays
            if relay.get('source') != None:
                relayR['source'] = relay['source']
                smartAppend(sources, 'name', relayR)
            #zone type relays
            elif relay.get('zone') != None:
                relayR['zone'] = relay['zone']
                smartAppend(zones, 'name', relayR)
            else: #unmanaged relays
                misc.append(relayR)


        response['sources'] = sources
        response['zones'] = zones
        return response, 200

    def post(self):
        return "Mutators are not supported on /info route", 405

    def put(self):
        return self.post()

    def delete(self):
        return self.post()



api.add_resource(Sensor, "/sensor/<string:name>")
api.add_resource(Actor, "/actor/<string:name>", endpoint="actor")
api.add_resource(Action, "/action", endpoint="action")
api.add_resource(Info, "/info")

#run in non-debug mode, on port <whatever the user set in the config file>, open to all network (not very secure)
app.run(debug=False, port=port, host='0.0.0.0')
