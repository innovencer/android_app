package com.advante.golazzos.Helpers;

/**
 * Created by Ruben Flores on 2/9/2016.
 */

import com.advante.golazzos.Interface.IChangelistener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author jon
 */
public class Decompress {
    private FileInputStream _zipFile;
    private String _location;
    IChangelistener _iChangelistener;
    public Decompress(FileInputStream zipFile, String location, IChangelistener iChangelistener) {
        _zipFile = zipFile;
        _location = location;
        _iChangelistener = iChangelistener;
        _dirChecker("");
    }

    public void unzip() {
        try {
            //FileInputStream inputStream = new FileInputStream(_zipFile);
            FileInputStream inputStream = _zipFile;
            ZipInputStream zipStream = new ZipInputStream(inputStream);
            ZipEntry zEntry = null;
            //Log.d("Golazzos","unzipping");
            while ((zEntry = zipStream.getNextEntry()) != null) {
                //Log.d("Unzip", "Unzipping " + zEntry.getName() + " at "
                        //+ _location);

                if (zEntry.isDirectory()) {
                    _dirChecker(zEntry.getName());
                } else {
                    _iChangelistener.onChange(zEntry.getName());
                    FileOutputStream fout = new FileOutputStream(
                            this._location + "/" + zEntry.getName());
                    BufferedOutputStream bufout = new BufferedOutputStream(fout);
                    byte[] buffer = new byte[1024];
                    int read = 0;
                    while ((read = zipStream.read(buffer)) != -1) {
                        bufout.write(buffer, 0, read);
                    }

                    zipStream.closeEntry();
                    bufout.close();
                    fout.close();
                }
            }
            zipStream.close();
            //Log.d("Unzip", "Unzipping complete. path :  " + _location);
        } catch (Exception e) {
            //Log.d("Unzip", "Unzipping failed");
            e.printStackTrace();
        }
    }

    private void _dirChecker(String dir) {
        File f = new File(_location + dir);

        if(!f.isDirectory()) {
            f.mkdirs();
        }
    }
}

