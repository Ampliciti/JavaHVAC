#This is the entry point for our RaspberryPi based Python API for communicating sensor data and taking action changing relays/switches/valves/pumps/etc
#Also, this may be the best file name I've ever had in an application.

from flask import Flask
from flask_restful import Api, Resource, reqparse
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
        pins.append(relay['GPIO'])
    GPIOHelper.setup(pins)

sensor_config = loadSensorConfig()
service_config = loadServiceConfig()
relay_config = loadRelayConfig();

#general server info
name = service_config['name']
address = service_config['address'].split(":")[0]
port = service_config['address'].split(":")[1]

setupGPIO()



actors = [
    {
        "name": "Nicholas",
        "age": 42,
        "occupation": "Network Engineer"
    },
    {
        "name": "Elvin",
        "age": 32,
        "occupation": "Doctor"
    },
    {
        "name": "Jass",
        "age": 22,
        "occupation": "Web Developer"
    }
]


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
        for actor in actors:
            if(name == actor["name"]):
                return actor, 200
        return "Actor not found", 404

    def post(self, name):
        parser = reqparse.RequestParser()
        parser.add_argument("age")
        parser.add_argument("occupation")
        args = parser.parse_args()

        for actor in actors:
            if(name == actor["name"]):
                return "User with name {} already exists".format(name), 400

        actor = {
            "name": name,
            "age": args["age"],
            "occupation": args["occupation"]
        }
        actors.append(actor)
        return user, 201

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
        #start with an empty source and zone objects
        sources = []
        zones = []
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
            if sensor['source'] != None:
                sensorR['source'] = sensor['source']
                sources.append(sensorR)
            #zone type sensors
            elif sensor['zone'] != None:
                sensorR['zone'] = sensor['zone']
                zones.append(sensorR)

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
            if sensor['source'] != None:
                relayR['source'] = relay['source']
                sources.append(relayR)
            #zone type relays
            elif sensor['zone'] != None:
                relayR['zone'] = relay['zone']
                zones.append(relayR)

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
api.add_resource(Actor, "/actor/<string:name>")
api.add_resource(Info, "/info")

#run in non-debug mode, on port <whatever the user set in the config file>, open to all network (not very secure)
app.run(debug=False, port=port, host='0.0.0.0')
