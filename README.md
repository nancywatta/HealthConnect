HealthConnect
=====
Server side Environment
Created on 2014/8/25
Modified on 2014/9/12

1.	JDK –I assume you all have this
2.	Eclipse for EE development
a)	Not Eclipse standard or Eclipse for Android. If you don’t have one, download the latest version-‘Luna’.
3.	Tomcat 7 Server
a)	Download and install it. Note that you should set up classpath for tomcat itself.
b)	If you double click “startup.bat” under tomcat bin folder and this windows shows up:
 
Then open a bowser with url: localhost:8080
 
These shows that your tomcat has successfully started
c)	For eclipse, you should also setup like this: (change to your own tomcat home path)
 
You will see there are icons on the top:
 
Click that “Cat”-(this cat means tomcat) to start tomcat server. Note: it is supposed to have same result either start from tomcat itself or start from eclipse.
4.	MySQL 5.6
a)	Download MySQL and install it. Note that charset should be ‘UTF-8’, root name and password could be, for example in this HC project, root/root.
b)	Start your MySQL service (run services.msc )
 
This cmd window shows your mysql has been installed successfully.
 

c)	Usually, it would be convenient to install a client tool for MySQL like: SQLyog
 
 
d)	Execute all sql commands to create tables. You can execute “createTables.sql” under “HealthConnect\doc\db”
 

5.	Maven
a)	This is a maven project. So you need first download and install maven in your laptop and then install maven plugin in your eclipse. –just google it.
b)	Then you may download ur HC project from Github repository to your workspace
c)	Import as a maven project:
 
Overall, the project would be like this in eclipse:
 
Note that when you first build maven project, it takes some time to download all dependent libs from remote. Just be patient

6.	The project uses Spring4, Hibernate4, MySQL5.6. It is somehow necessary to get a bit basic servlet knowledge as its based servlet. (Junit also embedded in).
In “APNController.java”. List, find, update, delete have been tested.
Right Click the project Run as…Run on server-
 
 Since we haven’t created any jsp files or other pages for this application, we can only see the welcome page (”Hello World”).
this shows hc project is running correctly.
The request should follow this url pattern: 
http://localhost:8080/HealthConnect/ApnUser/showAll
 

 

Then your url will call the method with “@RequestMapping(value="/showAll")” in the specified controller(“/ApnUser”)
This briefly how it works.
And for result, I just print it out in console window:
 

7.	Test your rest web services api.
Open a browser with the url like: http://localhost:8080/HealthConnect/api/index.html
Then it will show a UI layout:
 
You can input parameters under each method and then test:
 
 
========

761Project
