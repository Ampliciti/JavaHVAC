#This is the entry point for our RaspberryPi based Python API for communicating sensor data and taking action changing relays/switches/valves/pumps/etc
#Also, this may be the best file name I've ever had in an application.

from flask import Flask
from flask_restful import Api, Resource, reqparse

app = Flask(__name__)
api = Api(app)

sensors = [
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
        for sensor in sensors:
            if(name == sensor["name"]):
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

app.run(debug=False)