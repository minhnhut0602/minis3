# miniS3

[![Build Status](https://travis-ci.org/prafsoni/minis3.svg?branch=master)](https://travis-ci.org/prafsoni/minis3)

This is a coding assignment for FINRA Full-stack Java Developer Position.
And, I am calling it as miniS3 because it treats everything(files) as Object and store them on disk, 
just like AWS S3. Nope! I am not claiming in anyway that this is S3 or even close to it, it just shares that notion of 
treating everything as Objects.

So, what can you do with it!, you can upload file with metadata(default and custom),
while metadata is stored on persistence store and file on disk, you
can upload file over 2GB of size download file as stream.

### Requirements for Assignment:

Implement a RESTful API spring-boot application that provides the following APIs:
* API to upload a file with a few meta-data fields. Persist meta-data in persistence store (In memory DB or file system and store the content on a file system)
* API to get file meta-data
* API to download content stream (Optional)
* API to search for file IDs with a search criterion (Optional)
* Write a scheduler in the same app to poll for new items in the last hour and send an email (Optional)

### What I did:
All except search(didn't get time), but what I did extra added OAuth2 and JWT Tokens with spring-security.

### What I might do in future(i.e. when I don't have anything to do.):
Enhance it and make it actually usable in real world.