#!/usr/bin/env python3

import os

def main():
    os.system(" ./gradlew :processor_annotations:publishToMavenLocal")
    os.system(" ./gradlew :converters:publishToMavenLocal")
    os.system(" ./gradlew :processor:publishToMavenLocal")

if __name__ == "__main__":
    main()