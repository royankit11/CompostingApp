import pyodbc
from flask import Flask, request, jsonify
from flask_restful import Resource, Api


app = Flask(__name__)
objapi = Api(app)

class getCompostUser(Resource):
    def get(self, strUsername, strPassword, blEditProfile):
        DBfile = "C:\\Users\\Rik\\Desktop\\CompostingApp\\CompostingAppPy\\CompostingDB.accdb"

        conn = pyodbc.connect('Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ='+DBfile)
        cur = conn.cursor()

        SQL = "SELECT Username, Password, Address1, Address2, Town, State, Country, Latitude, Longitude, Email, TypeOfService, OrgName, ID FROM CompostUserData WHERE UCase(Username) = '" + strUsername.upper() + "'"
        cur.execute(SQL)
        users = cur.fetchall()
        cur.close()
        conn.close()

        userData = {}

        if users:
            if blEditProfile == "true":
                userData["Username"] = strUsername
                userData["Password"] = users[0][1]
                userData["Address1"] = users[0][2]
                userData["Address2"] = users[0][3]
                userData["Town"] = users[0][4]
                userData["State"] = users[0][5]
                userData["Country"] = users[0][6]
                userData["Email"] = users[0][9]
                userData["TypeOfService"] = users[0][10]
                userData["OrgName"] = users[0][11]
                userData["ID"] = users[0][12]
                userData["Error"] = ""
                return userData
            else:    
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
                    userData["ID"] = users[0][12]
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
        
        DBfile = "C:\\Users\\Rik\\Desktop\\CompostingApp\\CompostingAppPy\\CompostingDB.accdb"

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

class updateProfileComposter(Resource):
    def get(self, strUsername, strPassword, strAddress1, strAddress2, strTown, strState, strCountry, intLat, intLng, strEmail, strTypeOfService, strOrgName, intID):
        
        DBfile = "C:\\Users\\Rik\\Desktop\\CompostingApp\\CompostingAppPy\\CompostingDB.accdb"

        conn = pyodbc.connect('Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ='+DBfile)
        cur = conn.cursor()
        message = {}

        SQL = "SELECT Username FROM CompostUserData WHERE UCase(Username) = '" + strUsername.upper() + "' AND ID = " + intID
        cur.execute(SQL)
        users = cur.fetchall()
        cur.close()
        conn.close()

        if users:
            message["Message"] = "ERROR"
            return jsonify(message)
        else:                    
            SQL = "UPDATE CompostUserData SET Username = '" + strUsername + "', Password = '" + strPassword + "', Address1 = '" + strAddress1 + "',"
            SQL = SQL + " Address2 = '" + strAddress2 + "', Town = '" + strTown + "', State = '" + strState + "', Country = '" + strCountry + "',"
            SQL = SQL + " Latitude = " + intLat + ", Longitude = " + intLng + ", Email = '" + strEmail + "', TypeOfService = '" + strTypeOfService + "',"
            SQL = SQL + " OrgName = '" + strOrgName + "' WHERE ID = " + intID

        conn2 = pyodbc.connect('Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ='+DBfile)
        cur2 = conn2.cursor()
        message2 = {}
        
        cur2.execute(SQL)
        conn2.commit()
        cur2.close()
        conn2.close()

        message2["Username"] = strUsername
        message2["Message"] = "SUCCESS"
        
        return jsonify(message2)
               


class getClientUser(Resource):
    def get(self, strUsername, strPassword, blEditProfile):
        DBfile = "C:\\Users\\Rik\\Desktop\\CompostingApp\\CompostingAppPy\\CompostingDB.accdb"

        conn = pyodbc.connect('Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ='+DBfile)
        cur = conn.cursor()

        SQL = "SELECT Username, Password, Email, FirstName, LastName, ID FROM ClientUserData WHERE UCase(Username) = '" + strUsername.upper() + "'"
        cur.execute(SQL)
        users = cur.fetchall()
        cur.close()
        conn.close()

        userData = {}

        if users:
            if blEditProfile == "true":
                userData["Username"] = strUsername
                userData["Password"] = users[0][1]
                userData["Email"] = users[0][2]
                userData["FirstName"] = users[0][3]
                userData["LastName"] = users[0][4]
                userData["ID"] = users[0][5]
                userData["Error"] = ""
                return userData
            else:
                if strPassword == str(users[0][1]):        
                    userData["Username"] = strUsername
                    userData["Password"] = strPassword
                    userData["Email"] = users[0][2]
                    userData["FirstName"] = users[0][3]
                    userData["LastName"] = users[0][4]
                    userData["ID"] = users[0][5]
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
        
        DBfile = "C:\\Users\\Rik\\Desktop\\CompostingApp\\CompostingAppPy\\CompostingDB.accdb"

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

