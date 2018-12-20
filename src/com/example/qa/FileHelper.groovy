package com.example.qa

import com.cloudbees.groovy.cps.NonCPS
import groovy.io.FileType

/**
 * File helper utilities.
 */

class FileHelper {
    @NonCPS
    static List<File> listsAllFilesOld(File dir) {
        List<File> children = new ArrayList<>()
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles()
            for (int i = 0; i < files.length; i++) {
                File file = files[i]
                if (file.isFile()) {
                    children.add(file)
                } else {
                    children.addAll(listsAllFilesOld(file))
                }
            }
        }
        return children
    }

    @NonCPS
    static List<File> listsAllFiles(File dir) {
        List<File> children = new ArrayList<>()
        if (dir.exists() && dir.isDirectory()) {
            dir.eachFileRecurse(FileType.FILES) { file ->
                children.add(file)
            }
        }
        return children
    }

    static String hello(String name) {
        return "hello $name"
    }
}
