A jave based Multithreaded Download Manager that can :
- download a file in chunks hence making it possible to download a file from multiple sources at the same time.
- resume file transfer from a point closest to the point of failure in case of an interrupted download.
- maintain a priority queue for lining up and executing parallel downloads.
- MD5 check a file after download if the original MD5 is available.
etc.

This is a simple framework that can be plugged into a java appilcation to fetch files from known sources.
A UI can be created around it to make it a stand alone DownloadManager if required.