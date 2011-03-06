from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext import db

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
        self.response.out.write("<html><body>")

        # Retrieve AgentLocations from the datastore
        agent_locations = db.GqlQuery("SELECT * FROM AgentLocation ORDER BY " +
                                      "time DESC")

        # Write a table for for location fixes
        self.response.out.write("<table><tr><th>Agent</th><th>Device</th>" +
                                "<th>Timestamp</th><th>Longitude</th>" +
                                "<th>Latitude</th></tr>")
        for fix in agent_locations:
            self.response.out.write("<tr>")
            data = (fix.agentid, fix.deviceid, fix.time, fix.longitude,
                    fix.latitude)
            for x in data:
                self.response.out.write("<td>%s</td>" % x)
            self.response.out.write("</tr>")
        self.response.out.write("</table>")

        self.response.out.write("</body></html>")

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
        self.response.out.write("<html><body>")

        # Retrieve PatientLocations from the datastore
        agent_locations = db.GqlQuery("SELECT * FROM PatientLocation " +
                                      "ORDER BY time DESC")

        # Write a table for for location fixes
        self.response.out.write("<table><tr><th>Agent</th><th>Device</th>" +
                                "<th>Timestamp</th><th>Longitude</th>" +
                                "<th>Latitude</th><th>QR Code</th></tr>")
        for fix in agent_locations:
            self.response.out.write("<tr>")
            data = (fix.agentid, fix.deviceid, fix.time, fix.longitude,
                    fix.latitude, fix.qrcode)
            for x in data:
                self.response.out.write("<td>%s</td>" % x)
            self.response.out.write("</tr>")
        self.response.out.write("</table>")

        self.response.out.write("</body></html>")
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
    urlmap = [("/agentlocation", AgentLocationPage), 
              ("/patientlocation", PatientLocationPage)]
    app = webapp.WSGIApplication(urlmap, debug=True)
    run_wsgi_app(app)