class updateProfileClient(Resource):
    def get(self, strUsername, strPassword, strEmail, strFirstName, strLastName, intID):
        
        DBfile = "C:\\Users\\Rik\\Desktop\\CompostingApp\\CompostingAppPy\\CompostingDB.accdb"

        conn = pyodbc.connect('Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ='+DBfile)
        cur = conn.cursor()
        message = {}

        SQL = "SELECT Username FROM ClientUserData WHERE UCase(Username) = '" + strUsername.upper() + "' AND ID = " + intID
        cur.execute(SQL)
        users = cur.fetchall()
        cur.close()
        conn.close()

        if users:
            message["Message"] = "ERROR"
            return jsonify(message)
        else:                    
            SQL = "UPDATE ClientUserData SET Username = '" + strUsername + "', Password = '" + strPassword + "', Email = '" + strEmail + "',"
            SQL = SQL + " FirstName = '" + strFirstName + "', LastName = '" + strLastName + "' WHERE ID = " + intID

        conn2 = pyodbc.connect('Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ='+DBfile)
        cur2 = conn2.cursor()
        message2 = {}
        
        cur2.execute(SQL)
        conn2.commit()
        cur2.close()
        conn2.close()

        message2["Username"] = strUsername
        message2["Message"] = "SUCCESS"
        
        return jsonify(message2)


class getCompostLocations(Resource):
    def get(self, strState):
        DBfile = "C:\\Users\\Rik\\Desktop\\CompostingApp\\CompostingAppPy\\CompostingDB.accdb"

        conn = pyodbc.connect('Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ='+DBfile)
        cur = conn.cursor()

        SQL = "SELECT Latitude, Longitude, OrgName, Address1, Town, State, Email, ID FROM CompostUserData WHERE State = '" + strState + "'"
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
                location_dict["ID"] = locations[i][7]
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
            location_dict["ID"] = ""
            location_dict["Error"] = "No compost locations found"

            location_arr.append(location_dict)

        return jsonify(location_arr)

class logEmail(Resource):
    def get(self, intSenderID, intReceiverID, strSubject):
        
        DBfile = "C:\\Users\\Rik\\Desktop\\CompostingApp\\CompostingAppPy\\CompostingDB.accdb"

        conn = pyodbc.connect('Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ='+DBfile)
        cur = conn.cursor()
        message = {}

        SQL = "INSERT INTO EmailLog (SenderID, ReceiverID, CreateDateTime, Subject)"
        SQL = SQL + "VALUES (" + intSenderID + " ," + intReceiverID + ", " 
        SQL = SQL + "Now() ,'" + strSubject + "')"
        cur.execute(SQL)
        conn.commit()
        cur.close()
        conn.close()

        message["Message"] = "SUCCESS"
        
        return jsonify(message)

class getRecentEmails(Resource):
    def get(self, intReceiverID):
        DBfile = "C:\\Users\\Rik\\Desktop\\CompostingApp\\CompostingAppPy\\CompostingDB.accdb"

        conn = pyodbc.connect('Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ='+DBfile)
        cur = conn.cursor()

        SQL = "SELECT TOP 5 FORMAT(A.CreateDateTime, 'General Date') AS ParsedCreateDate, A.Subject, B.Email"
        SQL = SQL + " FROM EmailLog A INNER JOIN ClientUserData B ON A.SenderID = B.ID"
        SQL = SQL + " WHERE A.ReceiverID = " + intReceiverID + " ORDER BY A.CreateDateTime DESC"
        cur.execute(SQL)
        emails = cur.fetchall()
        cur.close()
        conn.close()

        email_arr = []

        if emails:
            for i in range (0, len(emails)):
                email_dict = {}
                email_dict["CreateDateTime"] = emails[i][0]
                email_dict["Subject"] = emails[i][1]
                email_dict["SenderEmail"] = emails[i][2]
                email_dict["Error"] = ""

                email_arr.append(email_dict)
        else:
            email_dict = {}
            email_dict["CreateDateTime"] = ""
            email_dict["Subject"] = ""
            email_dict["SenderEmail"] = ""
            email_dict["Error"] = "No email messages found"

            email_arr.append(email_dict)

        return jsonify(email_arr)


    
objapi.add_resource(getCompostUser, '/getCompostUser/<strUsername>/<strPassword>/<blEditProfile>')
objapi.add_resource(RegisterComposter, "/RegisterComposter/<strUsername>/<strPassword>/<strAddress1>/<strAddress2>/<strTown>/<strState>/<strCountry>/<intLat>/<intLng>/<strEmail>/<strTypeOfService>/<strOrgName>")
objapi.add_resource(updateProfileComposter, "/updateProfileComposter/<strUsername>/<strPassword>/<strAddress1>/<strAddress2>/<strTown>/<strState>/<strCountry>/<intLat>/<intLng>/<strEmail>/<strTypeOfService>/<strOrgName>/<intID>")
objapi.add_resource(getClientUser, '/getClientUser/<strUsername>/<strPassword>/<blEditProfile>')
objapi.add_resource(RegisterClient, "/RegisterClient/<strUsername>/<strPassword>/<strEmail>/<strFirstName>/<strLastName>")
objapi.add_resource(updateProfileClient, "/updateProfileClient/<strUsername>/<strPassword>/<strEmail>/<strFirstName>/<strLastName>/<intID>")
objapi.add_resource(getCompostLocations, "/getCompostLocations/<strState>")
objapi.add_resource(logEmail, "/logEmail/<intSenderID>/<intReceiverID>/<strSubject>")
objapi.add_resource(getRecentEmails, "/getRecentEmails/<intReceiverID>")

#app.run(debug=True)
app.run(host='0.0.0.0', port=5000)

    





