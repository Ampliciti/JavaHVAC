#This is the entry point for our RaspberryPi based Python API for communicating sensor data and taking action changing relays/switches/valves/pumps/etc
#Also, this may be the best file name I've ever had in an application.

from flask import Flask
from flask_restful import Api, Resource, reqparse
import json
import ReadDS18B20

app = Flask(__name__)
api = Api(app)

#this probably doesn't follow python best practices, this is my first python rest app like this.

def loadSensorConfig():
    with open('sensor-config.json') as json_file:
        data = json.load(json_file)
        return data['sensors']

sensor_config = loadSensorConfig()

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




api.add_resource(Sensor, "/sensor/<string:name>")
api.add_resource(Actor, "/actor/<string:name>")
#api.add_resource(Info, "/info")

#run in non-debug mode, on port 5000, open to all network (not very secure)
app.run(debug=False, port=5000, host= '0.0.0.0')
