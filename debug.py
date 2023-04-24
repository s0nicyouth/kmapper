#!/usr/bin/env python3

import argparse
import os

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--module", help="Module you want to build and debug. Default is :examples:testload", type=str, default=":examples:testload")
    print(parser.format_help())
    args = parser.parse_args()
    print("Debugging module %s." % args.module)
    print("In IntelliJ Idea go to Run->Attach to process... and select gradle daemon process")
    os.system("./gradlew %s:clean %s:build --no-daemon -Dorg.gradle.debug=true -Pkotlin.compiler.execution.strategy=in-process" % (args.module, args.module))


if __name__ == "__main__":
    main()