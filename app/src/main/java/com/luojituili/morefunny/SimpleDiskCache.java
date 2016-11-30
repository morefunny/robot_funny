package com.luojituili.morefunny;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sherlockhua on 2016/11/28.
 */

public class SimpleDiskCache {

    private DiskLruCache _diskCache = null;
    private static HashMap<String, SimpleDiskCache> _instanceMap = new HashMap<>();

    private SimpleDiskCache(File dir, int version, long maxSize) throws IOException{

        if (!dir.exists()) {
            dir.mkdirs();
        }

        _diskCache = DiskLruCache.open(dir, version, 1, maxSize);
    }

    public static synchronized SimpleDiskCache open(String cacheName, File dir, int version, long maxSize) throws IOException{

        if (_instanceMap.containsKey(cacheName)) {
            return _instanceMap.get(cacheName);
        }

        SimpleDiskCache cache = new SimpleDiskCache(dir, version, maxSize);
        _instanceMap.put(cacheName, cache);
        return cache;

    }

    public static synchronized SimpleDiskCache getCache(String cacheName) throws FileNotFoundException {
        if (!_instanceMap.containsKey(cacheName)) {
            throw new FileNotFoundException();
        }

        return _instanceMap.get(cacheName);
    }
    public void put(String key, String value) throws  IOException {

        DiskLruCache.Editor editor = _diskCache.edit(key);
        if (editor != null) {
            try {
                editor.set(0, value);
                editor.commit();
            } catch (Exception e) {
                editor.abort();
                e.printStackTrace();
            }
        }
    }

    public String get(String key) throws IOException, Exception {
        DiskLruCache.Snapshot snapShot = _diskCache.get(key);
        if (snapShot == null) {
            throw  new Exception(String.format("diskcache get[%s] failed", key));
        }

        return snapShot.getString(0);
    }

    public<T> void putArrayList(String key, ArrayList<T> list) throws  IOException{
        DiskLruCache.Editor editor = _diskCache.edit(key);
        if (editor != null) {
            try {
                OutputStream output = editor.newOutputStream(0);
                ObjectOutputStream objOutput = new ObjectOutputStream(output);
                objOutput.writeObject(list);

                editor.commit();
            } catch (Exception e) {
                editor.abort();
                e.printStackTrace();
            }
        }
    }

    public<T> ArrayList<T> getArrayList(String key) throws IOException, Exception {

        DiskLruCache.Snapshot snapShot = _diskCache.get(key);
        if (snapShot == null) {
            throw  new Exception(String.format("diskcache get[%s] failed", key));
        }

        InputStream input = snapShot.getInputStream(0);
        ObjectInputStream objInput = new ObjectInputStream(input);
        return (ArrayList<T>)objInput.readObject();
    }
}
