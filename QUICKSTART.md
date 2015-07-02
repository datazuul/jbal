#JOpac2 Quick-start Installation

# 5 steps #

  1. Install java on your computer
  1. create a directory in your root with name 'siti' (mkdir /siti)
  1. download the war file and rename it as JSites.zip
  1. unzip it (assume it is in /JSites directory
  1. in the /JSites directory give

java -cp jettylib/jetty-6.1.20.jar:jettylib/jetty-util-6.1.20.jar:jettylib/servlet-api-2.5-20081211.jar:WEB-INF/classes JSites.main.Start

Point your browser at http://localhost:8080/JSites and enjoy!
(we're changing the app name: if the above doesn't run try http://localhost:8080/JOpac2)

P.S.: to login and add content go to http://localhost:8080/JSites/login (login admin/admin)
(we're changing the app name: if the above doesn't run try http://localhost:8080/JOpac2/login)

# First time #
If you get an error, try to load this page:
http://localhost:8080/JOpac2/pageview?pid=0

This is needed only once at installation to make the application selfbuild the database and directory structure. No need to do that when upgrading.

That is a known bug we will solve asap.

# Questions? #
Please, write to the user forum!