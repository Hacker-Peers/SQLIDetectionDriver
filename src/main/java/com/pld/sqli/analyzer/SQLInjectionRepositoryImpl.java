package com.pld.sqli.analyzer;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import com.pld.sqli.config.ISQLIAnalyzerConfig;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

/**
 * @author Simon Berthiaume (sberthiaume@gmail.com) based on Pierre-Luc Dupont (pldupont@gmail.com) work
 */
public class SQLInjectionRepositoryImpl implements ISQLInjectionRepository {
    private ISQLIAnalyzerConfig cfg;

    private final Table<String, SQLInjectionAnalyzerEntry, SQLInjectionAnalyzerEntry> entries = HashBasedTable.create();
    private boolean isFlushing = false;
    private XStream xs;

    /**
     *
     * @param cfg Injected config.
     */
    public SQLInjectionRepositoryImpl(ISQLIAnalyzerConfig cfg) {
        this.cfg = cfg;
    }

    @Override
    public void addStatement(String entryPoint, String statement, int inClauseVariant) throws IOException {

        SQLInjectionAnalyzerEntry newEntry = new SQLInjectionAnalyzerEntry(entryPoint, statement, inClauseVariant);

        synchronized(entries) {
            SQLInjectionAnalyzerEntry oldEntry = entries.get(entryPoint, newEntry);
            if (oldEntry != null) {
                oldEntry.mergeStatementCall(newEntry);
            } else {
                entries.put(entryPoint, newEntry, newEntry);
            }
        }

        flushIfNecessary(false);
    }

    private void flushIfNecessary(boolean force) throws IOException {
        boolean needsToFlush = false;

        //Only the first thread that enters this block needs to be the one flushing, but we don't want this to block the other threads.
        synchronized(entries) {
            needsToFlush = cfg.isAnalyzerUseDiskStorage()
                                && (!isFlushing)
                                && (entries.rowKeySet().size() >= cfg.getAnalyzerMaxSizeInMemory()
                                    || force);
            if (needsToFlush) {
                isFlushing = true;
            }
        }

        if (needsToFlush) {
            storeEntriesToDisk();
        }
    }

    @Override
    public void flush() throws IOException {
        flushIfNecessary(true);
    }

    /**
     * Store current entries to disk.
     * If there already is a disk file, merge the results.
     */
    void storeEntriesToDisk() throws IOException {
        File location = new File(cfg.getAnalyzerStoragePath(), "SQLIAnalyzerDiskStorage.xml");

//        Map<String, Map<String, SQLInjectionAnalyzerEntry>> result;
        if (location.exists()) {
            FileInputStream is = new FileInputStream(location);
            Table<String, SQLInjectionAnalyzerEntry, SQLInjectionAnalyzerEntry> onDisk =
                    (Table<String, SQLInjectionAnalyzerEntry, SQLInjectionAnalyzerEntry>) getXStream().fromXML(is);
            is.close();

            onDisk.values().parallelStream()
                    .forEach((e) -> {
                        synchronized (entries) {
                            SQLInjectionAnalyzerEntry oldEntry = entries.get(e.getEntryPoint(), e);
                            if (oldEntry != null) {
                                oldEntry.mergeStatementCall(e);
                            } else {
                                entries.put(e.getEntryPoint(), e, e);
                            }
                        }
                    });
        } else {
            location.getParentFile().mkdirs();
        }

        // Store merge result.
        FileOutputStream os = new FileOutputStream(location, false);
        synchronized (entries) {
            getXStream().toXML(entries, os);
            os.close();
            entries.clear();
            isFlushing = false;
        }
    }

    /**
     * Because creating the XStream is heavy, we lazy-create it and keep it internally; we could even make this static since it's supposedly thread-safe.
     * @return Usable XStream object.
     */
    private XStream getXStream() {
        if (xs == null) {
            xs = new XStream();
        }

        return xs;
    }

    @Override
    public Collection<SQLInjectionAnalyzerEntry> getEntries() throws IOException {
        Table<String, SQLInjectionAnalyzerEntry, SQLInjectionAnalyzerEntry> result;
        if (cfg.isAnalyzerUseDiskStorage())  {
            storeEntriesToDisk();
            File location = new File(cfg.getAnalyzerStoragePath(), "SQLIAnalyzerDiskStorage.xml");
            FileInputStream is = new FileInputStream(location);
            result = (Table<String, SQLInjectionAnalyzerEntry, SQLInjectionAnalyzerEntry>) getXStream().fromXML(is);
            is.close();
        } else {
            result = entries;
        }
        return result.values();

    }
}
