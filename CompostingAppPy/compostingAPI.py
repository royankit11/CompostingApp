import pyodbc
from flask import Flask, request, jsonify
from flask_restful import Resource, Api


app = Flask(__name__)
objapi = Api(app)

class getCompostUser(Resource):
    def get(self, strUsername, strPassword):
        DBfile = "C:\\HOME\\Rik\\Projects\\Python\\CompostingApp\\CompostingDB.accdb"

        conn = pyodbc.connect('Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ='+DBfile)
        cur = conn.cursor()

        SQL = "SELECT Username, Password, Address1, Address2, Town, State, Country, Latitude, Longitude, Email, TypeOfService, OrgName FROM CompostUserData WHERE UCase(Username) = '" + strUsername.upper() + "'"
        cur.execute(SQL)
        users = cur.fetchall()
        cur.close()
        conn.close()

        userData = {}

        if users:
            if strPassword == str(users[0][1]):        
                userData["Username"] = strUsername
                userData["Address1"] = users[0][2]
                userData["Address2"] = users[0][3]
                userData["State"] = users[0][4]
                userData["Town"] = users[0][5]
                userData["Country"] = users[0][6]
                userData["Latitude"] = users[0][7]
                userData["Longitude"] = users[0][8]
                userData["Email"] = users[0][9]
                userData["TypeOfService"] = users[0][10]
                userData["OrgName"] = users[0][11]
                userData["Error"] = ""
                return userData

            else:
                userData["Error"] = "Invalid Password"
                return userData
        else:
            userData["Error"] = "Invalid Username"
            return userData

class RegisterComposter(Resource):
    def get(self, strUsername, strPassword, strAddress1, strAddress2, strTown, strState, strCountry, intLat, intLng, strEmail, strTypeOfService, strOrgName):
        
        DBfile = "C:\\HOME\\Rik\\Projects\\Python\\CompostingApp\\CompostingDB.accdb"

        conn = pyodbc.connect('Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ='+DBfile)
        cur = conn.cursor()
        message = {}

        SQL = "SELECT Username FROM CompostUserData WHERE UCase(Username) = '" + strUsername.upper() + "'"
        cur.execute(SQL)
        users = cur.fetchall()
        cur.close()
        conn.close()

        if users:
            message["Message"] = "Username Already Exists"
            return jsonify(message)
        
        else:
            conn2 = pyodbc.connect('Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ='+DBfile)
            cur2 = conn2.cursor()

            SQL = "INSERT INTO CompostUserData (Username, Password, Address1, Address2, Town, State, Country, Latitude, Longitude, Email, TypeOfService, OrgName)"
            SQL = SQL + " VALUES ('" + strUsername + "' ,'" + strPassword + "' ,'" + strAddress1 + "' ,'" + strAddress2 + "' ,'" + strTown + "' ,'" + strState + "' ,'"
            SQL = SQL + strCountry + "' ," + intLat + ", " + intLng  + ", '" + strEmail + "', '" + strTypeOfService + "' ,'" + strOrgName + "')"

            print(SQL)
           
            cur2.execute(SQL)
            conn2.commit()
            cur2.close()
            conn2.close()

            message["Message"] = "SUCCESS"
            
            return jsonify(message)

class getClientUser(Resource):
    def get(self, strUsername, strPassword):
        DBfile = "C:\\HOME\\Rik\\Projects\\Python\\CompostingApp\\CompostingDB.accdb"

        conn = pyodbc.connect('Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ='+DBfile)
        cur = conn.cursor()

        SQL = "SELECT Username, Password, Email, FirstName, LastName FROM ClientUserData WHERE UCase(Username) = '" + strUsername.upper() + "'"
        cur.execute(SQL)
        users = cur.fetchall()
        cur.close()
        conn.close()

        userData = {}

        if users:
            if strPassword == str(users[0][1]):        
                userData["Username"] = strUsername
                userData["Email"] = users[0][2]
                userData["FirstName"] = users[0][3]
                userData["LastName"] = users[0][4]
                userData["Error"] = ""
                return userData

            else:
                userData["Error"] = "Invalid Password"
                return userData
        else:
            userData["Error"] = "Invalid Username"
            return userData

class RegisterClient(Resource):
    def get(self, strUsername, strPassword, strEmail, strFirstName, strLastName):
        
        DBfile = "C:\\HOME\\Rik\\Projects\\Python\\CompostingApp\\CompostingDB.accdb"

        conn = pyodbc.connect('Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ='+DBfile)
        cur = conn.cursor()
        message = {}

        SQL = "SELECT Username FROM ClientUserData WHERE UCase(Username) = '" + strUsername.upper() + "'"
        cur.execute(SQL)
        users = cur.fetchall()
        cur.close()
        conn.close()

        if users:
            message["Message"] = "Username Already Exists"
            return jsonify(message)
        
        else:
            conn2 = pyodbc.connect('Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ='+DBfile)
            cur2 = conn2.cursor()
            
            SQL = "INSERT INTO ClientUserData (Username, Password, Email, FirstName, LastName)"
            SQL = SQL + "VALUES ('" + strUsername + "' ,'" + strPassword + "' ,'" 
            SQL = SQL + strEmail + "' ,'" + strFirstName + "' ,'" + strLastName + "')"
            cur2.execute(SQL)
            conn2.commit()
            cur2.close()
            conn2.close()
    
            message["Message"] = "SUCCESS"
            
            return jsonify(message)

class getCompostLocations(Resource):
    def get(self):
        DBfile = "C:\\HOME\\Rik\\Projects\\Python\\CompostingApp\\CompostingDB.accdb"

        conn = pyodbc.connect('Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ='+DBfile)
        cur = conn.cursor()

        SQL = "SELECT Latitude, Longitude, OrgName, Address1, Town, State, Email FROM CompostUserData"
        cur.execute(SQL)
        locations = cur.fetchall()
        cur.close()
        conn.close()

        location_arr = []

        if locations:
            for i in range (0, len(locations)):
                location_dict = {}
                location_dict["Latitude"] = locations[i][0]
                location_dict["Longitude"] = locations[i][1]
                location_dict["OrgName"] = locations[i][2]
                location_dict["Address1"] = locations[i][3]
                location_dict["Town"] = locations[i][4]
                location_dict["State"] = locations[i][5]
                location_dict["Email"] = locations[i][6]
                location_dict["Error"] = ""

                location_arr.append(location_dict)
        else:
            location_dict = {}
            location_dict["Latitude"] = ""
            location_dict["Longitude"] = ""
            location_dict["OrgName"] = ""
            location_dict["Address1"] = ""
            location_dict["Town"] = ""
            location_dict["State"] = ""
            location_dict["Email"] = ""
            userData["Error"] = "No compost locations found"

            location_arr.append(location_dict)

        return jsonify(location_arr)
    
objapi.add_resource(getCompostUser, '/getCompostUser/<strUsername>/<strPassword>/')
objapi.add_resource(RegisterComposter, "/RegisterComposter/<strUsername>/<strPassword>/<strAddress1>/<strAddress2>/<strTown>/<strState>/<strCountry>/<intLat>/<intLng>/<strEmail>/<strTypeOfService>/<strOrgName>")
objapi.add_resource(getClientUser, '/getClientUser/<strUsername>/<strPassword>/')
objapi.add_resource(RegisterClient, "/RegisterClient/<strUsername>/<strPassword>/<strEmail>/<strFirstName>/<strLastName>")
objapi.add_resource(getCompostLocations, "/getCompostLocations")

#app.run(debug=True)
app.run(host='0.0.0.0', port=5000)

    





