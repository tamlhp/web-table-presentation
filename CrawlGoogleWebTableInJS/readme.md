#Crawl Google WebTable

##1. Server
run:
	node server.js <keywords file> [num]
* `keywords file`'s structure is similar to keywords.txt
* `num` is number of table which need to crawl
* files will be put in `./download/keyword`

Require: `nodejs`, add class path

##2. Client
Chrome Extension.
Load `extension` folder in `chrome://extensions`
Go to Google Tables website
Click `Run Script` button to start.
Note: start sever first.