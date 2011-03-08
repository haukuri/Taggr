from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext import db

from google.appengine.ext.webapp import template
import os

from django.utils import simplejson as json

def to_json(entity):
    output = {}
    for key in entity.properties().iterkeys():
        output[key] = getattr(entity, key)
    return json.dumps(output)

class AgentLocation(db.Model):
    agentid = db.StringProperty()
    deviceid = db.StringProperty()
    time = db.StringProperty()
    longitude = db.StringProperty()
    latitude = db.StringProperty()

class PatientLocation(db.Model):
    agentid = db.StringProperty()
    deviceid = db.StringProperty()
    qrcode = db.StringProperty()
    time = db.StringProperty()
    longitude = db.StringProperty()
    latitude = db.StringProperty()

class AgentLocationPage(webapp.RequestHandler):
    def get(self):
        # Retrieve AgentLocations from the datastore
        agent_locations = db.GqlQuery("SELECT * FROM AgentLocation ORDER BY " +
                                      "time DESC")
        mpath = os.path.join(os.path.dirname(__file__), os.path.join('templates', 'agentlocation.kml'))
        self.response.headers['Content-Type'] = "text/plain; charset=utf-8"
        self.response.out.write(template.render(mpath, {'fixes': agent_locations}))
        
#        for fix in agent_locations:
#            self.response.out.write(to_json(fix)+"\n")

    def post(self):
        fix = AgentLocation()
        fix.agentid = self.request.get("agentid")
        fix.deviceid = self.request.get("deviceid")
        fix.time = self.request.get("time")
        fix.longitude = self.request.get("longitude")
        fix.latitude = self.request.get("latitude")
        fix.put()
        self.response.set_status(200)



class PatientLocationPage(webapp.RequestHandler):
    def get(self):
        patient_locations = db.GqlQuery("SELECT * FROM PatientLocation " +
                                      "ORDER BY time DESC")
        data = []
        for fix in patient_locations:
            efix = {}
            for key in fix.properties().iterkeys():
                efix[key] = getattr(fix, key)
            efix["color"] = "blue"
            for color in ["green", "red", "yellow"]:
                if efix["qrcode"].startswith(color):
                    efix["color"] = color
            data.append(efix)
        mpath = os.path.join(os.path.dirname(__file__), os.path.join('templates', 'patientlocation.kml'))
        self.response.headers['Content-Type'] = "text/plain; charset=utf-8"
        self.response.out.write(template.render(mpath, {'fixes': data}))
        
    def post(self):
        fix = PatientLocation()
        fix.agentid = self.request.get("agentid")
        fix.deviceid = self.request.get("deviceid")
        fix.qrcode = self.request.get("qrcode")
        fix.time = self.request.get("time")
        fix.longitude = self.request.get("longitude")
        fix.latitude = self.request.get("latitude")
        fix.put()
        self.response.set_status(200)

if __name__ == "__main__":
    urlmap = [("/agentlocation.kml", AgentLocationPage), 
              ("/patientlocation.kml", PatientLocationPage)]
    app = webapp.WSGIApplication(urlmap, debug=True)
    run_wsgi_app(app)
