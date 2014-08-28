package com.bluepowermod.part;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.bluepowermod.BluePower;

public class PartFileSyncer {
    private static final String PATH = BluePower.proxy.getSavePath() + "\\bluepower\\partIds\\";

    private static PartFileSyncer INSTANCE = new PartFileSyncer();

    public static PartFileSyncer instance() {
        return INSTANCE;
    }

    public List<String> getPartIds(Iterable<String> toBeMapped) {
        List<String> partIds = new ArrayList<String>();
        File file = new File(PATH);

        file.mkdirs();
        file = new File(PATH + "partIds.txt");
        if (file.exists()) {
            try {
                FileInputStream fos = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(fos, "UTF-8"));
                String line = br.readLine();
                while (line != null) {
                    partIds.add(line);
                    line = br.readLine();
                }
                br.close();
            } catch (Throwable e) {
                BluePower.log.error("Part id retrieval failed!");
                e.printStackTrace();
            }
        }

        for (String s : toBeMapped) {
            if (!partIds.contains(s)) {
                partIds.add(s);
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            for (String s : partIds) {
                br.write(s + System.getProperty("line.separator"));
            }
            br.close();
        } catch (Throwable e) {
            BluePower.log.error("Part id writing failed!");
            e.printStackTrace();
        }

        return partIds;
    }
}
