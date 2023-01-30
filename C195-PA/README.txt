
-- Java Database Application: Scheduling --

The purpose of this application is to provide the user a seamless way to interact with the company's database by adding, updating, and removing customers and appointments.  
All user login activity is logged.  
The user is able to view both customer and appointment tables from the database, with dates and times displayed in user's local time.

Author:  		        Kyle Fanene
Contact Information:  		kfanene@wgu.edu
Application version:  		1.0.0
Date:  			        January 28, 2024

IDE, module/driver information:
- IntelliJ IDEA 2021.1.3 (Community Edition)
- Java SE 17.0.1
- JavaFX-SDK-17.0.1
- mysql-connector-java-8.0.25

How to run program:
- Launch application
- Login with valid credentials
- Navigate to either Customers or Appointments records from the Welcome screen.
- User may add, update, or delete records from database as desired.
    Note that customers/appointments must be selected before updates or deletes can execute.
    Note all dates and times are displayed in default time zone set on user's device.
- User may filter customers and appointments tables by ID or name/title.
- User may view supplemental reports from the Appointments screen.
- Exit program either by the default method for your operating system, or by navigating to
    the Welcome screen and clicking "Exit."

Description of additional report:
The third report on the Reports screen calculates the total number of customers by country.
This report provides the user the exact distribution of customers by country.
