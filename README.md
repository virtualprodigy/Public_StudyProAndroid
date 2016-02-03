# StudyPro Android Public Repo
This is Study Pro, a re-branding of an app I created back in college at least 4 years ago.
The original version of the app, CramSlam(http://tiny.cc/CramSlamAndroid), is available on the market 
but due to a corrupted keystore and predating my ownership of a GitHub account, I can't update that application. 

With those juicy lemons, I've made a nice open Source lemonade project to show off my skills. The app
will now receive a new UI as well as some much need code refactoring. I strongly suggest looking at the commit history 
to see which files have been modified if you're browsing to see solid code. The project is very much a rewrite 
and some of the older code is just ugly and not that great. Feel free to run the app to see the current progress
of the app and its code. Also check the change log below to get an idea of where the project progress is. This app will
be publish to the android market and I'll provide a link. Also this is an open source project, some data maybe removed
as I've determined it is sensitive but this will not effect the app from running.  

#Change Log
###Version 1.0
  1. Adding the Toolbar and Navigation Drawer
  2. Added an EventBus, Orm and a few other nice libraries that will be helpful later
  3. Updated the package structure of the application 
  4. Updated my Eula to use an html file instead of being in the strings.xml
  5. Delete old alternate layout files, I'll create new ones later if needed 
  6. Removing the Tab layout 
     1. Added the new adapter and array.xml to crete the navigation drawer items
        1. currently only the StudyTimer Fragment has been setup in the drawer
     2. Working on the Study Timer's new UI
        1. Scrapping the old service implementation to use alarms for the timer
            1. (In Development) Currently the timer will start with the old service but it may crash the app
        2. working on a new implementation to dispatch the timed break notifications
            1. Adding the break notifications via an assets json file
         
#License
/* Copyright (C) Matthew Butler - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential. 
 *{StudyPro Android Public Repo} can not be copied and/or distributed without the express
  * permission of Matthew Butler
 */
