#!/bin/bash

curl localhost:8080/flickr/api/photo -H "Content-Type: application/json" -i -d@newphoto.json

