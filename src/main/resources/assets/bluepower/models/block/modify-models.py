#!/usr/bin/env python3
import os
import sys
import shutil

if len(sys.argv) <= 1:
    print("Must provide folder path!")
    sys.exit(1)

path = sys.argv[1]
sides = ["left", "right", "front"]
for s in sides:
    files = {
        f"{path}/{s}_on.json" : f"{path}/{s}_disabled.json",
        f"{path}/{s}_on_z.json" : f"{path}/{s}_disabled_z.json"
    }
    for s2, d in files.items():
        if os.path.isfile(s2):
            _ = shutil.copyfile(s2, d)
            with open(d, "r+") as f:
                contents = f.read().replace("bluestone_on", "disabled")
                _ = f.seek(0)
                _ = f.write(contents)
