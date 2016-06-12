package org.hackerpeers.sqli.analyzer;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.hackerpeers.sqli.config.ISQLIAnalyzerConfig;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Queue;

/**
 * In-memory implementation of the repository; unless data is saved externally, all data is lost when the JVM shuts down.
 * @author Simon Berthiaume (sberthiaume@gmail.com) based on Pierre-Luc Dupont (pldupont@gmail.com) work
 */
class SQLInjectionRepositoryFixedInMemory implements ISQLInjectionRepository {
    private final Table<String, String, SQLInjectionAnalyzerEntry> contentBuffer;
    private ISQLIAnalyzerConfig cfg;
    private Queue<SQLInjectionAnalyzerEntry> temporalBuffer;
    private int capacity;


    /**
     *
     * @param bufferSize The size of the internal buffer; when capacity is reached, older entries will be removed to allocate space.
     */
    SQLInjectionRepositoryFixedInMemory(ISQLIAnalyzerConfig cfg, int bufferSize) {
        this.cfg = cfg;
        capacity = bufferSize;
        temporalBuffer = new ArrayDeque<>(capacity);
        contentBuffer = HashBasedTable.create();
    }

    @Override
    public void addStatement(final String entryPoint, final String statement, final int inClauseVariant) throws IOException {
        failIfClosed();

        SQLInjectionAnalyzerEntry newEntry = new SQLInjectionAnalyzerEntry(entryPoint, statement, inClauseVariant);

        synchronized(contentBuffer) {
            SQLInjectionAnalyzerEntry oldEntry = contentBuffer.get(entryPoint, statement);
            if (oldEntry != null) {
                oldEntry.mergeStatementCall(newEntry);
            } else {
                makeRoomForNewEntry();
                contentBuffer.put(entryPoint, statement, newEntry);
                temporalBuffer.offer(newEntry);
            }
        }
    }

    private void makeRoomForNewEntry() {
        if (temporalBuffer.size() == capacity) {
            SQLInjectionAnalyzerEntry oldEntry = temporalBuffer.poll();
            contentBuffer.remove(oldEntry.getEntryPoint(), oldEntry.getStatement());
        }
    }

    /**
     * Get the full content of the repository.
     * @return An <i>unmmodifiable</i> collection of all the entries (could still be modified internally).
     * @throws IOException
     * @see Collections#unmodifiableCollection(Collection)
     */
    @Override
    public Collection<SQLInjectionAnalyzerEntry> getEntries() throws IOException {
        failIfClosed();
        return Collections.unmodifiableCollection(contentBuffer.values());
    }

    @Override
    public void close() throws IOException {
        synchronized(contentBuffer) {
            contentBuffer.clear();
            temporalBuffer.clear();
            capacity = -1;
        }
    }

    private void failIfClosed() throws IOException {
        synchronized(contentBuffer) {
            if (capacity < 0) {
                throw new IOException("Repository closed");
            }
        }
    }
}
